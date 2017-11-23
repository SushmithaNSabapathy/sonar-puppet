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

import java.util.regex.Pattern;

@Rule(
  key = "FileModes",
  name = "File mode should be represented by a valid 4-digit octal value (rather than 3) or symbolically",
  priority = Priority.MAJOR,
  tags = {Tags.BUG, Tags.FUTURE_PARSER})
@SqaleConstantRemediation("10min")
@ActivatedByDefault
public class FileModeCheck extends PuppetCheckVisitor {

  private static final String REGEX = "['|\"]?([0-7]{4}|([ugoa]*[-=+][-=+rstwxXugo]*)(,[ugoa]*[-=+][-=+rstwxXugo]*)*)['|\"]?";
  private static final Pattern PATTERN = Pattern.compile(REGEX);

  private static final String MESSAGE_OCTAL = "Set the file mode to a 4-digit octal value surrounded by single quotes.";
  private static final String MESSAGE_DOUBLE_QUOTES = "Replace double quotes by single quotes.";
  private static final String MESSAGE_INVALID = "Update the file mode to a valid value surrounded by single quotes.";

  @Override
  public void init() {
    subscribeTo(PuppetGrammar.PARAM);
  }

  @Override
  public void visitNode(AstNode paramNode) {
    if ("mode".equals(paramNode.getTokenValue())
      && "file".equalsIgnoreCase(paramNode.getFirstAncestor(PuppetGrammar.RESOURCE, PuppetGrammar.RESOURCE_OVERRIDE, PuppetGrammar.COLLECTION).getTokenValue())) {
      AstNode expressionNode = paramNode.getFirstChild(PuppetGrammar.EXPRESSION);
      if (expressionNode.getToken().getType().equals(PuppetTokenType.OCTAL_INTEGER) || expressionNode.getToken().getType().equals(PuppetTokenType.INTEGER)) {
        addIssue(expressionNode, this, MESSAGE_OCTAL);
      } else if (expressionNode.getToken().getType().equals(PuppetTokenType.DOUBLE_QUOTED_STRING_LITERAL) && PATTERN.matcher(expressionNode.getTokenValue()).matches()) {
        addIssue(expressionNode, this, MESSAGE_DOUBLE_QUOTES);
      } else if (expressionNode.getToken().getType().equals(PuppetTokenType.SINGLE_QUOTED_STRING_LITERAL) && !PATTERN.matcher(expressionNode.getTokenValue()).matches()
        || expressionNode.getToken().getType().equals(PuppetTokenType.DOUBLE_QUOTED_STRING_LITERAL) && !PATTERN.matcher(expressionNode.getTokenValue()).matches()
        && !CheckStringUtils.containsVariable(expressionNode.getTokenValue())) {
        addIssue(expressionNode, this, MESSAGE_INVALID);
      }
    }
  }

}
