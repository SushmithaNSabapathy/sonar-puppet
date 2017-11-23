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
import org.sonar.puppet.tree.NodeNameTree;
import org.sonar.puppet.tree.Tree;
import org.sonar.plugins.puppet.api.visitors.DoubleDispatchVisitor;

import java.util.Iterator;

public class NodeNameTreeImpl extends TreeImpl implements NodeNameTree {

  private final Tree nodeName;

  public NodeNameTreeImpl(Tree nodeName) {
    this.nodeName = nodeName;
  }

  @Override
  public Kind getKind() {
    return Kind.NODE_NAME;
  }

  @Override
  public void accept(DoubleDispatchVisitor visitor) {
    visitor.visitNodeName(this);
  }


  @Override
  public Iterator<Tree> childrenIterator() {
    return Iterators.singletonIterator(nodeName);
  }

  @Override
  public Tree nodeName() {
    return nodeName;
  }

}
