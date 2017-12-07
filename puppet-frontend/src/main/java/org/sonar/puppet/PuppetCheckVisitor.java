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
package org.sonar.puppet;

import com.google.common.base.Preconditions;
import com.sonar.sslr.api.AstNode;
import org.sonar.api.utils.AnnotationUtils;
import org.sonar.squidbridge.annotations.SqaleLinearRemediation;
import org.sonar.squidbridge.annotations.SqaleLinearWithOffsetRemediation;
import org.sonar.squidbridge.api.CheckMessage;
import org.sonar.squidbridge.api.CodeCheck;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.checks.SquidCheck;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;

public class PuppetCheckVisitor extends SquidCheck {

  public void addIssue(AstNode node, CodeCheck check, String message) {
    addIssue(node.getTokenLine(), check, message, null);
  }

  public void addIssue(AstNode node, CodeCheck check, String message, Double cost) {
    addIssue(node.getTokenLine(), check, message, cost);
  }

  public void addIssue(int line, CodeCheck check, String message) {
    addIssue(line, check, message, null);
  }

  public void addIssueOnFile(CodeCheck check, String message) {
    addIssue(-1, check, message, null);
  }

  public void addIssue(@Nullable Integer line, CodeCheck check, String message, @Nullable Double cost) {
    Preconditions.checkNotNull(check);
    Preconditions.checkNotNull(message);
    CheckMessage checkMessage = new CheckMessage(check, message);
    if (line > 0) {
      checkMessage.setLine(line);
    }
    if (cost == null) {
      Annotation linear = AnnotationUtils.getAnnotation(check, SqaleLinearRemediation.class);
      Annotation linearWithOffset = AnnotationUtils.getAnnotation(check, SqaleLinearWithOffsetRemediation.class);
      if (linear != null || linearWithOffset != null) {
        throw new IllegalStateException("A check annotated with a linear SQALE function should provide an effort to fix.");
      }
    } else {
      checkMessage.setCost(cost);
    }

    if (getContext().peekSourceCode() instanceof SourceFile) {
      getContext().peekSourceCode().log(checkMessage);
    } else if (getContext().peekSourceCode().getParent(SourceFile.class) != null) {
      getContext().peekSourceCode().getParent(SourceFile.class).log(checkMessage);
    } else {
      throw new IllegalStateException("Unable to log a check message on source code '"
        + (getContext().peekSourceCode() == null ? "[NULL]" : getContext().peekSourceCode().getKey()) + "'");
    }
  }

}
