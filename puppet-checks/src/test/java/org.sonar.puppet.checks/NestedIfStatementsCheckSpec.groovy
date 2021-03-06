/*
 * SonarQube Puppet Plugin
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Iain Adams and David RACODON
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.sonar.puppet.frontend.checks

import org.sonar.puppet.PuppetAstScanner
import org.sonar.squidbridge.api.SourceFile
import org.sonar.squidbridge.checks.CheckMessagesVerifier
import spock.lang.Specification

class NestedIfStatementsCheckSpec extends Specification {

  def "validate check"() {
    given:
    SourceFile file = PuppetAstScanner.scanSingleFile(
      new File("src/test/resources/checks/nested_if_statements_ex1.pp"),
      new NestedIfStatementsCheck());

    expect:
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(7).withMessage("Refactor this code to not nest more than 3 \"if\" or \"elsif\" statements.")
      .noMore();
  }

  def "setting the maximum nesting works as expected"() {
    given:
    NestedIfStatementsCheck check = new NestedIfStatementsCheck()
    check.maximumNestingLevel = 2
    SourceFile file = PuppetAstScanner.scanSingleFile(
      new File("src/test/resources/checks/nested_if_statements_ex2.pp"),
      check);

    expect:
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(5).withMessage("Refactor this code to not nest more than 2 \"if\" or \"elsif\" statements.")
      .noMore();
  }

  def "example with nested else if"() {
    given:
    SourceFile file = PuppetAstScanner.scanSingleFile(
      new File("src/test/resources/checks/nested_elseif_statements_ex1.pp"),
      new NestedIfStatementsCheck());

    expect:
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(6).withMessage("Refactor this code to not nest more than 3 \"if\" or \"elsif\" statements.")
      .noMore();
  }

  def "another example with nested elsif"() {
    given:
    SourceFile file = PuppetAstScanner.scanSingleFile(
      new File("src/test/resources/checks/nested_elseif_statements_ex2.pp"),
      new NestedIfStatementsCheck());

    expect:
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(7).withMessage("Refactor this code to not nest more than 3 \"if\" or \"elsif\" statements.")
      .noMore();
  }

  def "testing for if/elsif in else statements"() {
    given:
    SourceFile file = PuppetAstScanner.scanSingleFile(
      new File("src/test/resources/checks/nested_elseif_statements_ex3.pp"),
      new NestedIfStatementsCheck());

    expect:
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(9).withMessage("Refactor this code to not nest more than 3 \"if\" or \"elsif\" statements.")
      .next().atLine(24).withMessage("Refactor this code to not nest more than 3 \"if\" or \"elsif\" statements.")
      .noMore();
  }
}
