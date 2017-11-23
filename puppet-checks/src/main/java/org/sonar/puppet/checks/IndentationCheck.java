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

import com.google.common.collect.ImmutableList;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.puppet.PuppetCheckVisitor;
import org.sonar.puppet.PuppetGrammar;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Rule(
  key = "Indentation",
  priority = Priority.MINOR,
  name = "Code should be properly indented",
  tags = Tags.CONVENTION)
@SqaleConstantRemediation("1min")
public class IndentationCheck extends PuppetCheckVisitor {

  private static final int DEFAULT_INDENTATION_LEVEL = 2;

  private static final List<PuppetGrammar> BLOCK_TYPES = ImmutableList.of(
    PuppetGrammar.FILE_INPUT,
    PuppetGrammar.CLASSDEF,
    PuppetGrammar.DEFINITION,
    PuppetGrammar.NODE_DEFINITION,
    PuppetGrammar.IF_STMT,
    PuppetGrammar.ELSIF_STMT,
    PuppetGrammar.ELSE_STMT,
    PuppetGrammar.CASE_STMT,
    PuppetGrammar.CASE_MATCHER,
    PuppetGrammar.SINTVALUES,
    PuppetGrammar.UNLESS_STMT,
    PuppetGrammar.RESOURCE,
    PuppetGrammar.RESOURCE_INST,
    PuppetGrammar.PARAMS,
    PuppetGrammar.ANY_PARAMS,
    PuppetGrammar.HASH_PAIRS,
    PuppetGrammar.ARRAY);

  private int expectedLevel;
  private Set checkedLines;

  @Override
  public void init() {
    subscribeTo(BLOCK_TYPES.toArray(new AstNodeType[BLOCK_TYPES.size()]));
  }

  @Override
  public void visitFile(AstNode node) {
    expectedLevel = 0;
    checkedLines = new HashSet();
  }

  @Override
  public void visitNode(AstNode node) {
    if (node.is(PuppetGrammar.ELSIF_STMT, PuppetGrammar.ELSE_STMT, PuppetGrammar.RESOURCE_INST, PuppetGrammar.FILE_INPUT)) {
      // Do not increase the indentation level (already increased by the IF_STMT)
    } else if (node.is(PuppetGrammar.RESOURCE)) {
      if (node.getChildren(PuppetGrammar.RESOURCE_INST).size() > 1) {
        expectedLevel += DEFAULT_INDENTATION_LEVEL;
      }
    } else {
      expectedLevel += DEFAULT_INDENTATION_LEVEL;
    }

    if (node.is(PuppetGrammar.FILE_INPUT)) {
      checkIndentation(node.getChildren(PuppetGrammar.STATEMENT));
    } else if (node.is(PuppetGrammar.CLASSDEF, PuppetGrammar.DEFINITION, PuppetGrammar.NODE_DEFINITION)) {
      if (node.getFirstChild(PuppetGrammar.ARGUMENTS) != null) {
        checkIndentation(node.getFirstChild(PuppetGrammar.ARGUMENTS).getChildren(PuppetGrammar.ARGUMENT));
      }
      checkIndentation(node.getChildren(PuppetGrammar.STATEMENT));
    } else if (node.is(PuppetGrammar.RESOURCE_INST) && node.getParent().getChildren(PuppetGrammar.RESOURCE_INST).size() > 1) {
      checkIndentation(node.getChildren(PuppetGrammar.RESOURCE_NAME));
    } else if (node.is(PuppetGrammar.PARAMS, PuppetGrammar.ANY_PARAMS)) {
      checkIndentation(node.getChildren(PuppetGrammar.PARAM, PuppetGrammar.ANY_PARAM));
    } else if (node.is(PuppetGrammar.CASE_STMT)) {
      checkIndentation(node.getChildren(PuppetGrammar.CASE_MATCHER));
    } else if (node.is(PuppetGrammar.SINTVALUES)) {
      checkIndentation(node.getChildren(PuppetGrammar.SELECTVAL));
    } else if (node.is(PuppetGrammar.CASE_MATCHER, PuppetGrammar.UNLESS_STMT, PuppetGrammar.IF_STMT, PuppetGrammar.ELSIF_STMT, PuppetGrammar.ELSE_STMT)) {
      checkIndentation(node.getChildren(PuppetGrammar.STATEMENT));
    } else if (node.is(PuppetGrammar.HASH_PAIRS)) {
      checkIndentation(node.getChildren(PuppetGrammar.HASH_PAIR));
    } else if (node.is(PuppetGrammar.ARRAY)) {
      checkIndentation(node.getChildren(PuppetGrammar.EXPRESSION));
    }
  }

  @Override
  public void leaveNode(AstNode node) {
    if (node.is(PuppetGrammar.ELSIF_STMT, PuppetGrammar.ELSE_STMT, PuppetGrammar.RESOURCE_INST, PuppetGrammar.FILE_INPUT)) {
      // Do not decrease the indentation level (already decreased by the IF_STMT)
    } else if (node.is(PuppetGrammar.RESOURCE)) {
      if (node.getChildren(PuppetGrammar.RESOURCE_INST).size() > 1) {
        expectedLevel -= DEFAULT_INDENTATION_LEVEL;
      }
    } else {
      expectedLevel -= DEFAULT_INDENTATION_LEVEL;
    }
  }

  private void checkIndentation(List<? extends AstNode> nodes) {
    List<Integer> issueLines = new ArrayList<>();
    for (AstNode node : nodes) {
      if (!checkedLines.contains(node.getTokenLine()) && node.getToken().getColumn() != expectedLevel) {
        issueLines.add(node.getTokenLine());
      }
      checkedLines.add(node.getToken().getLine());
    }
    if (!issueLines.isEmpty()) {
      groupIssues(issueLines);
    }
  }

  private void groupIssues(List<Integer> issues) {
    int start = 0;
    for (int i = 1; i < issues.size(); i++) {
      if (issues.get(i - 1) + 1 != issues.get(i)) {
        raiseIssue(issues.get(start), issues.get(i - 1) - issues.get(start) + 1);
        start = i;
      }
    }
    raiseIssue(issues.get(start), issues.get(issues.size() - 1) - issues.get(start) + 1);
  }

  private void raiseIssue(int start, int lines) {
    if (lines == 1) {
      addIssue(start, this, "Make this line start at column " + (expectedLevel + 1) + ".");
    } else {
      addIssue(start, this, "The following " + lines + " lines should start on column " + (expectedLevel + 1) + ".");
    }
  }
}
