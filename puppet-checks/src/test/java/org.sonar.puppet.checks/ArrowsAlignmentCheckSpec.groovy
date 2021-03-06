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

class ArrowsAlignmentCheckSpec extends Specification {

  private static final String MESSAGE = "Properly align arrows (arrows are not all placed at the same column).";
  private static final String MESSAGE_SPACE = "Properly align arrows (arrows are not all placed one space ahead of the longest attribute).";

  def "validate rule"() {
    given:
    SourceFile file = PuppetAstScanner.scanSingleFile(
      new File("src/test/resources/checks/arrows_alignment.pp"),
      new ArrowsAlignmentCheck()
    );

    expect:
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(9).withMessage(MESSAGE_SPACE)
      .next().atLine(18).withMessage(MESSAGE)
      .next().atLine(23).withMessage(MESSAGE)
      .next().atLine(28).withMessage(MESSAGE_SPACE)
      .next().atLine(40).withMessage(MESSAGE_SPACE)
      .next().atLine(49).withMessage(MESSAGE)
      .next().atLine(54).withMessage(MESSAGE)
      .next().atLine(59).withMessage(MESSAGE_SPACE)
      .next().atLine(68).withMessage(MESSAGE_SPACE)
      .next().atLine(77).withMessage(MESSAGE)
      .next().atLine(82).withMessage(MESSAGE)
      .next().atLine(87).withMessage(MESSAGE_SPACE)
      .next().atLine(92).withMessage(MESSAGE)
      .next().atLine(99).withMessage(MESSAGE_SPACE)
      .next().atLine(103).withMessage(MESSAGE)
      .next().atLine(116).withMessage(MESSAGE)
      .next().atLine(121).withMessage(MESSAGE_SPACE)
      .next().atLine(131).withMessage(MESSAGE)
      .next().atLine(136).withMessage(MESSAGE)
      .next().atLine(141).withMessage(MESSAGE_SPACE)
      .noMore();
  }
}
