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
  key = "ResourceDefaultNamingConvention",
  name = "Resource default statements should follow a naming convention",
  priority = Priority.CRITICAL,
  tags = {Tags.PITFALL})
@SqaleConstantRemediation("20min")
@ActivatedByDefault
public class ResourceDefaultNamingConventionCheck extends PuppetCheckVisitor {

  private static final String FORMAT = "^([A-Z][a-z0-9_]*::)*[A-Z][a-z0-9_]*$";

  @Override
  public void init() {
    subscribeTo(PuppetGrammar.RESOURCE);
  }

  @Override
  public void leaveNode(AstNode node) {
    if (node.getFirstChild(PuppetGrammar.RESOURCE_INST) == null && !node.getTokenValue().matches(FORMAT)) {
      addIssue(node, this, "Rename resource default \"" + node.getTokenValue() + "\" to match the regular expression: " + FORMAT);
    }
  }

}
