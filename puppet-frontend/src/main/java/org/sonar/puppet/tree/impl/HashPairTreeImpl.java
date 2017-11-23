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
import org.sonar.puppet.tree.ExpressionTree;
import org.sonar.puppet.tree.HashPairTree;
import org.sonar.puppet.tree.SyntaxToken;
import org.sonar.puppet.tree.Tree;
import org.sonar.plugins.puppet.api.visitors.DoubleDispatchVisitor;

import java.util.Iterator;

public class HashPairTreeImpl extends TreeImpl implements HashPairTree {

  private final Tree key;
  private final SyntaxToken farrow;
  private final ExpressionTree value;

  public HashPairTreeImpl(Tree key, SyntaxToken farrow, ExpressionTree value) {
    this.key = key;
    this.farrow = farrow;
    this.value = value;
  }

  @Override
  public Kind getKind() {
    return Kind.HASH_PAIR;
  }

  @Override
  public void accept(DoubleDispatchVisitor visitor) {
    visitor.visitHashPair(this);
  }

  @Override
  public Iterator<Tree> childrenIterator() {
    return Iterators.forArray(key, farrow, value);
  }

  @Override
  public Tree key() {
    return key;
  }

  @Override
  public SyntaxToken farrow() {
    return farrow;
  }

  @Override
  public ExpressionTree value() {
    return value;
  }

}
