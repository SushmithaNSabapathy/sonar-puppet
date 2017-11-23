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
package org.sonar.puppet.frontend.parser

import com.google.common.base.Charsets
import org.sonar.puppet.PuppetConfiguration;
import com.sonar.sslr.api.Grammar;
import com.sonar.sslr.impl.Parser
import org.sonar.puppet.parser.PuppetParser;
import org.sonar.sslr.grammar.GrammarRuleKey
import spock.lang.Specification;

public class GrammarSpec extends Specification {
  protected Parser<Grammar> p = PuppetParser.create(new PuppetConfiguration(Charsets.UTF_8));

  protected void setRootRule(GrammarRuleKey ruleKey) {
    p.setRootRule(p.getGrammar().rule(ruleKey));
  }
}