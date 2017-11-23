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
import org.sonar.puppet.tree.ProgramTree;
import org.sonar.puppet.tree.StatementTree;
import org.sonar.puppet.tree.SyntaxToken;
import org.sonar.puppet.tree.Tree;
import org.sonar.plugins.puppet.api.visitors.DoubleDispatchVisitor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProgramTreeImpl extends TreeImpl implements ProgramTree {

  private final SyntaxToken byteOrderMark;
  private final List<StatementTree> statements;
  private final SyntaxToken eof;

  public ProgramTreeImpl(@Nullable SyntaxToken byteOrderMark, @Nullable List<StatementTree> statements, SyntaxToken eof) {
    this.byteOrderMark = byteOrderMark;
    this.statements = statements != null ? statements : new ArrayList<>();
    this.eof = eof;
  }

  @Override
  public Kind getKind() {
    return Kind.PROGRAM;
  }

  @Override
  public void accept(DoubleDispatchVisitor visitor) {
    visitor.visitProgram(this);
  }


  @Override
  public Iterator<Tree> childrenIterator() {
    return Iterators.concat(
      Iterators.singletonIterator(byteOrderMark),
      statements.iterator(),
      Iterators.singletonIterator(eof));
  }

  @Override
  public boolean hasByteOrderMark() {
    return byteOrderMark != null;
  }

  @Override
  public List<StatementTree> statements() {
    return statements;
  }

}
