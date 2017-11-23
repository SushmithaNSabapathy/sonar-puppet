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

import java.util.Iterator;

public class IfStatementTreeImpl extends TreeImpl implements IfStatementTree {

  private final SyntaxToken ifKeyword;
  private final ExpressionTree condition;
  private final StatementBlockTree statementBlock;

  public IfStatementTreeImpl(SyntaxToken ifKeyword, ExpressionTree condition, StatementBlockTree statementBlock) {
    this.ifKeyword = ifKeyword;
    this.condition = condition;
    this.statementBlock = statementBlock;
  }

  @Override
  public Kind getKind() {
    return Kind.IF;
  }

  @Override
  public void accept(DoubleDispatchVisitor visitor) {
    visitor.visitIfStatement(this);
  }

  @Override
  public Iterator<Tree> childrenIterator() {
    return Iterators.forArray(ifKeyword, condition, statementBlock);
  }

  @Override
  public SyntaxToken ifKeyword() {
    return ifKeyword;
  }

  @Override
  public ExpressionTree condition() {
    return condition;
  }

  @Override
  public StatementBlockTree statementBlock() {
    return statementBlock;
  }

}
