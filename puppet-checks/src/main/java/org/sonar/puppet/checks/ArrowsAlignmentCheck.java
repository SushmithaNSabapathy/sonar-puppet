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

import com.sonar.sslr.api.AstNode;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.puppet.PuppetCheckVisitor;
import org.sonar.puppet.PuppetGrammar;
import org.sonar.puppet.PuppetPunctuator;
import org.sonar.puppet.PuppetTokenType;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;

@Rule(
  key = "ArrowsAlignment",
  priority = Priority.MINOR,
  name = "All arrows in attribute/value list should be aligned",
  tags = Tags.CONVENTION)
@ActivatedByDefault
@SqaleConstantRemediation("2min")
public class ArrowsAlignmentCheck extends PuppetCheckVisitor {

  private static final String MESSAGE = "Properly align arrows (arrows are not all placed at the same column).";
  private static final String MESSAGE_SPACE = "Properly align arrows (arrows are not all placed one space ahead of the longest attribute).";

  @Override
  public void init() {
    subscribeTo(PuppetGrammar.HASH_PAIRS, PuppetGrammar.PARAMS, PuppetGrammar.ANY_PARAMS, PuppetGrammar.SINTVALUES);
  }

  @Override
  public void visitNode(AstNode node) {
    if (node.is(PuppetGrammar.ANY_PARAMS)) {
      checkAllArrowsAlignment(node);
    } else if (node.is(PuppetGrammar.HASH_PAIRS)) {
      checkHashRocketsAlignment(node, PuppetGrammar.HASH_PAIR, PuppetGrammar.KEY);
    } else if (node.is(PuppetGrammar.PARAMS)) {
      checkHashRocketsAlignment(node, PuppetGrammar.PARAM, PuppetGrammar.PARAM_NAME);
    } else if (node.is(PuppetGrammar.SINTVALUES)) {
      checkHashRocketsAlignment(node, PuppetGrammar.SELECTVAL, PuppetGrammar.SELECTLHAND);
    }
  }

  private void checkHashRocketsAlignment(AstNode node, PuppetGrammar nodeTypeContainingArrow, PuppetGrammar nodeTypeBeforeArrow) {
    int arrowColumn = -1;
    for (AstNode nodeContainingArrow : node.getChildren(nodeTypeContainingArrow)) {
      if (arrowColumn == -1) {
        arrowColumn = nodeContainingArrow.getFirstChild(PuppetPunctuator.FARROW).getToken().getColumn();
      } else if (nodeContainingArrow.getFirstChild(PuppetPunctuator.FARROW).getToken().getColumn() != arrowColumn) {
        addIssue(node, this, MESSAGE);
        return;
      }
    }
    if (arrowColumn != -1) {
      checkHashRocketsAsLeftAsPossible(node, nodeTypeContainingArrow, nodeTypeBeforeArrow, arrowColumn);
    }
  }

  private void checkHashRocketsAsLeftAsPossible(AstNode node, PuppetGrammar nodeTypeContainingArrow, PuppetGrammar nodeTypeBeforeArrow, int arrowColumn) {
    int upperColumn = -1;
    int currentColumn;
    for (AstNode nodeContainingArrow : node.getChildren(nodeTypeContainingArrow)) {
      currentColumn = nodeContainingArrow.getFirstChild(nodeTypeBeforeArrow).getLastToken().getColumn()
        + nodeContainingArrow.getFirstChild(nodeTypeBeforeArrow).getTokenValue().length();
      if (currentColumn > upperColumn) {
        upperColumn = currentColumn;
      }
    }
    if (upperColumn != arrowColumn - 1) {
      addIssue(node, this, MESSAGE_SPACE);
    }
  }

  private void checkAllArrowsAlignment(AstNode node) {
    int arrowColumn = -1;
    for (AstNode paramNode : node.getChildren(PuppetGrammar.PARAM)) {
      if (arrowColumn == -1) {
        arrowColumn = paramNode.getFirstChild(PuppetPunctuator.FARROW).getToken().getColumn();
      } else if (paramNode.getFirstChild(PuppetPunctuator.FARROW).getToken().getColumn() != arrowColumn) {
        addIssue(node, this, MESSAGE);
        return;
      }
    }
    for (AstNode anyParamNode : node.getChildren(PuppetGrammar.ADD_PARAM)) {
      if (arrowColumn == -1) {
        arrowColumn = anyParamNode.getFirstChild(PuppetPunctuator.PARROW).getToken().getColumn();
      } else if (anyParamNode.getFirstChild(PuppetPunctuator.PARROW).getToken().getColumn() != arrowColumn) {
        addIssue(node, this, MESSAGE);
        return;
      }
    }

    if (arrowColumn != -1) {
      int upperColumn = -1;
      int currentColumn;
      for (AstNode paramNode : node.getChildren(PuppetGrammar.PARAM)) {
        currentColumn = paramNode.getFirstChild(PuppetGrammar.PARAM_NAME).getLastToken().getColumn() + paramNode.getFirstChild(PuppetGrammar.PARAM_NAME).getTokenValue().length();
        if (currentColumn > upperColumn) {
          upperColumn = currentColumn;
        }
      }
      for (AstNode paramNode : node.getChildren(PuppetGrammar.ADD_PARAM)) {
        currentColumn = paramNode.getFirstChild(PuppetTokenType.NAME).getLastToken().getColumn() + paramNode.getFirstChild(PuppetTokenType.NAME).getTokenValue().length();
        if (currentColumn > upperColumn) {
          upperColumn = currentColumn;
        }
      }
      if (upperColumn + 1 != arrowColumn) {
        addIssue(node, this, MESSAGE_SPACE);
      }
    }
  }

}
