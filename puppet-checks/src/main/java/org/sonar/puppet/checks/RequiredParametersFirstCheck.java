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
  key = "RequiredParametersFirst",
  priority = Priority.MAJOR,
  name = "Class and define required parameters should be listed before optional parameters",
  tags = Tags.CONVENTION)
@ActivatedByDefault
@SqaleConstantRemediation("2min")
public class RequiredParametersFirstCheck extends PuppetCheckVisitor {

  @Override
  public void init() {
    subscribeTo(PuppetGrammar.CLASSDEF, PuppetGrammar.DEFINITION);
  }

  @Override
  public void visitNode(AstNode node) {
    if (node.getFirstChild(PuppetGrammar.ARGUMENTS) != null) {
      boolean foundOptionalParameter = false;
      for (AstNode argumentNode : node.getFirstChild(PuppetGrammar.ARGUMENTS).getChildren(PuppetGrammar.ARGUMENT)) {
        if (argumentNode.getFirstChild(PuppetPunctuator.EQUALS) != null) {
          foundOptionalParameter = true;
        } else if (foundOptionalParameter && argumentNode.getFirstChild(PuppetPunctuator.EQUALS) == null) {
          addIssue(node, this, "Move required parameters before optional parameters.");
          break;
        }
      }
    }
  }

}
