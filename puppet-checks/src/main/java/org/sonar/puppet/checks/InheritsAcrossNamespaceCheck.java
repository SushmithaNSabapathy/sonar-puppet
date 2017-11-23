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
import org.apache.commons.lang.StringUtils;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.puppet.PuppetCheckVisitor;
import org.sonar.puppet.PuppetGrammar;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;

@Rule(
  key = "InheritsAcrossNamespace",
  priority = Priority.MAJOR,
  name = "Classes should not inherit across namespaces",
  tags = {Tags.PITFALL})
@ActivatedByDefault
@SqaleConstantRemediation("4h")
public class InheritsAcrossNamespaceCheck extends PuppetCheckVisitor {

  @Override
  public void init() {
    subscribeTo(PuppetGrammar.CLASS_PARENT);
  }

  @Override
  public void visitNode(AstNode node) {
    String inheritedModuleName = StringUtils.substringBefore(node.getFirstChild(PuppetGrammar.CLASSNAME_OR_DEFAULT).getTokenValue(), "::");
    String classModuleName = StringUtils.substringBefore(node.getParent().getFirstChild(PuppetGrammar.CLASSNAME).getTokenValue(), "::");

    if (!inheritedModuleName.equals(classModuleName)) {
      addIssue(node, this, "Remove this inheritance from an external module class.");
    }
  }

}
