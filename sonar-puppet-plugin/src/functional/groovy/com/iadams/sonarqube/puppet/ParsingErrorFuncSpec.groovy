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
package org.sonar.puppet.frontend

import com.iadams.sonarqube.functional.FunctionalSpecBase

class ParsingErrorFuncSpec extends FunctionalSpecBase {

  def "run sonar-runner un-parsable file"() {
    when:
    copyResources("parsing_error.pp", "parsing_error.pp")
    deleteProject()
    runSonarRunner()
    sleep(5000) //analysis needs time to be run on server

    then:
    analysisFinishedSuccessfully()
    analysisLogContainsErrorsOrWarnings()
    analysisLogContains("Parse error at line 1 column 13:")
    theFollowingProjectMetricsHaveTheFollowingValue([violations: 1, files: 1])
    theFollowingFileMetricsHaveTheFollowingValue('parsing_error.pp', [violations: 1])
  }
}
