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

import com.google.common.annotations.VisibleForTesting;
import com.sonar.sslr.api.AstNode;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.puppet.PuppetCheckVisitor;
import org.sonar.puppet.PuppetGrammar;
import org.sonar.puppet.PuppetMetric;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleLinearWithOffsetRemediation;
import org.sonar.squidbridge.api.SourceClass;
import org.sonar.squidbridge.checks.ChecksHelper;

@Rule(
  key = "TooComplexClassesAndDefines",
  priority = Priority.MAJOR,
  name = "Classes and defines should not be too complex",
  tags = Tags.BRAIN_OVERLOAD)
@ActivatedByDefault
@SqaleLinearWithOffsetRemediation(coeff = "5min", offset = "30min", effortToFixDescription = "per complexity point above the threshold")
public class TooComplexClassesAndDefinesCheck extends PuppetCheckVisitor {

  public static final int DEFAULT_MAX_COMPLEXITY = 50;

  @RuleProperty(
    key = "max",
    defaultValue = "" + DEFAULT_MAX_COMPLEXITY,
    description = "Maximum points of complexity")
  private int max = DEFAULT_MAX_COMPLEXITY;

  @Override
  public void init() {
    subscribeTo(PuppetGrammar.CLASSDEF, PuppetGrammar.DEFINITION);
  }

  @Override
  public void leaveNode(AstNode node) {
    SourceClass sourceClass = (SourceClass) getContext().peekSourceCode();
    int complexity = ChecksHelper.getRecursiveMeasureInt(sourceClass, PuppetMetric.COMPLEXITY);

    if (complexity > max) {
      String nodeType = node.is(PuppetGrammar.CLASSDEF) ? "class" : "define";
      addIssue(node, this,
        "The complexity of this " + nodeType + " is " + complexity + " which is greater than " + max + " authorized. Split this " + nodeType + ".",
        (double) complexity - max);
    }
  }

  @VisibleForTesting
  public void setMax(int max) {
    this.max = max;
  }

}
