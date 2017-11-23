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
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.sslr.ast.AstSelect;

import java.util.List;

@Rule(
  key = "S1066",
  priority = Priority.MAJOR,
  name = "Collapsible \"if\" statements should be merged",
  tags = {Tags.CONFUSING})
@ActivatedByDefault
@SqaleConstantRemediation("5min")
public class CollapsibleIfStatementsCheck extends PuppetCheckVisitor {

  @Override
  public void init() {
    subscribeTo(PuppetGrammar.IF_STMT, PuppetGrammar.ELSIF_STMT);
  }

  @Override
  public void visitNode(AstNode node) {
    AstNode singleIfChild = singleIfChild(node);
    if (singleIfChild != null && !hasElseOrElsif(singleIfChild)) {
      addIssue(singleIfChild, this, "Merge this \"if\" statement with the enclosing one.");
    }
  }

  private static boolean hasElseOrElsif(AstNode ifNode) {
    return ifNode.hasDirectChildren(PuppetGrammar.ELSIF_STMT, PuppetGrammar.ELSE_STMT);
  }

  private static AstNode singleIfChild(AstNode statement) {
    List<AstNode> statements = statement.getChildren(PuppetGrammar.STATEMENT);

    if (statements.size() == 1) {
      AstSelect nestedIf = statements.get(0).select()
        .children(PuppetGrammar.COMPOUND_STMT)
        .children(PuppetGrammar.IF_STMT);
      if (nestedIf.size() == 1) {
        return nestedIf.get(0);
      }
    }
    return null;
  }
}
