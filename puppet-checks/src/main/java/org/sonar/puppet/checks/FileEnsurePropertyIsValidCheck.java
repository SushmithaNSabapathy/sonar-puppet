/*
 * SonarQube Puppet Analyzer
 * Copyright (C) 2017-2017 David RACODON
 * david.racodon@gmail.com
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
package org.sonar.puppet.checks;

import com.sonar.sslr.api.AstNode;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.puppet.PuppetCheckVisitor;
import org.sonar.puppet.PuppetGrammar;
import org.sonar.puppet.PuppetTokenType;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;

@Rule(
  key = "FileEnsurePropertyIsValid",
  priority = Priority.CRITICAL,
  name = "\"ensure\" attribute of \"file\" resource should be valid",
  tags = {Tags.BUG})
@ActivatedByDefault
@SqaleConstantRemediation("10min")
public class FileEnsurePropertyIsValidCheck extends PuppetCheckVisitor {

  private static final String ACCEPTED_NAMES = "present|absent|file|directory|link";

  @Override
  public void init() {
    subscribeTo(PuppetGrammar.PARAM);
  }

  @Override
  public void visitNode(AstNode paramNode) {
    AstNode expressionNode = paramNode.getFirstChild(PuppetGrammar.EXPRESSION);
    if ("ensure".equals(paramNode.getFirstChild(PuppetGrammar.PARAM_NAME).getTokenValue())
      && "file".equalsIgnoreCase(paramNode.getFirstAncestor(PuppetGrammar.RESOURCE, PuppetGrammar.RESOURCE_OVERRIDE, PuppetGrammar.COLLECTION).getTokenValue())) {
      checkEnsureValidString(expressionNode);
      checkEnsureValidName(expressionNode);
    }
  }

  private void checkEnsureValidString(AstNode expressionNode) {
    if (expressionNode.getFirstChild(PuppetTokenType.SINGLE_QUOTED_STRING_LITERAL, PuppetTokenType.DOUBLE_QUOTED_STRING_LITERAL) != null) {
      String value = expressionNode.getFirstChild(PuppetTokenType.SINGLE_QUOTED_STRING_LITERAL, PuppetTokenType.DOUBLE_QUOTED_STRING_LITERAL).getTokenValue();
      String unquotedValue = value.substring(1, value.length() - 1);
      if (!CheckStringUtils.containsVariable(unquotedValue) && !unquotedValue.matches(ACCEPTED_NAMES)) {
        addIssue(expressionNode, this, "Fix the invalid \"ensure\" property.");
      }
    }
  }

  private void checkEnsureValidName(AstNode expressionNode) {
    if (expressionNode.getFirstChild(PuppetTokenType.NAME) != null
      && !expressionNode.getFirstChild(PuppetTokenType.NAME).getTokenValue().matches(ACCEPTED_NAMES)) {
      addIssue(expressionNode, this, "Fix the invalid \"ensure\" property.");
    }
  }

}
