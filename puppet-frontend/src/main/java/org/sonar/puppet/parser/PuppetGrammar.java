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

import com.sonar.sslr.api.typed.GrammarBuilder;
import org.sonar.puppet.tree.*;
import org.sonar.puppet.tree.impl.InternalSyntaxToken;
import org.sonar.puppet.tree.impl.SeparatedList;

public class PuppetGrammar {

  private final GrammarBuilder<InternalSyntaxToken> b;
  private final TreeFactory f;

  public PuppetGrammar(GrammarBuilder<InternalSyntaxToken> b, TreeFactory f) {
    this.b = b;
    this.f = f;
  }

  public ProgramTree PROGRAM() {
    return b.<ProgramTree>nonterminal(PuppetLexicalGrammar.PROGRAM).is(
      f.program(
        b.optional(b.token(PuppetLexicalGrammar.BOM)),
        b.zeroOrMore(STATEMENT()),
        b.token(PuppetLexicalGrammar.EOF)));
  }

  public StatementBlockTree STATEMENT_BLOCK() {
    return b.<StatementBlockTree>nonterminal(PuppetLexicalGrammar.STATEMENT_BLOCK).is(
      f.statementBlock(
        b.token(PuppetLexicalGrammar.LBRACE),
        b.zeroOrMore(STATEMENT()),
        b.token(PuppetLexicalGrammar.RBRACE)));
  }

  public StatementTree STATEMENT() {
    return b.<StatementTree>nonterminal(PuppetLexicalGrammar.STATEMENT).is(
      f.statement(
        b.firstOf(

        )));
  }

  public NodeTree NODE() {
    return b.<NodeTree>nonterminal(PuppetLexicalGrammar.NODE).is(
      f.node(
        b.token(PuppetLexicalGrammar.NODE_KEYWORD),
        NODE_NAME_LIST(),
        b.optional(b.token(PuppetLexicalGrammar.INHERITS_KEYWORD)),
        b.optional(NODE_NAME()),
        STATEMENT_BLOCK()));
  }

  public SeparatedList<NodeNameTree, CommaDelimiterTree> NODE_NAME_LIST() {
    return b.<SeparatedList<NodeNameTree, CommaDelimiterTree>>nonterminal().is(
      f.nodeNameList(
        NODE_NAME(),
        b.zeroOrMore(
          f.newTuple1(
            COMMA_DELIMITER(),
            NODE_NAME())),
        b.optional(COMMA_DELIMITER())));
  }

  public NodeNameTree NODE_NAME() {
    return b.<NodeNameTree>nonterminal(PuppetLexicalGrammar.NODE_NAME).is(
      f.nodeName(
        b.firstOf(
          b.token(PuppetLexicalGrammar.DEFAULT),
          REGEX(),
          STRING(),
          NAME())));
  }

  public NameTree NAME() {
    return b.<NameTree>nonterminal(PuppetLexicalGrammar.NAME).is(
      f.name(b.token(PuppetLexicalGrammar.NAME_LITERAL)));
  }

  public CommaDelimiterTree COMMA_DELIMITER() {
    return b.<CommaDelimiterTree>nonterminal(PuppetLexicalGrammar.COMMA_DELIMITER).is(
      f.commaDelimiter(b.token(PuppetLexicalGrammar.COMMA)));
  }

  public IfElsifElseStatementTree IF_ELSIF_ELSE_STATEMENT() {
    return b.<IfElsifElseStatementTree>nonterminal(PuppetLexicalGrammar.IF_ELSIF_ELSE_STATEMENT).is(
      f.ifElsifElseStatement(
        IF_STATEMENT(),
        b.zeroOrMore(ELSIF_STATEMENT()),
        b.optional(ELSE_STATEMENT())));
  }

  public IfStatementTree IF_STATEMENT() {
    return b.<IfStatementTree>nonterminal(PuppetLexicalGrammar.IF_STATEMENT).is(
      f.ifStatement(
        b.token(PuppetLexicalGrammar.IF_KEYWORD),
        EXPRESSION(),
        STATEMENT_BLOCK()));
  }

  public ElsifStatementTree ELSIF_STATEMENT() {
    return b.<ElsifStatementTree>nonterminal(PuppetLexicalGrammar.ELSIF_STATEMENT).is(
      f.elsifStatement(
        b.token(PuppetLexicalGrammar.ELSIF_KEYWORD),
        EXPRESSION(),
        STATEMENT_BLOCK()));
  }

  public ElseStatementTree ELSE_STATEMENT() {
    return b.<ElseStatementTree>nonterminal(PuppetLexicalGrammar.ELSE_STATEMENT).is(
      f.elseStatement(
        b.token(PuppetLexicalGrammar.ELSE_KEYWORD),
        STATEMENT_BLOCK()));
  }

  public ExpressionTree EXPRESSION() {
    return b.<ExpressionTree>nonterminal(PuppetLexicalGrammar.EXPRESSION).is(
      f.expression(
        b.token(PuppetLexicalGrammar.ELSE_KEYWORD)));
  }

  public HashPairTree HASH_PAIR() {
    return b.<HashPairTree>nonterminal(PuppetLexicalGrammar.HASH_PAIR).is(
      f.hashPair(
        b.firstOf(
          STRING(),
          NAME()),
        b.token(PuppetLexicalGrammar.FARROW),
        EXPRESSION()));
  }

  public RegexTree REGEX() {
    return b.<RegexTree>nonterminal(PuppetLexicalGrammar.REGEX).is(
      f.regex(b.token(PuppetLexicalGrammar.REGEX_LITERAL)));
  }

  public VariableTree VARIABLE() {
    return b.<VariableTree>nonterminal(PuppetLexicalGrammar.VARIABLE).is(
      f.variable(b.token(PuppetLexicalGrammar.VARIABLE_LITERAL)));
  }

  public StringTree STRING() {
    return b.<StringTree>nonterminal(PuppetLexicalGrammar.STRING).is(
      f.string(
        b.firstOf(
          b.token(PuppetLexicalGrammar.DOUBLE_QUOTED_STRING_LITERAL),
          b.token(PuppetLexicalGrammar.SINGLE_QUOTED_STRING_LITERAL))));
  }

  public NumberTree NUMBER() {
    return b.<NumberTree>nonterminal(PuppetLexicalGrammar.NUMBER).is(
      f.number(
        b.firstOf(
          HEXADECIMAL(),
          OCTAL(),
          DECIMAL()
        )));
  }

  public DecimalTree DECIMAL() {
    return b.<DecimalTree>nonterminal(PuppetLexicalGrammar.DECIMAL).is(
      f.decimal(b.token(PuppetLexicalGrammar.DECIMAL_LITERAL)));
  }

  public HexadecimalTree HEXADECIMAL() {
    return b.<HexadecimalTree>nonterminal(PuppetLexicalGrammar.HEXADECIMAL).is(
      f.hexadecimal(b.token(PuppetLexicalGrammar.HEXADECIMAL_LITERAL)));
  }

  public OctalTree OCTAL() {
    return b.<OctalTree>nonterminal(PuppetLexicalGrammar.OCTAL).is(
      f.octal(b.token(PuppetLexicalGrammar.OCTAL_LITERAL)));
  }

}
