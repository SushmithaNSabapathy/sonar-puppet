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
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.puppet.PuppetCheckVisitor;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;

@Rule(
  key = "LintIgnore",
  name = "Useless \"lint:ignore\" and \"lint:endignore\" tags should be removed",
  priority = Priority.MINOR)
@SqaleConstantRemediation("1min")
@ActivatedByDefault
public class LintIgnoreCheck extends PuppetCheckVisitor implements AstAndTokenVisitor {

  private static final String PATTERN_BEGIN = "lint:ignore";
  private static final String PATTERN_END = "lint:endignore";
  private static final String MESSAGE_BEGIN = "Remove this useless \"lint:ignore\" tag.";
  private static final String MESSAGE_END = "Remove this useless \"lint:endignore\" tag.";

  private final CommentContainsPatternChecker checkerBegin = new CommentContainsPatternChecker(this, PATTERN_BEGIN, MESSAGE_BEGIN);
  private final CommentContainsPatternChecker checkerEnd = new CommentContainsPatternChecker(this, PATTERN_END, MESSAGE_END);

  @Override
  public void visitToken(Token token) {
    checkerBegin.visitToken(token);
    checkerEnd.visitToken(token);
  }

}
