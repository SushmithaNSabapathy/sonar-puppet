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

import org.sonar.puppet.tree.HexadecimalTree;
import org.sonar.puppet.tree.SyntaxToken;
import org.sonar.plugins.puppet.api.visitors.DoubleDispatchVisitor;

public class HexadecimalTreeImpl extends LiteralTreeImpl implements HexadecimalTree {

  public HexadecimalTreeImpl(SyntaxToken hexadecimal) {
    super(hexadecimal);
  }

  @Override
  public Kind getKind() {
    return Kind.HEXADECIMAL;
  }

  @Override
  public void accept(DoubleDispatchVisitor visitor) {
    visitor.visitHexadecimal(this);
  }

}
