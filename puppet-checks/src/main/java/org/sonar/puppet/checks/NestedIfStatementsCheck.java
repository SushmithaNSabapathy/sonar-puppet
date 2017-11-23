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
import org.sonar.check.RuleProperty;
import org.sonar.puppet.PuppetCheckVisitor;
import org.sonar.puppet.PuppetGrammar;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;

@Rule(
  key = "NestedIfStatements",
  name = "Control flow statements \"if\" and \"elsif\" statements should not be nested too deeply",
  priority = Priority.MAJOR,
  tags = Tags.BRAIN_OVERLOAD)
@SqaleConstantRemediation("10min")
@ActivatedByDefault
public class NestedIfStatementsCheck extends PuppetCheckVisitor {

  private static final int DEFAULT_MAX = 3;

  @RuleProperty(
    key = "maximumNestingLevel",
    defaultValue = "" + DEFAULT_MAX)
  public int maximumNestingLevel = DEFAULT_MAX;

  private int depth;

  @Override
  public void init() {
    subscribeTo(
      PuppetGrammar.IF_STMT, PuppetGrammar.ELSIF_STMT);
  }

  @Override
  public void visitFile(AstNode astNode) {
    depth = 0;
  }

  @Override
  public void visitNode(AstNode node) {
    depth++;
    if (depth == maximumNestingLevel + 1) {
      String message = "Refactor this code to not nest more than %d \"if\" or \"elsif\" statements.";
      addIssue(node, this, String.format(message, maximumNestingLevel));
    }
  }

  @Override
  public void leaveNode(AstNode astNode) {
    depth--;
  }
}
