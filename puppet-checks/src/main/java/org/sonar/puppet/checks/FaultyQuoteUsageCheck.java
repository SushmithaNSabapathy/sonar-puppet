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
  key = "FaultyQuoteUsage",
  priority = Priority.MINOR,
  name = "Single and double quotes should be properly used",
  tags = Tags.CONVENTION)
@ActivatedByDefault
@SqaleConstantRemediation("2min")
public class FaultyQuoteUsageCheck extends PuppetCheckVisitor {

  @Override
  public void init() {
    subscribeTo(PuppetTokenType.DOUBLE_QUOTED_STRING_LITERAL, PuppetTokenType.SINGLE_QUOTED_STRING_LITERAL);
  }

  @Override
  public void visitNode(AstNode node) {
    checkDoubleQuotedString(node);
    checkSingleQuotedString(node);
  }

  private void checkDoubleQuotedString(AstNode node) {
    if (node.is(PuppetTokenType.DOUBLE_QUOTED_STRING_LITERAL)) {
      String stringWithoutQuotes = node.getTokenValue().substring(1, node.getTokenValue().length() - 1);
      if (!CheckStringUtils.containsVariable(stringWithoutQuotes) && !CheckStringUtils.containsSpecialCharacter(stringWithoutQuotes)) {
        addIssue(node, this, "Surround the string with single quotes instead of double quotes.");
      } else if (!node.getParent().is(PuppetGrammar.RESOURCE_NAME) && CheckStringUtils.containsOnlyVariable(stringWithoutQuotes)) {
        addIssue(node, this, "Remove quotes surrounding this variable.");
      }
    }
  }

  private void checkSingleQuotedString(AstNode node) {
    if (node.is(PuppetTokenType.SINGLE_QUOTED_STRING_LITERAL)) {
      String stringWithoutQuotes = node.getTokenValue().substring(1, node.getTokenValue().length() - 1);
      if (Pattern.compile("\\\\'").matcher(stringWithoutQuotes).find()) {
        addIssue(node, this, "Surround the string with double quotes instead of single quotes and do not escape single quotes inside this string.");
      }
    }
  }

}
