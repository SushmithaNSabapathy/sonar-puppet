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
package org.sonar.puppet.tree.impl;

import com.google.common.collect.Iterators;
import org.sonar.puppet.tree.*;
import org.sonar.plugins.puppet.api.visitors.DoubleDispatchVisitor;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.function.Function;

public class NodeTreeImpl extends TreeImpl implements NodeTree {

  private final SyntaxToken nodeKeyword;
  private final SeparatedList<NodeNameTree, CommaDelimiterTree> nodeNames;
  private final SyntaxToken inheritsKeyword;
  private final NodeNameTree parentNodeName;
  private final StatementBlockTree statementBlock;

  public NodeTreeImpl(SyntaxToken nodeKeyword, SeparatedList<NodeNameTree, CommaDelimiterTree> nodeNames, @Nullable SyntaxToken inheritsKeyword, @Nullable NodeNameTree parentNodeName, StatementBlockTree statementBlock) {
    this.nodeKeyword = nodeKeyword;
    this.nodeNames = nodeNames;
    this.inheritsKeyword = inheritsKeyword;
    this.parentNodeName = parentNodeName;
    this.statementBlock = statementBlock;
  }

  @Override
  public Kind getKind() {
    return Kind.NODE;
  }

  @Override
  public void accept(DoubleDispatchVisitor visitor) {
    visitor.visitNode(this);
  }


  @Override
  public Iterator<Tree> childrenIterator() {
    return Iterators.concat(
      Iterators.singletonIterator(nodeKeyword),
      nodeNames.elementsAndSeparators(Function.identity(), Function.identity()),
      Iterators.forArray(
        inheritsKeyword,
        parentNodeName,
        statementBlock));
  }

  @Override
  public SyntaxToken nodeKeyword() {
    return nodeKeyword;
  }

  @Override
  public SeparatedList<NodeNameTree, CommaDelimiterTree> nodeNames() {
    return nodeNames;
  }

  @Override
  @Nullable
  public SyntaxToken inheritsKeyword() {
    return inheritsKeyword;
  }

  @Override
  @Nullable
  public NodeNameTree parentNodeName() {
    return parentNodeName;
  }

  @Override
  public StatementBlockTree statementBlock() {
    return statementBlock;
  }

}
