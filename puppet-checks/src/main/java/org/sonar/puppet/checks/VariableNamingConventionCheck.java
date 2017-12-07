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
import org.sonar.puppet.PuppetTokenType;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;

@Rule(
  key = "VariableNamingConvention",
  name = "Variables should follow a naming convention",
  priority = Priority.MAJOR,
  tags = {Tags.CONVENTION, Tags.FUTURE_PARSER})
@SqaleConstantRemediation("5min")
@ActivatedByDefault
public class VariableNamingConventionCheck extends PuppetCheckVisitor {

  private static final String FORMAT = "^\\$(::)?([a-z][a-z0-9_]*::)*[a-z_][a-z0-9_]*$";

  @Override
  public void init() {
    subscribeTo(PuppetTokenType.VARIABLE);
  }

  @Override
  public void leaveNode(AstNode node) {
    if (!node.getTokenValue().matches(FORMAT)) {
      addIssue(node, this, "Rename variable \"" + node.getTokenValue().substring(1) + "\" to match the regular expression: ^(::)?([a-z][a-z0-9_]*::)*[a-z_][a-z0-9_]*$");
    }
  }

}
