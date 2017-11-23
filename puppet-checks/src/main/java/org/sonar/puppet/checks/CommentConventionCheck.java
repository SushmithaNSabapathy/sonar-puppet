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

import com.sonar.sslr.api.AstAndTokenVisitor;
import com.sonar.sslr.api.Token;
import com.sonar.sslr.api.Trivia;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.puppet.PuppetCheckVisitor;
import org.sonar.puppet.lexer.PuppetLexer;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;

import java.util.Iterator;
import java.util.regex.Pattern;

@Rule(
  key = "CommentConvention",
  name = "All comments should be formatted consistently",
  priority = Priority.MINOR,
  tags = {Tags.CONVENTION})
@ActivatedByDefault
@SqaleConstantRemediation("1min")
public class CommentConventionCheck extends PuppetCheckVisitor implements AstAndTokenVisitor {

  Pattern pattern = Pattern.compile(PuppetLexer.SLASH_LINE_COMMENT + "|" + PuppetLexer.MULTI_LINE_COMMENT);

  @Override
  public void visitToken(Token token) {
    Iterator iterator = token.getTrivia().iterator();
    while (iterator.hasNext()) {
      Trivia trivia = (Trivia) iterator.next();
      if (trivia.isComment() && pattern.matcher(trivia.getToken().getOriginalValue()).matches()) {
        addIssue(trivia.getToken().getLine(), this, "Use starting comment token '#' instead.");
      }
    }
  }

}
