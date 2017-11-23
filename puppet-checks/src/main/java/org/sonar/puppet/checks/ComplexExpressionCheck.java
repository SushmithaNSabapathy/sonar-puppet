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

import com.google.common.annotations.VisibleForTesting;
import com.sonar.sslr.api.AstNode;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.puppet.PuppetCheckVisitor;
import org.sonar.puppet.PuppetGrammar;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleLinearWithOffsetRemediation;

import java.text.MessageFormat;

@Rule(
  key = "ComplexExpression",
  priority = Priority.MAJOR,
  name = "Expressions should not be too complex",
  tags = {Tags.CONFUSING})
@ActivatedByDefault
@SqaleLinearWithOffsetRemediation(coeff = "2min", offset = "5min", effortToFixDescription = "number of boolean operators beyond the limit")
public class ComplexExpressionCheck extends PuppetCheckVisitor {

  private static final int MAX_NUMBER_OF_BOOLEAN_OPERATORS = 3;

  @RuleProperty(
    key = "max",
    defaultValue = "" + MAX_NUMBER_OF_BOOLEAN_OPERATORS,
    description = "Maximum number of boolean operators")
  private int max = MAX_NUMBER_OF_BOOLEAN_OPERATORS;

  @Override
  public void init() {
    subscribeTo(PuppetGrammar.EXPRESSION);
  }

  @Override
  public void visitNode(AstNode node) {
    if (node.getDescendants(PuppetGrammar.BOOL_OPERATOR).size() > max) {
      addIssue(node.getFirstDescendant(PuppetGrammar.BOOL_OPERATOR),
        this,
        MessageFormat.format(
          "Reduce the number of boolean operators. This condition contains {0,number,integer} boolean operators, {1,number,integer} more than the {2,number,integer} maximum.",
          node.getDescendants(PuppetGrammar.BOOL_OPERATOR).size(),
          node.getDescendants(PuppetGrammar.BOOL_OPERATOR).size() - max,
          max),
        (double) node.getDescendants(PuppetGrammar.BOOL_OPERATOR).size() - max);

    }
  }

  @VisibleForTesting
  public void setMax(int max) {
    this.max = max;
  }

}
