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
import org.sonar.puppet.PuppetTokenType;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;

@Rule(
  key = "UnquotedResourceTitle",
  priority = Priority.MINOR,
  name = "Resource titles should be quoted",
  tags = Tags.CONVENTION)
@ActivatedByDefault
@SqaleConstantRemediation("2min")
public class UnquotedResourceTitleCheck extends PuppetCheckVisitor {

  @Override
  public void init() {
    subscribeTo(PuppetGrammar.RESOURCE_NAME);
  }

  @Override
  public void visitNode(AstNode node) {
    if (node.getFirstChild(PuppetTokenType.NAME) != null) {
      addIssue(node, this, "Quote this resource title.");
    } else if (node.getFirstChild(PuppetGrammar.ARRAY) != null) {
      boolean hasUnquotedTitle = false;
      for (AstNode expressionNode : node.getFirstChild(PuppetGrammar.ARRAY).getChildren(PuppetGrammar.EXPRESSION)) {
        if (expressionNode.getFirstChild(PuppetTokenType.NAME) != null || expressionNode.getFirstChild(PuppetTokenType.VARIABLE) != null) {
          hasUnquotedTitle = true;
          break;
        }
      }
      if (hasUnquotedTitle) {
        addIssue(node, this, "Quote each resource title of this array.");
      }
    }
  }

}
