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
package org.sonar.puppet.tree;

import org.sonar.plugins.puppet.api.visitors.DoubleDispatchVisitor;
import org.sonar.sslr.grammar.GrammarRuleKey;

import javax.annotation.Nullable;
import java.util.Iterator;

public interface Tree {

  boolean is(Kind... kind);

  void accept(DoubleDispatchVisitor visitor);

  Iterator<Tree> childrenIterator();

  String treeValue();

  int getLine();

  @Nullable
  Tree parent();

  void setParent(Tree parent);

  boolean hasAncestor(Class<? extends Tree> clazz);

  boolean isLeaf();

  enum Kind implements GrammarRuleKey {
    PROGRAM(ProgramTree.class),
    STATEMENT_BLOCK(StatementBlockTree.class),
    STATEMENT(StatementTree.class),
    NODE(NodeTree.class),
    NODE_NAME(NodeNameTree.class),
    NAME(NameTree.class),
    COMMA_DELIMITER(CommaDelimiterTree.class),
    IF_ELSIF_ELSE(IfElsifElseStatementTree.class),
    IF(IfStatementTree.class),
    ELSIF(ElsifStatementTree.class),
    ELSE(ElseStatementTree.class),
    EXPRESSION(ExpressionTree.class),
    HASH_PAIR(HashPairTree.class),
    VARIABLE(VariableTree.class),
    REGEX(RegexTree.class),
    STRING(StringTree.class),
    NUMBER(NumberTree.class),
    DECIMAL(DecimalTree.class),
    HEXADECIMAL(HexadecimalTree.class),
    OCTAL(OctalTree.class),
    TOKEN(SyntaxToken.class),
    TRIVIA(SyntaxTrivia.class),
    SPACING(SyntaxSpacing.class);

    final Class<? extends Tree> associatedInterface;

    Kind(Class<? extends Tree> associatedInterface) {
      this.associatedInterface = associatedInterface;
    }

    public Class<? extends Tree> getAssociatedInterface() {
      return associatedInterface;
    }
  }

}
