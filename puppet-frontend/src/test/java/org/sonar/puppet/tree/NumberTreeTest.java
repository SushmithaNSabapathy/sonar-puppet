/*
 * SonarQube CSS / SCSS / Less Analyzer
 * Copyright (C) 2013-2017 David RACODON
 * mailto: david.racodon@gmail.com
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
package org.sonar.puppet.tree;

import org.junit.Test;
import org.sonar.puppet.parser.PuppetLexicalGrammar;

import static org.fest.assertions.Assertions.assertThat;

public class NumberTreeTest extends PuppetTreeTest {

  public NumberTreeTest() {
    super(PuppetLexicalGrammar.NUMBER);
  }

  @Test
  public void number() {
    checkParsed("1", Tree.Kind.DECIMAL);
    checkParsed(" 1", Tree.Kind.DECIMAL);
    checkParsed("1.2", Tree.Kind.DECIMAL);
    checkParsed("1.2e3", Tree.Kind.DECIMAL);

    checkParsed("066", Tree.Kind.OCTAL);
    checkParsed("0454", Tree.Kind.OCTAL);

    checkParsed("0x44", Tree.Kind.HEXADECIMAL);
    checkParsed("0X44", Tree.Kind.HEXADECIMAL);
    checkParsed("0x4b4", Tree.Kind.HEXADECIMAL);
    checkParsed("0x4B4", Tree.Kind.HEXADECIMAL);
  }

  @Test
  public void notNumber() {
    checkNotParsed("abc");
    checkNotParsed(".2");
  }

  private void checkParsed(String toParse, Tree.Kind expectedKind) {
    NumberTree tree = (NumberTree) parser().parse(toParse);
    assertThat(tree.number()).isNotNull();
    assertThat(tree.number().is(expectedKind)).isTrue();
  }

}
