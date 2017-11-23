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
import org.sonar.puppet.PuppetPunctuator;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;

@Rule(
  key = "RightToLeftChainingArrows",
  name = "Right-to-left chaining arrows should not be used",
  priority = Priority.MAJOR,
  tags = {Tags.CONVENTION, Tags.CONFUSING})
@ActivatedByDefault
@SqaleConstantRemediation("10min")
public class RightToLeftChainingArrowsCheck extends PuppetCheckVisitor {

  @Override
  public void init() {
    subscribeTo(PuppetPunctuator.OUT_EDGE, PuppetPunctuator.OUT_EDGE_SUB);
  }

  @Override
  public void visitNode(AstNode node) {
    addIssue(node, this, "Rework the code to use left-to-right chaining arrows instead of right-to-left chaining arrows.");
  }

}
