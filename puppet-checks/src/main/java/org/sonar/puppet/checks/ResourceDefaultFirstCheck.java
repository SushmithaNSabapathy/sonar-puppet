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

import java.util.ArrayList;
import java.util.List;

@Rule(
  key = "ResourceDefaultFirst",
  name = "Resource defaults should be defined before the first resource declaration",
  priority = Priority.MAJOR,
  tags = Tags.CONFUSING)
@SqaleConstantRemediation("2min")
@ActivatedByDefault
public class ResourceDefaultFirstCheck extends PuppetCheckVisitor {

  @Override
  public void init() {
    subscribeTo(PuppetGrammar.FILE_INPUT);
  }

  @Override
  public void visitNode(AstNode node) {
    int lineResourceInstance = getFirstResourceInstanceLine(node);
    if (lineResourceInstance == -1) {
      return;
    }
    for (AstNode resourceNode : getResourceDefaultNodes(node)) {
      if (resourceNode.getTokenLine() > lineResourceInstance) {
        addIssue(resourceNode, this, "Move this resource default before the first resource declaration.");
      }
    }
  }

  private static List<AstNode> getResourceDefaultNodes(AstNode node) {
    List<AstNode> resourceDefaults = new ArrayList();
    for (AstNode resourceNode : node.getDescendants(PuppetGrammar.RESOURCE)) {
      if (resourceNode.getFirstChild(PuppetGrammar.RESOURCE_INST) == null) {
        resourceDefaults.add(resourceNode);
      }
    }
    return resourceDefaults;
  }

  private static int getFirstResourceInstanceLine(AstNode node) {
    return !node.getDescendants(PuppetGrammar.RESOURCE_INST).isEmpty() ? node.getDescendants(PuppetGrammar.RESOURCE_INST).get(0).getTokenLine() : -1;
  }

}
