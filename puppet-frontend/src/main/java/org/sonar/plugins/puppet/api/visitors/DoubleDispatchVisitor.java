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
package org.sonar.plugins.puppet.api.visitors;

import com.google.common.base.Preconditions;
import org.sonar.puppet.tree.*;
import org.sonar.puppet.tree.impl.PuppetTree;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

public abstract class DoubleDispatchVisitor implements TreeVisitor {

  private TreeVisitorContext context = null;

  @Override
  public TreeVisitorContext getContext() {
    Preconditions.checkState(context != null, "this#scanTree(context) should be called to initialised the context before accessing it");
    return context;
  }

  @Override
  public final void scanTree(TreeVisitorContext context) {
    this.context = context;
    scan(context.getTopTree());
  }

  protected void scan(@Nullable Tree tree) {
    if (tree != null) {
      tree.accept(this);
    }
  }

  protected void scanChildren(Tree tree) {
    Iterator<Tree> childrenIterator = ((PuppetTree) tree).childrenIterator();

    Tree child;

    while (childrenIterator.hasNext()) {
      child = childrenIterator.next();
      if (child != null) {
        child.accept(this);
      }
    }
  }

  protected <T extends Tree> void scan(List<T> trees) {
    trees.forEach(this::scan);
  }

  public void visitProgram(ProgramTree tree) {
    scanChildren(tree);
  }

  public void visitStatementBlock(StatementBlockTree tree) {
    scanChildren(tree);
  }

  public void visitStatement(StatementTree tree) {
    scanChildren(tree);
  }

  public void visitNode(NodeTree tree) {
    scanChildren(tree);
  }

  public void visitNodeName(NodeNameTree tree) {
    scanChildren(tree);
  }

  public void visitName(NameTree tree) {
    scanChildren(tree);
  }

  public void visitCommaDelimiter(CommaDelimiterTree tree) {
    scanChildren(tree);
  }

  public void visitIfElsifElseStatement(IfElsifElseStatementTree tree) {
    scanChildren(tree);
  }

  public void visitIfStatement(IfStatementTree tree) {
    scanChildren(tree);
  }

  public void visitElsifStatement(ElsifStatementTree tree) {
    scanChildren(tree);
  }

  public void visitElseStatement(ElseStatementTree tree) {
    scanChildren(tree);
  }

  public void visitHashPair(HashPairTree tree) {
    scanChildren(tree);
  }

  public void visitExpression(ExpressionTree tree) {
    scanChildren(tree);
  }

  public void visitVariable(VariableTree tree) {
    scanChildren(tree);
  }

  public void visitRegex(RegexTree tree) {
    scanChildren(tree);
  }

  public void visitString(StringTree tree) {
    scanChildren(tree);
  }

  public void visitNumber(NumberTree tree) {
    scanChildren(tree);
  }

  public void visitDecimal(DecimalTree tree) {
    scanChildren(tree);
  }

  public void visitHexadecimal(HexadecimalTree tree) {
    scanChildren(tree);
  }

  public void visitOctal(OctalTree tree) {
    scanChildren(tree);
  }

  public void visitToken(SyntaxToken token) {
    for (SyntaxTrivia syntaxTrivia : token.trivias()) {
      syntaxTrivia.accept(this);
    }
  }

  public void visitComment(SyntaxTrivia commentToken) {
    // No sub-tree
  }

}
