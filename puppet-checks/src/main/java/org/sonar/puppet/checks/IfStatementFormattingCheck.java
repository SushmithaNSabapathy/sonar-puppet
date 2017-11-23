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
package org.sonar.puppet.checks;

import com.google.common.base.Preconditions;
import com.sonar.sslr.api.AstNode;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.puppet.PuppetCheckVisitor;
import org.sonar.puppet.PuppetGrammar;
import org.sonar.puppet.PuppetPunctuator;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;

@Rule(
  key = "IfStatementFormatting",
  priority = Priority.MINOR,
  name = "All \"if\" statements should be formatted the same way",
  tags = {Tags.CONVENTION})
@ActivatedByDefault
@SqaleConstantRemediation("2min")
public class IfStatementFormattingCheck extends PuppetCheckVisitor {

  @Override
  public void init() {
    subscribeTo(PuppetGrammar.IF_STMT, PuppetGrammar.ELSIF_STMT, PuppetGrammar.ELSE_STMT);
  }

  @Override
  public void visitNode(AstNode node) {
    checkOpeningCurlyBraceLastTokenOnTheLine(node.getFirstChild(PuppetPunctuator.LBRACE));
    checkClosingCurlyBraceOnlyTokenOnTheLine(node.getFirstChild(PuppetPunctuator.RBRACE));
    checkElsifOnSameLineAsClosingCurlyBrace(node);
    checkElseOnSameLineAsClosingCurlyBrace(node);
  }

  private void checkOpeningCurlyBraceLastTokenOnTheLine(AstNode node) {
    if (isOnSameLine(node, node.getNextAstNode())) {
      addIssue(node, this, "Move the code following the opening curly brace to the next line.");
    }
    if (!isOnSameLine(node, node.getPreviousAstNode().getLastChild() != null ? node.getPreviousAstNode().getLastChild() : node.getPreviousAstNode())) {
      addIssue(node, this, "Move the opening curly brace to the previous line.");
    }
  }

  private void checkClosingCurlyBraceOnlyTokenOnTheLine(AstNode node) {
    if (isOnSameLine(node, node.getPreviousAstNode())) {
      addIssue(node, this, "Move the closing curly brace to the next line.");
    }
  }

  private void checkElsifOnSameLineAsClosingCurlyBrace(AstNode node) {
    if (node.is(PuppetGrammar.ELSIF_STMT)) {
      AstNode previousClosingBrace = node.getPreviousSibling().is(PuppetGrammar.ELSIF_STMT) ? node.getPreviousSibling().getFirstChild(PuppetPunctuator.RBRACE) : node
        .getPreviousSibling();
      if (!isOnSameLine(node, previousClosingBrace)) {
        addIssue(node, this, "Move the \"elsif\" statement next to the previous closing curly brace.");
      }
    }
  }

  private void checkElseOnSameLineAsClosingCurlyBrace(AstNode node) {
    if (node.is(PuppetGrammar.ELSE_STMT)) {
      AstNode previousClosingBrace = node.getParent().getLastChild(PuppetGrammar.ELSIF_STMT) != null ? node.getParent().getLastChild(PuppetGrammar.ELSIF_STMT)
        .getFirstChild(PuppetPunctuator.RBRACE) : node.getParent().getFirstChild(PuppetPunctuator.RBRACE);
      if (!isOnSameLine(node, previousClosingBrace)) {
        addIssue(node, this, "Move the \"else\" statement next to the previous closing curly brace.");
      }
    }
  }

  private static boolean isOnSameLine(AstNode... nodes) {
    Preconditions.checkArgument(nodes.length > 1);
    int lineRef = nodes[0].getTokenLine();
    for (AstNode node : nodes) {
      if (node.getTokenLine() != lineRef) {
        return false;
      }
    }
    return true;
  }

}
