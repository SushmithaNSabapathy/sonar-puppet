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

@Rule(
  key = "NestedClassesOrDefines",
  priority = Priority.CRITICAL,
  name = "Classes and defines should not be defined within other classes or defines",
  tags = Tags.PITFALL)
@ActivatedByDefault
@SqaleConstantRemediation("1h")
public class NestedClassesOrDefinesCheck extends PuppetCheckVisitor {

  private String message;

  @Override
  public void init() {
    subscribeTo(PuppetGrammar.CLASSDEF, PuppetGrammar.DEFINITION);
  }

  @Override
  public void visitNode(AstNode node) {
    for (AstNode nestedNode : node.getDescendants(PuppetGrammar.CLASSDEF, PuppetGrammar.DEFINITION)) {
      message = "Move this nested "
        + (nestedNode.is(PuppetGrammar.CLASSDEF) ? "class " : "define ")
        + "\"" + nestedNode.getFirstChild(PuppetGrammar.CLASSNAME).getTokenValue() + "\""
        + " outside of "
        + (node.is(PuppetGrammar.CLASSDEF) ? "class " : "define ")
        + "\"" + node.getFirstChild(PuppetGrammar.CLASSNAME).getTokenValue() + "\""
        + ".";
      addIssue(nestedNode, this, message);
    }
  }

}
