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
package org.sonar.puppet.frontend.parser.simple_statements

import org.sonar.puppet.frontend.parser.GrammarSpec

import static org.sonar.puppet.PuppetGrammar.RESOURCE_OVERRIDE
import static org.sonar.sslr.tests.Assertions.assertThat

class ResourceOverrideSpec extends GrammarSpec {

  def setup() {
    setRootRule(RESOURCE_OVERRIDE)
  }

  def "example resource references parses correctly"() {
    expect:
    assertThat(p).matches('''File['logrotate.conf'] {
								  content => template('logrotate/spec.erb'),
								}''')

    assertThat(p).matches('''File['/etc/passwd'] {
								  owner => 'root',
								  group => 'root',
								  mode  => '0640',
								}''')
  }
}