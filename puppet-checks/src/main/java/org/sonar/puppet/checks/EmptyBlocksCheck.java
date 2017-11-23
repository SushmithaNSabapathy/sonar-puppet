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
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;

@Rule(
  key = "EmptyBlocks",
  name = "Empty blocks of code should be removed",
  priority = Priority.MAJOR,
  tags = {Tags.PITFALL})
@SqaleConstantRemediation("5min")
@ActivatedByDefault
public class EmptyBlocksCheck extends PuppetCheckVisitor {

  @Override
  public void init() {
    subscribeTo(
      PuppetGrammar.CLASSDEF,
      PuppetGrammar.DEFINITION,

      PuppetGrammar.RESOURCE,
      PuppetGrammar.RESOURCE_OVERRIDE,

      PuppetGrammar.CASE_MATCHER,
      PuppetGrammar.IF_STMT,
      PuppetGrammar.ELSIF_STMT,
      PuppetGrammar.ELSE_STMT,
      PuppetGrammar.UNLESS_STMT);
  }

  @Override
  public void visitNode(AstNode node) {
    checkClassesAndDefines(node);
    checkResources(node);
    checkConditionalStatements(node);
  }

  private void checkClassesAndDefines(AstNode node) {
    if (node.is(PuppetGrammar.CLASSDEF, PuppetGrammar.DEFINITION)) {
      if (node.getFirstChild(PuppetGrammar.ARGUMENTS) == null
        && node.getFirstChild(PuppetGrammar.CLASSNAME).getNextAstNode().is(PuppetPunctuator.LPAREN)) {
        addIssue(node, this, "Remove this empty argument list.");
      }
      if (node.getFirstChild(PuppetGrammar.STATEMENT) == null) {
        String nodeType = node.is(PuppetGrammar.CLASSDEF) ? "class" : "define";
        addIssue(node, this, "Remove this empty " + nodeType + ".");
      }
    }
  }

  private void checkResources(AstNode node) {
    if (node.is(PuppetGrammar.RESOURCE)
      && node.getFirstChild(PuppetGrammar.RESOURCE_INST) == null
      && node.getFirstChild(PuppetGrammar.PARAMS).getChildren(PuppetGrammar.PARAM).isEmpty()) {
      addIssue(node, this, "Remove this empty resource default statement.");
    } else if (node.is(PuppetGrammar.RESOURCE_OVERRIDE)
      && node.getFirstChild(PuppetGrammar.ANY_PARAMS).getChildren(PuppetGrammar.PARAM, PuppetGrammar.ADD_PARAM).isEmpty()) {
      addIssue(node, this, "Remove this empty resource override.");
    }
  }

  private void checkConditionalStatements(AstNode node) {
    if (node.getFirstChild(PuppetGrammar.STATEMENT) == null) {
      if (node.is(PuppetGrammar.IF_STMT)) {
        addIssue(node, this, "Remove this empty \"if\" statement.");
      } else if (node.is(PuppetGrammar.UNLESS_STMT)) {
        addIssue(node, this, "Remove this empty \"unless\" statement.");
      } else if (!hasTrivia(node)) {
        if (node.is(PuppetGrammar.CASE_MATCHER)) {
          addIssue(node, this, "Remove this empty \"case\" matcher or add a comment to explain why it is empty.");
        } else if (node.is(PuppetGrammar.ELSIF_STMT)) {
          addIssue(node, this, "Remove this empty \"elsif\" statement or add a comment to explain why it is empty.");
        } else if (node.is(PuppetGrammar.ELSE_STMT)) {
          addIssue(node, this, "Remove this empty \"else\" statement or add a comment to explain why it is empty.");
        }
      }
    }
  }

  private static boolean hasTrivia(AstNode node) {
    if (node.getToken().hasTrivia()) {
      return true;
    }
    for (AstNode childNode : node.getChildren()) {
      if (childNode.getToken().hasTrivia()) {
        return true;
      }
    }
    return false;
  }

}
