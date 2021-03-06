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

import static org.sonar.puppet.PuppetGrammar.HASH
import static org.sonar.sslr.tests.Assertions.assertThat

class HashSpec extends GrammarSpec {

  def setup() {
    setRootRule(HASH)
  }

  def "example hashes parses correctly"() {
    expect:
    assertThat(p).matches("{ key1 => 'val1', key2 => 'val2' }")
    assertThat(p).matches("{ key1 => 'val1', key2 => 'val2', }")
    assertThat(p).matches("{ key1 => template('abc') }")
  }

  def "hashes with selectors parse"() {
    expect:
    assertThat(p).matches('''{
									  'authnz_ldap' => $::apache::version::distrelease ? {
											'7'     => 'mod_ldap',
											default => 'mod_authz_ldap',
									  }
								 }''')
  }
}
