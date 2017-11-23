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
import org.sonar.check.RuleProperty;
import org.sonar.puppet.PuppetCheckVisitor;
import org.sonar.puppet.PuppetTokenType;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Rule(
  key = "PuppetURLModules",
  name = "\"puppet:///\" URL path should start with \"modules/\"",
  priority = Priority.CRITICAL,
  tags = {Tags.PITFALL})
@SqaleConstantRemediation("10min")
@ActivatedByDefault
public class PuppetURLModulesCheck extends PuppetCheckVisitor {

  private static final String PREFIX = "puppet:///";

  @RuleProperty(key = "mountPoints", description = "Comma-separated list of additional mount points in complement of 'modules' and variable usage.", defaultValue = "")
  public String mountPoints = "";

  @Override
  public void init() {
    subscribeTo(PuppetTokenType.DOUBLE_QUOTED_STRING_LITERAL, PuppetTokenType.SINGLE_QUOTED_STRING_LITERAL);
  }

  @Override
  public void visitNode(AstNode node) {
    if (node.getTokenValue().substring(1).startsWith(PREFIX)) {
      List<String> validPaths = new ArrayList<>(Arrays.asList(PREFIX + "modules/", PREFIX + "$"));
      if (StringUtils.isNotBlank(mountPoints)) {
        for (String pathCustom : mountPoints.split(",")) {
          if (StringUtils.isNotBlank(pathCustom)) {
            validPaths.add(PREFIX + pathCustom + "/");
          }
        }
      }
      if (!StringUtils.startsWithAny(node.getTokenValue().substring(1), validPaths.toArray(new String[validPaths.size()]))) {
        String message = "Add \"modules/\" to the path";
        if (validPaths.size() > 2) {
          message += " (Or " + mountPoints + ")";
        }
        message += ".";
        addIssue(node, this, message);
      }
    }
  }

}
