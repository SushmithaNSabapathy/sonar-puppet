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
import org.sonar.puppet.tree.StatementBlockTree;
import org.sonar.puppet.tree.StatementTree;
import org.sonar.puppet.tree.SyntaxToken;
import org.sonar.puppet.tree.Tree;
import org.sonar.plugins.puppet.api.visitors.DoubleDispatchVisitor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StatementBlockTreeImpl extends TreeImpl implements StatementBlockTree {

  private final SyntaxToken openinCurlyBrace;
  private final List<StatementTree> statements;
  private final SyntaxToken closingCurlyBrace;

  public StatementBlockTreeImpl(SyntaxToken openinCurlyBrace, @Nullable List<StatementTree> statements, SyntaxToken closingCurlyBrace) {
    this.openinCurlyBrace = openinCurlyBrace;
    this.statements = statements != null ? statements : new ArrayList<>();
    this.closingCurlyBrace = closingCurlyBrace;
  }

  @Override
  public Kind getKind() {
    return Kind.STATEMENT_BLOCK;
  }

  @Override
  public void accept(DoubleDispatchVisitor visitor) {
    visitor.visitStatementBlock(this);
  }

  @Override
  public Iterator<Tree> childrenIterator() {
    return Iterators.concat(
      Iterators.singletonIterator(openinCurlyBrace),
      statements.iterator(),
      Iterators.singletonIterator(closingCurlyBrace));
  }

  @Override
  public SyntaxToken openingCurlyBrace() {
    return openinCurlyBrace;
  }

  @Override
  public List<StatementTree> statements() {
    return statements;
  }

  @Override
  public SyntaxToken closingCurlyBrace() {
    return closingCurlyBrace;
  }

}
