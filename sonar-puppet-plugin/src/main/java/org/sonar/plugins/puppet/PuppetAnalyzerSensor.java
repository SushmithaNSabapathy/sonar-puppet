/*
 * SonarQube Puppet Analyzer
 * Copyright (C) 2017-2017 David RACODON
 * david.racodon@gmail.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.plugins.puppet;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.sonar.sslr.api.RecognitionException;
import com.sonar.sslr.api.typed.ActionParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.issue.NoSonarFilter;
import org.sonar.api.rule.RuleKey;
import org.sonar.plugins.puppet.api.PuppetCheck;
import org.sonar.puppet.tree.Tree;
import org.sonar.plugins.puppet.api.visitors.CustomRulesDefinition;
import org.sonar.plugins.puppet.api.visitors.TreeVisitor;
import org.sonar.plugins.puppet.api.visitors.issues.Issue;
import org.sonar.puppet.CharsetAwareVisitor;
import org.sonar.puppet.checks.CheckList;
import org.sonar.puppet.metrics.MetricsVisitor;
import org.sonar.puppet.parser.PuppetParserBuilder;
import org.sonar.puppet.tree.impl.TreeImpl;
import org.sonar.puppet.visitors.CpdVisitor;
import org.sonar.puppet.visitors.PuppetTreeVisitorContext;
import org.sonar.puppet.visitors.SyntaxHighlighterVisitor;
import org.sonar.squidbridge.ProgressReport;
import org.sonar.squidbridge.api.AnalysisException;

import javax.annotation.Nullable;
import java.io.File;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class PuppetAnalyzerSensor implements Sensor {

  private static final Logger LOG = LoggerFactory.getLogger(PuppetAnalyzerSensor.class);

  private final FileSystem fileSystem;
  private final CheckFactory checkFactory;
  private final NoSonarFilter noSonarFilter;
  private final ActionParser<Tree> parser;
  private final CustomRulesDefinition[] customRulesDefinition;
  private final FilePredicate mainFilePredicate;

  private RuleKey parsingErrorRuleKey = null;
  private IssueSaver issueSaver;

  public PuppetAnalyzerSensor(FileSystem fileSystem, CheckFactory checkFactory, NoSonarFilter noSonarFilter) {
    this(fileSystem, checkFactory, noSonarFilter, null);
  }

  public PuppetAnalyzerSensor(FileSystem fileSystem, CheckFactory checkFactory, NoSonarFilter noSonarFilter,
                              @Nullable CustomRulesDefinition[] customRulesDefinition) {

    this.fileSystem = fileSystem;
    this.noSonarFilter = noSonarFilter;
    this.checkFactory = checkFactory;
    this.customRulesDefinition = customRulesDefinition;
    this.parser = PuppetParserBuilder.createParser(fileSystem.encoding());

    mainFilePredicate = fileSystem.predicates().and(
      fileSystem.predicates().hasType(InputFile.Type.MAIN),
      fileSystem.predicates().hasLanguage(PuppetLanguage.KEY));
  }

  @Override
  public void describe(SensorDescriptor descriptor) {
    descriptor
      .onlyOnLanguage(PuppetLanguage.KEY)
      .name("Puppet Analyzer Sensor")
      .onlyOnFileType(InputFile.Type.MAIN);
  }

  @Override
  public void execute(SensorContext sensorContext) {
    Checks checks = Checks.createPuppetChecks(checkFactory)
      .addChecks(CheckList.REPOSITORY_KEY, CheckList.getChecks())
      .addCustomChecks(customRulesDefinition);

    List<TreeVisitor> treeVisitors = treeVisitors(sensorContext, checks, noSonarFilter);

    // FIXME: setParsingErrorCheckIfActivated(treeVisitors);

    ProgressReport progressReport = new ProgressReport(
      "Report about progress of " + PuppetLanguage.NAME + " analyzer",
      TimeUnit.SECONDS.toMillis(10));

    progressReport.start(Lists.newArrayList(fileSystem.files(mainFilePredicate)));

    issueSaver = new IssueSaver(sensorContext, checks);
    List<Issue> issues = new ArrayList<>();

    boolean success = false;
    try {
      for (InputFile inputFile : fileSystem.inputFiles(mainFilePredicate)) {
        LOG.debug("Analyzing " + inputFile.absolutePath() + "...");
        issues.addAll(analyzeFile(sensorContext, inputFile, treeVisitors));
        progressReport.nextFile();
      }
      saveSingleFileIssues(issues);
      success = true;
    } finally {
      stopProgressReport(progressReport, success);
    }
  }

  private List<TreeVisitor> treeVisitors(SensorContext sensorContext, Checks checks, NoSonarFilter noSonarFilter) {
    List<TreeVisitor> treeVisitors = Lists.newArrayList();
    treeVisitors.addAll(checks.visitorChecks());
    treeVisitors.add(new SyntaxHighlighterVisitor(sensorContext));
    treeVisitors.add(new CpdVisitor(sensorContext));
    treeVisitors.add(new MetricsVisitor(sensorContext, noSonarFilter));
    return treeVisitors;
  }

  private List<Issue> analyzeFile(SensorContext sensorContext, InputFile inputFile, List<TreeVisitor> visitors) {
    try {
      TreeImpl cssTree = (TreeImpl) parser.parse(new File(inputFile.absolutePath()));
      return scanFile(inputFile, cssTree, visitors);

    } catch (RecognitionException e) {
      checkInterrupted(e);
      LOG.error("Unable to parse file: " + inputFile.absolutePath());
      LOG.error(e.getMessage());
      processRecognitionException(e, sensorContext, inputFile);

    } catch (Exception e) {
      checkInterrupted(e);
      throw new AnalysisException("Unable to analyze file: " + inputFile.absolutePath(), e);
    }
    return new ArrayList<>();
  }

  private List<Issue> scanFile(InputFile inputFile, TreeImpl tree, List<TreeVisitor> visitors) {
    PuppetTreeVisitorContext context = new PuppetTreeVisitorContext(tree, inputFile.file(), PuppetLanguage.KEY);
    List<Issue> issues = new ArrayList<>();
    for (TreeVisitor visitor : visitors) {
      if (visitor instanceof CharsetAwareVisitor) {
        ((CharsetAwareVisitor) visitor).setCharset(fileSystem.encoding());
      }
      if (visitor instanceof PuppetCheck) {
        issues.addAll(((PuppetCheck) visitor).scanFile(context));
      } else {
        visitor.scanTree(context);
      }
    }
    return issues;
  }

  private void saveSingleFileIssues(List<Issue> issues) {
    issues.forEach(issueSaver::saveIssue);
  }

  private void processRecognitionException(RecognitionException e, SensorContext sensorContext, InputFile inputFile) {
    if (parsingErrorRuleKey != null) {
      NewIssue newIssue = sensorContext.newIssue();

      NewIssueLocation primaryLocation = newIssue.newLocation()
        .message(e.getMessage())
        .on(inputFile)
        .at(inputFile.selectLine(e.getLine()));

      newIssue
        .forRule(parsingErrorRuleKey)
        .at(primaryLocation)
        .save();
    }
  }

  private static void stopProgressReport(ProgressReport progressReport, boolean success) {
    if (success) {
      progressReport.stop();
    } else {
      progressReport.cancel();
    }
  }

  private static void checkInterrupted(Exception e) {
    Throwable cause = Throwables.getRootCause(e);
    if (cause instanceof InterruptedException || cause instanceof InterruptedIOException) {
      throw new AnalysisException("Analysis cancelled", e);
    }
  }

  //  private void setParsingErrorCheckIfActivated(List<TreeVisitor> treeVisitors) {
//    for (TreeVisitor check : treeVisitors) {
//      if (ParsingErrorCheck.class == check.getClass()) {
//        parsingErrorRuleKey = checks.ruleKeyFor((PuppetCheck) check);
//        break;
//      }
//    }
//  }

}
