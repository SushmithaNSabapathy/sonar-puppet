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
package org.sonar.puppet.frontend.parser.compound_statements

import org.sonar.puppet.frontend.parser.GrammarSpec
import spock.lang.Unroll

import static org.sonar.puppet.PuppetGrammar.RESOURCE
import static org.sonar.puppet.PuppetGrammar.RESOURCE_NAME
import static org.sonar.sslr.tests.Assertions.assertThat

public class ResourceSpec extends GrammarSpec {

  def setup() {
    setRootRule(RESOURCE)
  }

  def "resource with class notify parses correctly"() {
    expect:
    assertThat(p).matches('''package { 'httpd':
								 	ensure => $package_ensure,
								 	name   => $apache_name,
								 	notify => Class['Apache::Service'],
								 }''')
  }

  def "resource with package require parses correctly"() {
    expect:
    assertThat(p).matches('''user { $user:
									ensure  => present,
									gid     => $group,
									require => Package['httpd'],
								 }''')
  }

  def "check fully qualified type"() {
    expect:
    assertThat(p).matches('''concat::fragment { 'Apache ports header':
								ensure  => present,
								target  => $ports_file,
								content => template('apache/ports_header.erb')
							  }''')
  }

  def "array of titles"() {
    expect:
    assertThat(p).matches('''file { ['/etc',
										'/etc/rc.d',
										'/etc/rc.d/init.d',
										'/etc/rc.d/rc0.d',
										'/etc/rc.d/rc1.d']:
								  ensure => directory,
								  owner  => 'root',
								  group  => 'root',
								  mode   => '0755',
								}''')
  }

  def "resource with no attributes"() {
    expect:
    assertThat(p).matches('::apache::mod { \'expires\': }')
  }

  def "resource with unless attribute"() {
    expect:
    assertThat(p).matches('''exec { 'titi':
								  unless => 'toto',
								}''')
  }

  def "handle multiple resource bodies"() {
    expect:
    assertThat(p).matches('''file {
								  '/etc/rc.d':
									ensure => directory,
									owner  => 'root',
									group  => 'root',
									mode   => '0755';

								  '/etc/rc.d/init.d':
									ensure => directory,
									owner  => 'root',
									group  => 'root',
									mode   => '0755';

								  '/etc/rc.d/rc0.d':
									ensure => directory,
									owner  => 'root',
									group  => 'root',
									mode   => '0755';
								}''')
  }

  def "example resource defaults parses correctly"() {
    expect:
    assertThat(p).matches("""Exec {
                                  path        => '/usr/bin:/bin:/usr/sbin:/sbin',
                                  environment => 'RUBYLIB=/opt/puppet/lib/ruby/site_ruby/1.8/',
                                  logoutput   => true,
                                  timeout     => 180,
                                }""")
    assertThat(p).matches("""File {
                                  owner => 'root',
                                  group => '0',
                                  mode  => '0644',
                                }""")

  }

  def "Should properly parse resource names of every allowed type"() {
    expect:
    assertThat(p).matches('file { $::abc[2]: }')
    assertThat(p).matches('file { $::abc[2][3]: }')
    assertThat(p).matches('''file { $osfamily ? {
                                      'Solaris' => 'wheel',
                                      default   => 'root',
                                  }:
                             }''')
    assertThat(p).matches('file { [1, 2]: }')
    assertThat(p).matches("file { ['1', '2']: }")
    assertThat(p).matches('file { default: }')
    assertThat(p).matches('file { name: }')
    assertThat(p).matches("file { 'abc': }")
    assertThat(p).matches('file { "abc": }')
    assertThat(p).matches('file { $abc: }')
    assertThat(p).matches('file { "${abc}": }')
    assertThat(p).matches('file { String: }')
  }

  @Unroll
  def "resource names parse"() {
    given:
    setRootRule(RESOURCE_NAME)

    expect:
    assertThat(p).matches(name)

    where:
    name << ["'dav_svn'", '"dav_svn"']
  }
}
