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
package org.sonar.puppet.parser;

import com.google.common.collect.Lists;
import com.sonar.sslr.api.typed.Optional;
import org.sonar.puppet.tree.*;
import org.sonar.puppet.tree.impl.*;

import java.util.List;

public class TreeFactory {

  public ProgramTree program(Optional<SyntaxToken> byteOrderMark, Optional<List<StatementTree>> statements, SyntaxToken eof) {
    return new ProgramTreeImpl(byteOrderMark.orNull(), statements.orNull(), eof);
  }

  public StatementBlockTree statementBlock(SyntaxToken openingCurlyBrace, Optional<List<StatementTree>> statements, SyntaxToken closingCurlyBrace) {
    return new StatementBlockTreeImpl(openingCurlyBrace, statements.orNull(), closingCurlyBrace);
  }

  public StatementTree statement(Tree statement) {
    return new StatementTreeImpl(statement);
  }

  public SeparatedList<NodeNameTree, CommaDelimiterTree> nodeNameList(NodeNameTree nodeName, Optional<List<Tuple<CommaDelimiterTree, NodeNameTree>>> subsequentSelectors, Optional<CommaDelimiterTree> trailingComma) {
    List<NodeNameTree> nodeNames = Lists.newArrayList(nodeName);
    List<CommaDelimiterTree> commas = Lists.newArrayList();

    if (subsequentSelectors.isPresent()) {
      for (Tuple<CommaDelimiterTree, NodeNameTree> t : subsequentSelectors.get()) {
        commas.add(t.first());
        nodeNames.add(t.second());
      }
    }

    if (trailingComma.isPresent()) {
      commas.add(trailingComma.get());
    }

    return new SeparatedList<>(nodeNames, commas);
  }

  public NodeTree node(SyntaxToken nodeKeyword, SeparatedList<NodeNameTree, CommaDelimiterTree> nodeNames, Optional<SyntaxToken> inheritsKeyword, Optional<NodeNameTree> parentNodeName, StatementBlockTree statementBlock) {
    return new NodeTreeImpl(nodeKeyword, nodeNames, inheritsKeyword.orNull(), parentNodeName.orNull(), statementBlock);
  }

  public NodeNameTree nodeName(Tree nodeName) {
    return new NodeNameTreeImpl(nodeName);
  }

  public NameTree name(SyntaxToken name) {
    return new NameTreeImpl(name);
  }

  public CommaDelimiterTree commaDelimiter(SyntaxToken comma) {
    return new CommaDelimiterTreeImpl(comma);
  }

  public IfElsifElseStatementTree ifElsifElseStatement(IfStatementTree ifStatement, Optional<List<ElsifStatementTree>> elsifStatements, Optional<ElseStatementTree> elseStatement) {
    return new IfElsifElseStatementTreeImpl(ifStatement, elsifStatements.orNull(), elseStatement.orNull());
  }

  public IfStatementTree ifStatement(SyntaxToken ifKeyword, ExpressionTree condition, StatementBlockTree statementBlock) {
    return new IfStatementTreeImpl(ifKeyword, condition, statementBlock);
  }

  public ElsifStatementTree elsifStatement(SyntaxToken elsifKeyword, ExpressionTree condition, StatementBlockTree statementBlock) {
    return new ElsifStatementTreeImpl(elsifKeyword, condition, statementBlock);
  }

  public ElseStatementTree elseStatement(SyntaxToken elseKeyword, StatementBlockTree statementBlock) {
    return new ElseStatementTreeImpl(elseKeyword, statementBlock);
  }

  public ExpressionTree expression(Tree expression) {
    return new ExpressionTreeImpl(expression);
  }

  public HashPairTree hashPair(Tree key, SyntaxToken farrow, ExpressionTree value) {
    return new HashPairTreeImpl(key, farrow, value);
  }

  public VariableTree variable(SyntaxToken variable) {
    return new VariableTreeImpl(variable);
  }

  public RegexTree regex(SyntaxToken regex) {
    return new RegexTreeImpl(regex);
  }

  public StringTree string(SyntaxToken string) {
    return new StringTreeImpl(string);
  }

  public NumberTree number(Tree number) {
    return new NumberTreeImpl(number);
  }

  public DecimalTree decimal(SyntaxToken floatNumber) {
    return new DecimalTreeImpl(floatNumber);
  }

  public HexadecimalTree hexadecimal(SyntaxToken hexadecimal) {
    return new HexadecimalTreeImpl(hexadecimal);
  }

  public OctalTree octal(SyntaxToken octal) {
    return new OctalTreeImpl(octal);
  }

  // ---------------------------------
  // Tuple
  // ---------------------------------

  public static class Tuple<T, U> {

    private final T first;
    private final U second;

    public Tuple(T first, U second) {
      super();

      this.first = first;
      this.second = second;
    }

    public T first() {
      return first;
    }

    public U second() {
      return second;
    }
  }

  private static <T, U> Tuple<T, U> newTuple(T first, U second) {
    return new Tuple<>(first, second);
  }

  public <T, U> Tuple<T, U> newTuple1(T first, U second) {
    return newTuple(first, second);
  }

}
