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
package org.sonar.puppet.visitors;

import com.google.common.collect.Lists;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.highlighting.NewHighlighting;
import org.sonar.api.batch.sensor.highlighting.TypeOfText;
import org.sonar.puppet.tree.*;
import org.sonar.plugins.puppet.api.visitors.SubscriptionVisitor;
import org.sonar.puppet.tree.impl.InternalSyntaxToken;

import java.util.ArrayList;
import java.util.List;

public class SyntaxHighlighterVisitor extends SubscriptionVisitor {

  private final SensorContext sensorContext;
  private final FileSystem fileSystem;
  private NewHighlighting highlighting;

  public SyntaxHighlighterVisitor(SensorContext sensorContext) {
    this.sensorContext = sensorContext;
    fileSystem = sensorContext.fileSystem();
  }

  @Override
  public List<Tree.Kind> nodesToVisit() {
    return Lists.newArrayList(
      Tree.Kind.STRING,
      // FIXME: Tree.Kind.KEYWORD,
      Tree.Kind.VARIABLE,
      Tree.Kind.TOKEN);
  }

  @Override
  public void visitFile(Tree tree) {
    highlighting = sensorContext.newHighlighting().onFile(fileSystem.inputFile(fileSystem.predicates().is(getContext().getFile())));
  }

  @Override
  public void leaveFile(Tree scriptTree) {
    highlighting.save();
  }

  @Override
  public void visitNode(Tree tree) {
    List<SyntaxToken> tokens = new ArrayList<>();
    TypeOfText code = null;

    if (tree.is(Tree.Kind.STRING)) {
      tokens.add(((StringTree) tree).value());
      code = TypeOfText.STRING;

      // FIXME
//    } else if (tree.is(Tree.Kind.KEYWORD)) {
//      tokens.add(((PropertyTree) tree).property().value());
//      code = TypeOfText.KEYWORD;

    } else if (tree.is(Tree.Kind.VARIABLE)) {
      tokens.add(((VariableTree) tree).value());
      code = TypeOfText.KEYWORD_LIGHT;

    } else if (tree.is(Tree.Kind.TOKEN)) {
      highlightComments((InternalSyntaxToken) tree);
    }

    for (SyntaxToken token : tokens) {
      highlight(token, code);
    }
  }

  protected void highlight(SyntaxToken token, TypeOfText type) {
    highlighting.highlight(token.line(), token.column(), token.endLine(), token.endColumn(), type);
  }

  private void highlightComments(InternalSyntaxToken token) {
    for (SyntaxTrivia trivia : token.trivias()) {
      highlight(trivia, TypeOfText.COMMENT);
    }
  }

}