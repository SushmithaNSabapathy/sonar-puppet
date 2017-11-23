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
  key = "TrailingCommas",
  priority = Priority.MINOR,
  name = "A trailing comma should be added after each resource attribute, parameter definition, hash pair and selector case",
  tags = Tags.CONVENTION)
@ActivatedByDefault
@SqaleConstantRemediation("2min")
public class TrailingCommasCheck extends PuppetCheckVisitor {

  @Override
  public void init() {
    subscribeTo(
      PuppetGrammar.HASH_PAIRS,
      PuppetGrammar.PARAMS,
      PuppetGrammar.ANY_PARAMS,
      PuppetGrammar.SINTVALUES,
      PuppetGrammar.ARGUMENTS);
  }

  @Override
  public void visitNode(AstNode node) {
    checkTrailingCommas(node, PuppetGrammar.PARAMS, PuppetGrammar.PARAM);
    checkTrailingCommas(node, PuppetGrammar.ANY_PARAMS, PuppetGrammar.PARAM, PuppetGrammar.ADD_PARAM);
    checkTrailingCommas(node, PuppetGrammar.HASH_PAIRS, PuppetGrammar.HASH_PAIR);
    checkTrailingCommas(node, PuppetGrammar.SINTVALUES, PuppetGrammar.SELECTVAL);
    checkTrailingCommas(node, PuppetGrammar.ARGUMENTS, PuppetGrammar.ARGUMENT);
  }

  private void checkTrailingCommas(AstNode node, PuppetGrammar parentType, PuppetGrammar... childType) {
    if (hasNotAllTrailingCommas(node, parentType, childType)) {
      if (node.is(PuppetGrammar.PARAMS, PuppetGrammar.PARAMS)) {
        if (hasNotTrailingSemiColon(node)) {
          createIssue(node, childType);
        }
      } else {
        createIssue(node, childType);
      }
    }
  }

  private boolean hasNotAllTrailingCommas(AstNode node, PuppetGrammar parentType, PuppetGrammar... childType) {
    return node.is(parentType) && node.getChildren(childType).size() != node.getChildren(PuppetPunctuator.COMMA).size();
  }

  private boolean hasNotTrailingSemiColon(AstNode node) {
    return node.getNextAstNode() == null || !node.getNextAstNode().is(PuppetPunctuator.SEMIC);
  }

  private void createIssue(AstNode node, PuppetGrammar... childType) {
    addIssue(node.getChildren(childType).get(node.getChildren(childType).size() - 1), this, "Add the missing trailing comma.");
  }

}
