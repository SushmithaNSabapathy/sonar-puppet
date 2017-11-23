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

public class DecimalTreeTest extends PuppetTreeTest {

  public DecimalTreeTest() {
    super(PuppetLexicalGrammar.DECIMAL);
  }

  @Test
  public void decimal() {
    checkParsed("1");
    checkParsed(" 1");
    checkParsed("0");
    checkParsed(" 0");
    checkParsed("1.2");
    checkParsed("1.2433");
    checkParsed("14.2433");
    checkParsed("0.2");
    checkParsed("0.2344");
    checkParsed("1e3");
    checkParsed("1E3");
    checkParsed("1e-3");
    checkParsed("1E-3");
    checkParsed("1.322e3");
    checkParsed("1.322e-3");
    checkParsed("1.322e-33");
  }

  @Test
  public void notDecimal() {
    checkNotParsed("+1");
    checkNotParsed("-1");
    checkNotParsed("+1.2");
    checkNotParsed("-1.2");
    checkNotParsed(".2");
  }

  private void checkParsed(String toParse) {
    DecimalTree tree = (DecimalTree) parser().parse(toParse);
    assertThat(tree).isNotNull();
    assertThat(tree.value()).isNotNull();
    assertThat(tree.text()).isEqualTo(toParse.trim());
  }

}
