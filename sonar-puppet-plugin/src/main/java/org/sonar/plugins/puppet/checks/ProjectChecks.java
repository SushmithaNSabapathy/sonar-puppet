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
package org.sonar.plugins.puppet.checks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.InputComponent;
import org.sonar.api.batch.fs.InputDir;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.rule.RuleKey;
import org.sonar.puppet.checks.CheckList;
import org.sonar.puppet.checks.MetadataJsonFilePresentCheck;
import org.sonar.puppet.checks.ReadmeFilePresentCheck;
import org.sonar.puppet.checks.TestsDirectoryPresentCheck;

import java.io.File;

public class ProjectChecks {

  private final SensorContext context;

  private static final Logger LOGGER = LoggerFactory.getLogger(ProjectChecks.class);
  private static final int readmeCheckDepth = 4;
  private static final int metadataCheckDepth = 4;
  private static final int testsCheckDepth = 4;

  public ProjectChecks(SensorContext context) {
    this.context = context;
  }

  public void reportProjectIssues() {
    if (context.fileSystem().baseDir() != null) {
      checkTestsDirectoryPresent(context.fileSystem().baseDir(), 0);
      checkMetadataJsonFilePresent(context.fileSystem().baseDir(), 0);
      checkReadmeFilePresent(context.fileSystem().baseDir(), 0);
    }
  }

  private void checkMetadataJsonFilePresent(File parentFile, int depth) {
    for (File file : parentFile.listFiles()) {
      if (file.isDirectory()) {
        if ("manifests".equals(file.getName())) {
          boolean metadataJsonFileFound = false;
          for (File testsSiblings : parentFile.listFiles()) {
            if (testsSiblings.isFile() && "metadata.json".equals(testsSiblings.getName())) {
              metadataJsonFileFound = true;
              break;
            }
          }
          if (!metadataJsonFileFound) {
            InputDir inputDir = context.fileSystem().inputDir(parentFile);
            if (inputDir == null) {
              addIssue(MetadataJsonFilePresentCheck.RULE_KEY, "Add a \"metadata.json\" file to the \"" + context.module().key() + "\" Puppet module.", context.module());
            } else {
              addIssue(MetadataJsonFilePresentCheck.RULE_KEY, "Add a \"metadata.json\" file to the \"" + inputDir.relativePath() + "\" Puppet module.", inputDir);
            }
          }
        } else {
          depth++;
          if (depth < metadataCheckDepth) {
            checkMetadataJsonFilePresent(file, depth);
          }
        }
      }
    }
  }

  private void checkReadmeFilePresent(File parentFile, int depth) {
    for (File file : parentFile.listFiles()) {
      if (file.isDirectory()) {
        if ("manifests".equals(file.getName())) {
          boolean readmeFileFound = false;
          for (File testsSiblings : parentFile.listFiles()) {
            if (testsSiblings.isFile() && ("README.md".equals(testsSiblings.getName()) || "README.markdown".equals(testsSiblings.getName()))) {
              readmeFileFound = true;
              break;
            }
          }
          if (!readmeFileFound) {
            InputDir inputDir = context.fileSystem().inputDir(parentFile);
            if (inputDir == null) {
              addIssue(ReadmeFilePresentCheck.RULE_KEY, "Add a \"README.md\" file to the \"" + context.module().key() + "\" Puppet module.", context.module());
            } else {
              addIssue(ReadmeFilePresentCheck.RULE_KEY, "Add a \"README.md\" file to the \"" + inputDir.relativePath() + "\" Puppet module.", inputDir);
            }
          }
        } else {
          depth++;
          if (depth < readmeCheckDepth) {
            checkReadmeFilePresent(file, depth);
          }
        }
      }
    }
  }

  private void checkTestsDirectoryPresent(File parentFile, int depth) {
    for (File file : parentFile.listFiles()) {
      if (file.isDirectory()) {
        if ("tests".equals(file.getName())) {
          LOGGER.info("Path: " + file.getPath());
          for (File testsSiblings : file.getParentFile().listFiles()) {
            InputDir inputDir = context.fileSystem().inputDir(file);
            if (testsSiblings.isDirectory()
              && "manifests".equals(testsSiblings.getName())) {
              if (inputDir == null) {
                addIssue(TestsDirectoryPresentCheck.RULE_KEY, "Replace the \"tests\" directory with an \"examples\" directory.", context.module());
                break;
              } else {
                addIssue(TestsDirectoryPresentCheck.RULE_KEY, "Replace the \"" + inputDir.path() + "\" directory with an \"examples\" directory.", inputDir);
                break;
              }
            }
          }
        } else {
          depth++;
          if (depth < testsCheckDepth) {
            checkTestsDirectoryPresent(file, depth);
          }
        }
      }
    }
  }

  protected void addIssue(String ruleKey, String message, InputComponent inputComponent) {
    LOGGER.info("Adding issue: " + ruleKey + " " + message);
    NewIssue newIssue = context
      .newIssue()
      .forRule(RuleKey.of(CheckList.REPOSITORY_KEY, ruleKey));

    newIssue.at(newLocation(inputComponent, newIssue, message)).save();
  }

  private static NewIssueLocation newLocation(InputComponent input, NewIssue issue, String message) {
    return issue.newLocation()
      .on(input).message(message);
  }

}
