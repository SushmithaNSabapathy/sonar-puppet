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
import org.sonar.sslr.ast.AstSelect;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Rule(
  key = "S1862",
  priority = Priority.CRITICAL,
  name = "Related \"if/elsif\" statements or \"cases\" in \"case\" or \"selector\" statement should not have the same condition",
  tags = {Tags.BUG, Tags.UNUSED, Tags.PITFALL})
@ActivatedByDefault
@SqaleConstantRemediation("10min")
public class DuplicateConditionCheck extends PuppetCheckVisitor {

  private List<AstNode> ignoreList;

  @Override
  public void init() {
    subscribeTo(PuppetGrammar.IF_STMT, PuppetGrammar.CASE_STMT, PuppetGrammar.SINTVALUES);
  }

  @Override
  public void visitFile(@Nullable AstNode astNode) {
    ignoreList = new ArrayList<>();
  }

  @Override
  public void visitNode(AstNode node) {
    List<AstNode> conditions = new ArrayList<>();
    if (node.is(PuppetGrammar.IF_STMT)) {
      if (ignoreList.contains(node)) {
        return;
      }
      conditions = getConditionsToCompare(node);
    } else if (node.is(PuppetGrammar.CASE_STMT)) {
      for (AstNode matcher : node.getChildren(PuppetGrammar.CASE_MATCHER)) {
        conditions.addAll(matcher.getFirstChild(PuppetGrammar.CASE_VALUES).getChildren(PuppetGrammar.SELECTLHAND));
      }
    } else if (node.is(PuppetGrammar.SINTVALUES)) {
      for (AstNode selectVal : node.getChildren(PuppetGrammar.SELECTVAL)) {
        conditions.addAll(selectVal.getChildren(PuppetGrammar.SELECTLHAND));
      }
    }
    findSameConditions(conditions);
  }

  private List<AstNode> getConditionsToCompare(AstNode ifStmt) {
    List<AstNode> conditions = new ArrayList<>();
    conditions.add(ifStmt.getFirstChild(PuppetGrammar.EXPRESSION).getFirstChild());

    for (AstNode elsifNode : ifStmt.getChildren(PuppetGrammar.ELSIF_STMT)) {
      conditions.addAll(getConditionsToCompare(elsifNode));
    }

    AstNode elseNode = ifStmt.getFirstChild(PuppetGrammar.ELSE_STMT);

    if (conditions.size() == 1 && elseNode != null) {
      List<AstNode> statements = elseNode.getChildren(PuppetGrammar.STATEMENT);
      lookForElseIfs(conditions, statements);
    }
    return conditions;
  }

  private void lookForElseIfs(List<AstNode> conditions, List<AstNode> statements) {
    AstNode singleIfChild = singleIfChild(statements);
    if (singleIfChild != null) {
      ignoreList.add(singleIfChild);
      conditions.addAll(getConditionsToCompare(singleIfChild));
    }
  }

  private static AstNode singleIfChild(List<AstNode> statements) {
    if (statements.size() == 1) {
      AstSelect nestedIf = statements.get(0).select()
        .children(PuppetGrammar.COMPOUND_STMT)
        .children(PuppetGrammar.IF_STMT);
      if (nestedIf.size() == 1) {
        return nestedIf.get(0);
      }
    }
    return null;
  }

  private void findSameConditions(List<AstNode> conditions) {
    for (int i = 1; i < conditions.size(); i++) {
      checkCondition(conditions, i);
    }
  }

  private void checkCondition(List<AstNode> conditions, int index) {
    for (int j = 0; j < index; j++) {
      if (equalNodes(conditions.get(j), conditions.get(index))) {
        String message = String.format("This branch duplicates the one on line %s.", conditions.get(j).getToken().getLine());
        getContext().createLineViolation(this, message, conditions.get(index).getToken().getLine());
        return;
      }
    }
  }

  public static boolean equalNodes(AstNode node1, AstNode node2) {
    if (!node1.getType().equals(node2.getType()) || node1.getNumberOfChildren() != node2.getNumberOfChildren()) {
      return false;
    }

    if (node1.getNumberOfChildren() == 0) {
      return node1.getToken().getValue().equals(node2.getToken().getValue());
    }

    List<AstNode> children1 = node1.getChildren();
    List<AstNode> children2 = node2.getChildren();
    for (int i = 0; i < children1.size(); i++) {
      if (!equalNodes(children1.get(i), children2.get(i))) {
        return false;
      }
    }
    return true;
  }
}
