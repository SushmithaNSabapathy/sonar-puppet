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
package org.sonar.puppet.metrics;

import com.google.common.collect.ImmutableList;
import org.sonar.puppet.tree.SyntaxToken;
import org.sonar.puppet.tree.SyntaxTrivia;
import org.sonar.puppet.tree.Tree;
import org.sonar.plugins.puppet.api.visitors.SubscriptionVisitor;
import org.sonar.squidbridge.CommentAnalyser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CommentLinesVisitor extends SubscriptionVisitor {

  private final Set<Integer> commentLines = new HashSet<>();
  private final Set<Integer> noSonarLines = new HashSet<>();
  private final CommentAnalyser commentAnalyser;

  public CommentLinesVisitor(Tree tree, CommentAnalyser commentAnalyser) {
    this.commentAnalyser = commentAnalyser;
    commentLines.clear();
    noSonarLines.clear();
    scanTree(tree);
  }

  @Override
  public List<Tree.Kind> nodesToVisit() {
    return ImmutableList.of(Tree.Kind.TOKEN);
  }

  @Override
  public void visitNode(Tree tree) {
    for (SyntaxTrivia trivia : ((SyntaxToken) tree).trivias()) {
      if (!commentAnalyser.isBlank(commentAnalyser.getContents(trivia.text()))) {
        String[] comments = commentAnalyser.getContents(trivia.text()).split("(\r)?\n|\r", -1);
        int lineNumber = trivia.line();
        for (String commentLine : comments) {
          if (commentLine.contains("NOSONAR")) {
            noSonarLines.add(lineNumber);
          } else if (!commentAnalyser.isBlank(commentLine)) {
            this.commentLines.add(lineNumber);
          }
          lineNumber++;
        }
      }
    }
  }

  public int getNumberOfCommentLines() {
    return commentLines.size();
  }

  public Set<Integer> getNoSonarLines() {
    return noSonarLines;
  }

}
