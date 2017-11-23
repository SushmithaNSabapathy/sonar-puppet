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

import com.google.common.io.Files;
import com.sonar.sslr.api.AstNode;
import org.sonar.api.utils.SonarException;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.puppet.CharsetAwareVisitor;
import org.sonar.puppet.PuppetCheckVisitor;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.List;

@Rule(
  key = "LineLength",
  priority = Priority.MINOR,
  name = "Lines should not be too long",
  tags = Tags.CONVENTION)
@ActivatedByDefault
@SqaleConstantRemediation("1min")
public class LineLengthCheck extends PuppetCheckVisitor implements CharsetAwareVisitor {

  private static final int DEFAULT_MAXIMUM_LINE_LENGTH = 140;
  private Charset charset;

  @RuleProperty(
    key = "maximumLineLength",
    defaultValue = "" + DEFAULT_MAXIMUM_LINE_LENGTH)
  public int maximumLineLength = DEFAULT_MAXIMUM_LINE_LENGTH;

  @Override
  public void setCharset(Charset charset) {
    this.charset = charset;
  }

  @Override
  public void visitFile(@Nullable AstNode astNode) {
    List<String> lines;
    try {
      lines = Files.readLines(getContext().getFile(), charset);
    } catch (IOException e) {
      throw new SonarException(e);
    }
    for (int i = 0; i < lines.size(); i++) {
      String line = lines.get(i);
      if (line.length() > maximumLineLength) {
        addIssue(
          i + 1,
          this,
          MessageFormat.format(
            "The line contains {0,number,integer} characters which is greater than {1,number,integer} authorized.",
            line.length(),
            maximumLineLength));
      }
    }
  }
}
