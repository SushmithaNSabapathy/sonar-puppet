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
package org.sonar.puppet;

import com.sonar.sslr.api.AstAndTokenVisitor;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Grammar;
import com.sonar.sslr.api.Token;
import org.sonar.squidbridge.SquidAstVisitor;
import org.sonar.squidbridge.measures.MetricDef;

import static com.sonar.sslr.api.GenericTokenType.EOF;

public class PuppetLinesOfCodeVisitor<G extends Grammar> extends SquidAstVisitor<G> implements AstAndTokenVisitor {

  private final MetricDef metric;
  private int lastTokenLine;

  public PuppetLinesOfCodeVisitor(MetricDef metric) {
    this.metric = metric;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void visitFile(AstNode node) {
    lastTokenLine = -1;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void visitToken(Token token) {
    if (!token.getType().equals(EOF)) {
      String[] tokenLines = token.getValue().split("\n", -1);

      int firstLineAlreadyCounted = lastTokenLine == token.getLine() ? 1 : 0;
      getContext().peekSourceCode().add(metric, (double) tokenLines.length - firstLineAlreadyCounted);

      lastTokenLine = token.getLine() + tokenLines.length - 1;
    }

  }
}
