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
///*
// * SonarQube Puppet Analyzer
// * Copyright (C) 2017-2017 David RACODON
// * david.racodon@gmail.com
// *
// * This program is free software; you can redistribute it and/or
// * modify it under the terms of the GNU Lesser General Public
// * License as published by the Free Software Foundation; either
// * version 3 of the License, or (at your option) any later version.
// *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// * Lesser General Public License for more details.
// *
// * You should have received a copy of the GNU Lesser General Public License
// * along with this program; if not, write to the Free Software Foundation,
// * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
// */
//package org.sonar.puppet;
//
//import com.google.common.base.Charsets;
//import org.sonar.puppet.metrics.ComplexityVisitor;
//import org.sonar.puppet.metrics.FunctionVisitor;
//import org.sonar.puppet.parser.PuppetParser;
//import com.sonar.sslr.api.AstNode;
//import com.sonar.sslr.api.Grammar;
//import com.sonar.sslr.impl.Parser;
//
//import java.io.File;
//import java.util.Collection;
//
//import org.sonar.squidbridge.*;
//import org.sonar.squidbridge.api.SourceClass;
//import org.sonar.squidbridge.api.SourceCode;
//import org.sonar.squidbridge.api.SourceFile;
//import org.sonar.squidbridge.api.SourceProject;
//import org.sonar.squidbridge.indexer.QueryByType;
//import org.sonar.squidbridge.metrics.CommentsVisitor;
//import org.sonar.squidbridge.metrics.CounterVisitor;
//import org.sonar.squidbridge.metrics.LinesVisitor;
//
//public class PuppetAstScanner {
//
//  private PuppetAstScanner() {
//  }
//
//  /*
//   * Helper method for testing checks without having to deploy them on a Sonar instance.
//   */
//  public static SourceFile scanSingleFile(File file, SquidAstVisitor<Grammar>... visitors) {
//    if (!file.isFile()) {
//      throw new IllegalArgumentException("File '" + file + "' not found.");
//    }
//    AstScanner<Grammar> scanner = create(new PuppetConfiguration(Charsets.UTF_8), visitors);
//    scanner.scanFile(file);
//    Collection<SourceCode> sources = scanner.getIndex().search(new QueryByType(SourceFile.class));
//    if (sources.size() != 1) {
//      throw new IllegalStateException("Only one SourceFile was expected whereas " + sources.size() + " has been returned.");
//    }
//    return (SourceFile) sources.iterator().next();
//  }
//
//  public static AstScanner<Grammar> create(PuppetConfiguration conf, SquidAstVisitor<Grammar>... visitors) {
//    final SquidAstVisitorContextImpl<Grammar> context = new SquidAstVisitorContextImpl<>(new SourceProject("Puppet Project"));
//    final Parser<Grammar> parser = PuppetParser.create(conf);
//
//    AstScanner.Builder<Grammar> builder = AstScanner.<Grammar>builder(context).setBaseParser(parser);
//
//    builder.withMetrics(PuppetMetric.values());
//    builder.setFilesMetric(PuppetMetric.FILES);
//    builder.setCommentAnalyser(new PuppetCommentAnalyser());
//
//    builder.withSquidAstVisitor(new LinesVisitor<Grammar>(PuppetMetric.LINES));
//    builder.withSquidAstVisitor(new PuppetLinesOfCodeVisitor<Grammar>(PuppetMetric.LINES_OF_CODE));
//    builder.withSquidAstVisitor(new FunctionVisitor());
//    builder.withSquidAstVisitor(new ComplexityVisitor());
//
//    builder.withSquidAstVisitor(CommentsVisitor.<Grammar>builder().withCommentMetric(PuppetMetric.COMMENT_LINES)
//      .withNoSonar(true)
//      .withIgnoreHeaderComment(conf.getIgnoreHeaderComments())
//      .build());
//
//    builder.withSquidAstVisitor(new SourceCodeBuilderVisitor<Grammar>(
//      new ClassSourceCodeBuilderCallback(),
//      PuppetGrammar.CLASSDEF,
//      PuppetGrammar.DEFINITION));
//
//    builder.withSquidAstVisitor(CounterVisitor.<Grammar>builder()
//      .setMetricDef(PuppetMetric.CLASSES)
//      .subscribeTo(PuppetGrammar.CLASSDEF, PuppetGrammar.DEFINITION)
//      .build());
//
//    // TODO: To be discussed with the mapping of FUNCTIONS
//    builder.withSquidAstVisitor(CounterVisitor.<Grammar>builder()
//      .setMetricDef(PuppetMetric.RESOURCES)
//      .subscribeTo(PuppetGrammar.RESOURCE)
//      .build());
//
//    builder.withSquidAstVisitor(CounterVisitor.<Grammar>builder()
//      .setMetricDef(PuppetMetric.STATEMENTS)
//      .subscribeTo(PuppetGrammar.STATEMENT)
//      .build());
//
//    for (SquidAstVisitor<Grammar> visitor : visitors) {
//      if (visitor instanceof CharsetAwareVisitor) {
//        ((CharsetAwareVisitor) visitor).setCharset(conf.getCharset());
//      }
//      builder.withSquidAstVisitor(visitor);
//    }
//
//    return builder.build();
//  }
//
//  public static class ClassSourceCodeBuilderCallback implements SourceCodeBuilderCallback {
//    private int seq = 0;
//
//    @Override
//    public SourceCode createSourceCode(SourceCode parentSourceCode, AstNode astNode) {
//      seq++;
//      SourceClass cls = new SourceClass("class:" + seq);
//      cls.setStartAtLine(astNode.getTokenLine());
//      return cls;
//    }
//  }
//
//}
