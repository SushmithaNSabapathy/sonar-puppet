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
import org.sonar.puppet.PuppetKeyword;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;

@Rule(
  key = "DocumentClassesAndDefines",
  name = "Classes and defines should be documented",
  priority = Priority.MINOR,
  tags = {Tags.CONVENTION})
@SqaleConstantRemediation("20min")
public class DocumentClassesAndDefinesCheck extends PuppetCheckVisitor {

  @Override
  public void init() {
    subscribeTo(PuppetGrammar.DEFINITION, PuppetGrammar.CLASSDEF);
  }

  @Override
  public void visitNode(AstNode node) {
    if (node.is(PuppetGrammar.DEFINITION) && !node.getFirstChild(PuppetKeyword.DEFINE).getToken().hasTrivia()) {
      addIssue(node, this, "Add some comments to this define.");
    } else if (node.is(PuppetGrammar.CLASSDEF) && !node.getFirstChild(PuppetKeyword.CLASS).getToken().hasTrivia()) {
      addIssue(node, this, "Add some comments to this class.");
    }
  }

}
