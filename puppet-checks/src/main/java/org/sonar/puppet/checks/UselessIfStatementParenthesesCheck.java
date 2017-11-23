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
  key = "UselessIfStatementParentheses",
  name = "Useless parentheses surrounding \"if/elsif\" statement condition should be removed",
  priority = Priority.MINOR,
  tags = {Tags.CONVENTION})
@SqaleConstantRemediation("2min")
@ActivatedByDefault
public class UselessIfStatementParenthesesCheck extends PuppetCheckVisitor {

  @Override
  public void init() {
    subscribeTo(PuppetGrammar.IF_STMT, PuppetGrammar.ELSIF_STMT);
  }

  @Override
  public void visitNode(AstNode node) {
    if (node.getFirstChild(PuppetGrammar.EXPRESSION).getChildren(PuppetGrammar.UNARY_NOT_EXPRESSION).size() == 1
      && node.getFirstChild(PuppetGrammar.EXPRESSION).getFirstChild(PuppetGrammar.UNARY_NOT_EXPRESSION).getFirstChild(PuppetPunctuator.LPAREN) != null
      && node.getFirstChild(PuppetGrammar.EXPRESSION).getFirstChild(PuppetGrammar.UNARY_NOT_EXPRESSION).getFirstChild(PuppetPunctuator.NOT) == null) {
      addIssue(
        node.getFirstChild(PuppetGrammar.EXPRESSION).getFirstChild(PuppetGrammar.UNARY_NOT_EXPRESSION),
        this,
        "Remove the useless parentheses surrounding the condition.");
    }
  }

}
