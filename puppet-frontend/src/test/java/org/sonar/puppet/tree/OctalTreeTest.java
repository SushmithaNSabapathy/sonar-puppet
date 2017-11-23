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

public class OctalTreeTest extends PuppetTreeTest {

  public OctalTreeTest() {
    super(PuppetLexicalGrammar.OCTAL);
  }

  @Test
  public void octal() {
    checkParsed("00");
    checkParsed("0123");
    checkParsed(" 0123");
  }

  @Test
  public void notOctal() {
    checkNotParsed("0");
    checkNotParsed(" 0");
    checkNotParsed("1");
    checkNotParsed("123");
    checkNotParsed("0abc");
    checkNotParsed("0x123");
  }

  private void checkParsed(String toParse) {
    OctalTree tree = (OctalTree) parser().parse(toParse);
    assertThat(tree).isNotNull();
    assertThat(tree.value()).isNotNull();
    assertThat(tree.text()).isEqualTo(toParse.trim());
  }

}
