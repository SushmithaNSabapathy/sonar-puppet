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
package org.sonar.puppet.metrics;

import org.sonar.puppet.tree.Tree;
import org.sonar.plugins.puppet.api.visitors.SubscriptionVisitor;

import java.util.ArrayList;
import java.util.List;

public class FunctionsVisitor extends SubscriptionVisitor {

  private int functions;

  public FunctionsVisitor(Tree tree) {
    functions = 0;
    scanTree(tree);
  }

  @Override
  public List<Tree.Kind> nodesToVisit() {
    // FIXME: return ImmutableList.of(Tree.Kind.SELECTOR, Tree.Kind.AT_RULE);
    return new ArrayList<>();
  }

  @Override
  public void visitNode(Tree tree) {
    functions++;
  }

  public int getNumberOfFunctions() {
    return functions;
  }

}
