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

class TrailingWhitespaceCheckSpec extends Specification {

  def "validate rule"() {
    given:
    TrailingWhitespaceCheck check = new TrailingWhitespaceCheck();

    SourceFile file = PuppetAstScanner.scanSingleFile(new File("src/test/resources/checks/trailingWhitespace.pp"), check);

    expect:
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(2).withMessage("Remove the useless trailing whitespaces at the end of this line.")
      .next().atLine(3).withMessage("Remove the useless trailing whitespaces at the end of this line.")
      .next().atLine(4).withMessage("Remove the useless trailing whitespaces at the end of this line.")
      .next().atLine(6).withMessage("Remove the useless trailing whitespaces at the end of this line.")
      .noMore();
  }
}
