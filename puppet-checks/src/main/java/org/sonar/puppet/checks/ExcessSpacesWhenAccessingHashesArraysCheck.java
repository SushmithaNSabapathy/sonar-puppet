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
import org.sonar.puppet.PuppetPunctuator;
import org.sonar.puppet.PuppetTokenType;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;

@Rule(
  key = "ExcessSpacesWhenAccessingHashesArrays",
  priority = Priority.CRITICAL,
  name = "There should not be any whitespace between the hash/array variable and the following brackets",
  tags = {Tags.PITFALL, Tags.FUTURE_PARSER})
@ActivatedByDefault
@SqaleConstantRemediation("2min")
public class ExcessSpacesWhenAccessingHashesArraysCheck extends PuppetCheckVisitor {

  @Override
  public void init() {
    subscribeTo(PuppetGrammar.HASH_ARRAY_ACCESS);
  }

  @Override
  public void visitNode(AstNode node) {
    int variableLastColumn = node.getFirstChild(PuppetTokenType.VARIABLE).getToken().getColumn() + node.getFirstChild(PuppetTokenType.VARIABLE).getTokenValue().length() - 1;
    int bracketColumn = node.getFirstChild(PuppetPunctuator.LBRACK).getToken().getColumn();
    if (bracketColumn != variableLastColumn + 1) {
      addIssue(node, this, "Remove the whitespace between the variable and the left bracket.");
    }
  }

}
