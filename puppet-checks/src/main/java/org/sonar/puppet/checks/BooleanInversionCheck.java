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

import com.google.common.collect.ImmutableMap;
import com.sonar.sslr.api.AstNode;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.puppet.PuppetCheckVisitor;
import org.sonar.puppet.PuppetGrammar;
import org.sonar.puppet.PuppetPunctuator;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;

import java.util.Map;

@Rule(
  key = "S1940",
  name = "Boolean checks should not be inverted",
  priority = Priority.MAJOR,
  tags = {Tags.PITFALL})
@SqaleConstantRemediation("2min")
@ActivatedByDefault
public class BooleanInversionCheck extends PuppetCheckVisitor {

  private static final Map<String, String> OPERATORS = ImmutableMap.<String, String>builder()
    .put("==", "!=")
    .put("!=", "==")
    .put("<", ">=")
    .put(">", "<=")
    .put("<=", ">")
    .put(">=", "<")
    .build();

  @Override
  public void init() {
    subscribeTo(PuppetGrammar.UNARY_NOT_EXPRESSION);
  }

  @Override
  public void visitNode(AstNode node) {
    if (node.getFirstChild().getType() == PuppetPunctuator.NOT) {
      if (node.getFirstChild(PuppetGrammar.COMPARISON) != null) {
        AstNode expression = node.getFirstChild(PuppetGrammar.COMPARISON);
        String val = expression.getFirstChild(PuppetGrammar.COMP_OPERATOR).getTokenValue();
        addIssue(expression, this, "Use the opposite operator (\"" + OPERATORS.get(val) + "\") instead.");
      } else if (node.getFirstChild(PuppetGrammar.BOOL_EXPRESSION) != null) {
        addIssue(node.getFirstChild(PuppetGrammar.BOOL_EXPRESSION), this, "Invert all the operators of this boolean expression instead.");
      }
    }
  }
}
