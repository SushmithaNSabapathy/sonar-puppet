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

import org.sonar.sslr.grammar.GrammarRuleKey;


// See grammar definition by Puppet Labs: https://github.com/puppetlabs/puppet/blob/3.x/lib/puppet/parser/grammar.ra


public enum PuppetGrammar implements GrammarRuleKey {

//  QUOTED_TEXT,
//  KEYWORD,
//
//  PARAMS,
//  PARAM,
//  PARAM_NAME,
//  ADD_PARAM,
//  ANY_PARAMS,
//  ANY_PARAM,
//
//  TYPE,
//
//  EXPRESSION,
//  EXPRESSIONS,
//  BOOL_EXPRESSION,
//  MATCH_EXPRESSION,
//  UNARY_NOT_EXPRESSION,
//  UNARY_NEG_EXPRESSION,
//  IN_EXPRESSION,
//
//  ATOM,
//
//  SHIFT_EXPRESSION,
//  ADDITIVE_EXPRESSION,
//  MULTIPLICATIVE_EXPRESSION,
//
//  COMPARISON,
//
//  ARITH_OP,
//  ASSIGNMENT,
//  APPENDS_STMT,
//  BOOL_OPERATOR,
//  COMP_OPERATOR,
//  A_OPER,
//  M_OPER,
//  MATCH_OPERATOR,
//
//  SHIFT_OPER,
//
//  RIGHT_VALUE,
//  FUNCVALUES,
//  FUNCRVALUE,
//
//  NUMBER,
//
//  // SIMPLE STATEMENTS
//  SIMPLE_STMT,
//  STATEMENT,
//  RESOURCE,
//  RESOURCE_NAME,
//  RESOURCE_INST,
//  RESOURCE_INSTANCES,
//  RESOURCE_OVERRIDE,
//
//  EXPORTED_RESOURCE,
//  VIRTUAL_RESOURCE,
//
//  RESOURCE_REF,
//  REQUIRE_STMT,
//  DEFINITION,
//  COLLECTION,
//  UNLESS_STMT,
//  IMPORT_STMT,
//  FUNCTION_STMT,
//  ARGUMENT_LIST,
//  ARGUMENTS,
//  ARGUMENT,
//  ARRAY,
//  HASH,
//  HASH_PAIRS,
//  HASH_PAIR,
//  KEY,
//
//  RELATIONSHIP,
//  RELATIONSHIP_SIDE,
//  EDGE,
//
//  HASH_ARRAY_ACCESS,
//  HASH_ARRAY_ACCESSES,
//
//  COLLECTOR_VAL,
//  COLLECTOR,
//  COLLECT_EXPR,
//  COLLECT_JOIN,
//  COLLECT_STMT,
//  COLLECT_STMTS,
//
//  NODE_DEFINITION,
//  HOST_MATCHES,
//  HOST_MATCH,
//
//  // Compound statements
//  COMPOUND_STMT,
//  CLASSDEF,
//  CLASSNAME,
//  CLASSNAME_OR_DEFAULT,
//  CLASS_PARENT,
//  IF_STMT,
//  ELSIF_STMT,
//  ELSE_STMT,
//  CASE_STMT,
//  CASE_MATCHER,
//  CASE_VALUES,
//
//  SELECTOR,
//  SVALUES,
//  SINTVALUES,
//  SELECTVAL,
//  SELECTLHAND,
//
//  // Top-level components
//  FILE_INPUT;
//
//
//    b.rule(STATEMENT).is(b.firstOf(SIMPLE_STMT, COMPOUND_STMT, EXPRESSION));
//
//  public static void grammar(LexerfulGrammarBuilder b) {
//
//    b.rule(FUNCTION_STMT).is(
//      b.firstOf(
//        b.sequence(NAME, PuppetPunctuator.LPAREN, EXPRESSIONS, b.optional(PuppetPunctuator.COMMA), PuppetPunctuator.RPAREN),
//        b.sequence(NAME, PuppetPunctuator.LPAREN, PuppetPunctuator.RPAREN),
//        b.sequence(NAME, FUNCVALUES)
//        ));
//
//    b.rule(FUNCVALUES).is(
//      RIGHT_VALUE,
//      b.zeroOrMore(PuppetPunctuator.COMMA, RIGHT_VALUE));
//
//    b.rule(FUNCRVALUE).is(
//      NAME,
//      PuppetPunctuator.LPAREN,
//      b.optional(EXPRESSIONS),
//      PuppetPunctuator.RPAREN
//      );
//
//    b.rule(PARAM_NAME).is(b.firstOf(KEYWORD, NAME, PuppetKeyword.TRUE, PuppetKeyword.FALSE));
//
//    b.rule(PARAM).is(
//      PARAM_NAME,
//      PuppetPunctuator.FARROW,
//      EXPRESSION);
//
//    b.rule(PARAMS).is(b.optional(
//      PARAM,
//      b.zeroOrMore(PuppetPunctuator.COMMA, PARAM)),
//      b.optional(PuppetPunctuator.COMMA));
//
//    b.rule(ADD_PARAM).is(NAME, PuppetPunctuator.PARROW, EXPRESSION);
//
//    b.rule(ANY_PARAM).is(b.firstOf(PARAM, ADD_PARAM)).skip();
//
//    b.rule(ANY_PARAMS).is(b.optional(
//      ANY_PARAM,
//      b.zeroOrMore(PuppetPunctuator.COMMA, ANY_PARAM)),
//      b.optional(PuppetPunctuator.COMMA));
//
//    b.rule(RESOURCE).is(b.firstOf(
//      b.sequence(CLASSNAME, PuppetPunctuator.LBRACE, RESOURCE_INSTANCES, b.optional(PuppetPunctuator.SEMIC), PuppetPunctuator.RBRACE),
//      b.sequence(TYPE, PuppetPunctuator.LBRACE, PARAMS, PuppetPunctuator.RBRACE)));
//
//    b.rule(RESOURCE_INST).is(RESOURCE_NAME, PuppetPunctuator.COLON, PARAMS);
//
//    b.rule(RESOURCE_INSTANCES).is(
//      RESOURCE_INST,
//      b.zeroOrMore(PuppetPunctuator.SEMIC, RESOURCE_INST)).skip();
//
//    b.rule(RESOURCE_NAME).is(b.firstOf(
//      SELECTOR,
//      HASH_ARRAY_ACCESSES,
//      ARRAY,
//      PuppetKeyword.DEFAULT,
//      NAME,
//      QUOTED_TEXT,
//      VARIABLE,
//      TYPE));
//
//    b.rule(RESOURCE_OVERRIDE).is(
//      RESOURCE_REF,
//      PuppetPunctuator.LBRACE,
//      ANY_PARAMS,
//      PuppetPunctuator.RBRACE);
//
//
//    b.rule(TYPE).is(REF);
//
//    b.rule(KEYWORD).is(b.firstOf(
//      PuppetKeyword.AND,
//      PuppetKeyword.CASE,
//      PuppetKeyword.CLASS,
//      PuppetKeyword.DEFAULT,
//      PuppetKeyword.DEFINE,
//      PuppetKeyword.ELSE,
//      PuppetKeyword.ELSIF,
//      PuppetKeyword.IF,
//      PuppetKeyword.IN,
//      PuppetKeyword.IMPORT,
//      PuppetKeyword.INHERITS,
//      PuppetKeyword.NODE,
//      PuppetKeyword.OR,
//      PuppetKeyword.UNDEF,
//      PuppetKeyword.UNLESS));
//
//  }
//
//  /*
//   * Simple Statements
//   */
//  public static void simpleStatements(LexerfulGrammarBuilder b) {
//
//    b.rule(SIMPLE_STMT).is(b.firstOf(
//      NODE_DEFINITION,
//      RELATIONSHIP,
//      ASSIGNMENT,
//      APPENDS_STMT,
//      RESOURCE,
//      UNLESS_STMT,
//      IMPORT_STMT,
//      RESOURCE_OVERRIDE,
//      DEFINITION,
//      FUNCTION_STMT));
//
//    b.rule(ASSIGNMENT).is(
//      b.firstOf(HASH_ARRAY_ACCESS, VARIABLE),
//      PuppetPunctuator.EQUALS,
//      EXPRESSION);
//
//    b.rule(APPENDS_STMT).is(
//      VARIABLE,
//      PuppetPunctuator.APPENDS,
//      EXPRESSION);
//
//    b.rule(DEFINITION).is(PuppetKeyword.DEFINE,
//      CLASSNAME,
//      ARGUMENT_LIST,
//      PuppetPunctuator.LBRACE,
//      b.zeroOrMore(STATEMENT),
//      PuppetPunctuator.RBRACE);
//
//    b.rule(ARGUMENT_LIST).is(b.optional(b.firstOf(
//      b.sequence(PuppetPunctuator.LPAREN, PuppetPunctuator.RPAREN),
//      b.sequence(PuppetPunctuator.LPAREN, ARGUMENTS, PuppetPunctuator.RPAREN)
//      ))).skip();
//
//    b.rule(ARGUMENTS).is(
//      ARGUMENT,
//      b.zeroOrMore(PuppetPunctuator.COMMA, ARGUMENT),
//      b.optional(PuppetPunctuator.COMMA));
//
//    b.rule(ARGUMENT).is(b.firstOf(
//      b.sequence(VARIABLE, PuppetPunctuator.EQUALS, EXPRESSION),
//      VARIABLE
//      ));
//
//    b.rule(HASH).is(b.firstOf(
//      b.sequence(PuppetPunctuator.LBRACE, HASH_PAIRS, PuppetPunctuator.RBRACE),
//      b.sequence(PuppetPunctuator.LBRACE, PuppetPunctuator.RBRACE)));
//
//    b.rule(HASH_PAIRS).is(
//      HASH_PAIR,
//      b.zeroOrMore(PuppetPunctuator.COMMA, HASH_PAIR),
//      b.optional(PuppetPunctuator.COMMA));
//
//    b.rule(HASH_PAIR).is(KEY, PuppetPunctuator.FARROW, EXPRESSION);
//
//    b.rule(KEY).is(b.firstOf(NAME, STRING));
//
//    b.rule(ARRAY).is(b.firstOf(
//      b.sequence(PuppetPunctuator.LBRACK, EXPRESSIONS, b.optional(PuppetPunctuator.COMMA), PuppetPunctuator.RBRACK),
//      b.sequence(PuppetPunctuator.LBRACK, PuppetPunctuator.RBRACK)));
//
//    b.rule(RESOURCE_REF).is(
//      b.firstOf(NAME, TYPE), PuppetPunctuator.LBRACK, EXPRESSIONS, PuppetPunctuator.RBRACK);
//
//    b.rule(RELATIONSHIP).is(
//      RELATIONSHIP_SIDE,
//      EDGE,
//      RELATIONSHIP_SIDE,
//      b.zeroOrMore(
//        EDGE,
//        RELATIONSHIP_SIDE
//        )
//      );
//
//    b.rule(RELATIONSHIP_SIDE).is(b.firstOf(
//      RESOURCE,
//      RESOURCE_REF,
//      COLLECTION,
//      VARIABLE,
//      STRING,
//      SELECTOR,
//      CASE_STMT,
//      HASH_ARRAY_ACCESSES
//      ));
//
//    b.rule(EDGE).is(b.firstOf(PuppetPunctuator.IN_EDGE, PuppetPunctuator.OUT_EDGE, PuppetPunctuator.IN_EDGE_SUB, PuppetPunctuator.OUT_EDGE_SUB)).skip();
//
//    b.rule(HASH_ARRAY_ACCESS).is(VARIABLE, PuppetPunctuator.LBRACK, EXPRESSION, PuppetPunctuator.RBRACK);
//    b.rule(HASH_ARRAY_ACCESSES).is(HASH_ARRAY_ACCESS, b.zeroOrMore(PuppetPunctuator.LBRACK, EXPRESSION, PuppetPunctuator.RBRACK));
//
//
//    b.rule(UNLESS_STMT).is(PuppetKeyword.UNLESS, EXPRESSION, PuppetPunctuator.LBRACE, b.zeroOrMore(STATEMENT), PuppetPunctuator.RBRACE);
//
//    b.rule(IMPORT_STMT).is(PuppetKeyword.IMPORT, STRING, b.zeroOrMore(PuppetPunctuator.COMMA, STRING));
//  }
//
//  /*
//   * Compound Statements
//   */
//  public static void compoundStatements(LexerfulGrammarBuilder b) {
//    b.rule(COMPOUND_STMT).is(b.firstOf(
//      CLASSDEF,
//      IF_STMT,
//      CASE_STMT,
//      COLLECTION,
//      EXPORTED_RESOURCE,
//      VIRTUAL_RESOURCE));
//
//    b.rule(CLASSDEF).is(PuppetKeyword.CLASS,
//      CLASSNAME,
//      ARGUMENT_LIST,
//      b.optional(CLASS_PARENT),
//      PuppetPunctuator.LBRACE,
//      b.zeroOrMore(STATEMENT),
//      PuppetPunctuator.RBRACE);
//
//    b.rule(CLASSNAME).is(b.firstOf(NAME, PuppetKeyword.CLASS));
//
//    b.rule(CLASSNAME_OR_DEFAULT).is(b.firstOf(CLASSNAME, PuppetKeyword.DEFAULT));
//
//    b.rule(CLASS_PARENT).is(PuppetKeyword.INHERITS, CLASSNAME_OR_DEFAULT);
//
//
//    b.rule(CASE_STMT).is(PuppetKeyword.CASE, EXPRESSION, PuppetPunctuator.LBRACE,
//      b.oneOrMore(CASE_MATCHER),
//      PuppetPunctuator.RBRACE);
//    b.rule(CASE_MATCHER).is(CASE_VALUES, PuppetPunctuator.COLON, PuppetPunctuator.LBRACE, b.zeroOrMore(STATEMENT), PuppetPunctuator.RBRACE);
//    b.rule(CASE_VALUES).is(SELECTLHAND, b.zeroOrMore(PuppetPunctuator.COMMA, SELECTLHAND));
//
//    b.rule(EXPORTED_RESOURCE).is(PuppetPunctuator.AT, PuppetPunctuator.AT, RESOURCE);
//
//    b.rule(VIRTUAL_RESOURCE).is(PuppetPunctuator.AT, RESOURCE);
//
//    /*
//     * Collections
//     */
//    b.rule(COLLECTION).is(b.firstOf(
//      b.sequence(TYPE, COLLECTOR, PuppetPunctuator.LBRACE, ANY_PARAMS, PuppetPunctuator.RBRACE),
//      b.sequence(TYPE, COLLECTOR)
//      ));
//
//    b.rule(COLLECTOR).is(b.firstOf(
//      b.sequence(PuppetPunctuator.LCOLLECT, COLLECT_STMTS, PuppetPunctuator.RCOLLECT),
//      b.sequence(PuppetPunctuator.LLCOLLECT, COLLECT_STMTS, PuppetPunctuator.RRCOLLECT)
//      ));
//
//    b.rule(COLLECT_STMTS).is(
//      b.optional(COLLECT_STMT),
//      b.zeroOrMore(COLLECT_JOIN, COLLECT_STMT)
//      );
//
//    b.rule(COLLECT_STMT).is(b.firstOf(
//      COLLECT_EXPR,
//      b.sequence(PuppetPunctuator.LPAREN, COLLECT_STMTS, PuppetPunctuator.RBRACE)
//      ));
//
//    b.rule(COLLECT_EXPR).is(b.firstOf(
//      b.sequence(COLLECTOR_VAL, PuppetPunctuator.ISEQUAL, EXPRESSION),
//      b.sequence(COLLECTOR_VAL, PuppetPunctuator.NOTEQUAL, EXPRESSION)
//      ));
//
//    b.rule(COLLECT_JOIN).is(b.firstOf(PuppetKeyword.AND, PuppetKeyword.OR));
//
//    b.rule(COLLECTOR_VAL).is(b.firstOf(VARIABLE, NAME));
//
//    /*
//     * Selectors
//     */
//    b.rule(SELECTOR).is(SELECTLHAND, PuppetPunctuator.QMARK, SVALUES);
//
//    b.rule(SVALUES).is(b.firstOf(
//      SELECTVAL,
//      b.sequence(PuppetPunctuator.LBRACE, SINTVALUES, PuppetPunctuator.RBRACE)
//      )).skip();
//
//    b.rule(SINTVALUES).is(SELECTVAL, b.zeroOrMore(PuppetPunctuator.COMMA, SELECTVAL), b.optional(PuppetPunctuator.COMMA));
//
//    b.rule(SELECTVAL).is(SELECTLHAND, PuppetPunctuator.FARROW, RIGHT_VALUE);
//
//    b.rule(SELECTLHAND).is(b.firstOf(
//      FUNCRVALUE,
//      NUMBER,
//      NAME,
//      TYPE,
//      STRING,
//      HASH_ARRAY_ACCESS,
//      VARIABLE,
//      PuppetKeyword.TRUE, PuppetKeyword.FALSE,
//      PuppetKeyword.UNDEF,
//      PuppetKeyword.DEFAULT,
//      REGULAR_EXPRESSION_LITERAL
//      ));
//  }
//
//  /*
//   * Expressions
//   * https://docs.puppetlabs.com/puppet/latest/reference/lang_expressions.htmls
//   */
//  public static void expressions(LexerfulGrammarBuilder b) {
//
//    b.rule(EXPRESSION).is(b.firstOf(
//      BOOL_EXPRESSION,
//      // RIGHT_VALUE,
//      HASH));
//
//    b.rule(EXPRESSIONS).is(EXPRESSION, b.zeroOrMore(PuppetPunctuator.COMMA, EXPRESSION)).skip();
//
//    // https://docs.puppetlabs.com/puppet/latest/reference/lang_expressions.html#order-of-operations
//
//    b.rule(UNARY_NOT_EXPRESSION).is(b.optional(PuppetPunctuator.NOT), ATOM).skipIfOneChild();
//    b.rule(UNARY_NEG_EXPRESSION).is(b.optional(PuppetPunctuator.MINUS), UNARY_NOT_EXPRESSION).skipIfOneChild();
//
//    b.rule(IN_EXPRESSION).is(UNARY_NEG_EXPRESSION, b.zeroOrMore(PuppetKeyword.IN, UNARY_NEG_EXPRESSION)).skipIfOneChild();
//
//    b.rule(MATCH_EXPRESSION).is(IN_EXPRESSION, b.zeroOrMore(MATCH_OPERATOR, IN_EXPRESSION)).skipIfOneChild();
//    b.rule(MULTIPLICATIVE_EXPRESSION).is(MATCH_EXPRESSION, b.zeroOrMore(M_OPER, MATCH_EXPRESSION)).skipIfOneChild();
//    b.rule(ADDITIVE_EXPRESSION).is(MULTIPLICATIVE_EXPRESSION, b.zeroOrMore(A_OPER, MULTIPLICATIVE_EXPRESSION)).skipIfOneChild();
//    b.rule(SHIFT_EXPRESSION).is(ADDITIVE_EXPRESSION, b.zeroOrMore(SHIFT_OPER, ADDITIVE_EXPRESSION)).skipIfOneChild();
//    b.rule(COMPARISON).is(SHIFT_EXPRESSION, b.zeroOrMore(COMP_OPERATOR, SHIFT_EXPRESSION)).skipIfOneChild();
//    b.rule(BOOL_EXPRESSION).is(COMPARISON, b.zeroOrMore(BOOL_OPERATOR, COMPARISON)).skipIfOneChild();
//
//    b.rule(ATOM).is(b.firstOf(
//      b.sequence(PuppetPunctuator.LPAREN, BOOL_EXPRESSION, PuppetPunctuator.RPAREN),
//      RIGHT_VALUE,
//      REGULAR_EXPRESSION_LITERAL)).skip();
//
//    // <arithop> ::= "+" | "-" | "/" | "*" | "<<" | ">>"
//    b.rule(ARITH_OP).is(b.firstOf(
//      SHIFT_OPER,
//      A_OPER,
//      M_OPER));
//
//    b.rule(SHIFT_OPER).is(b.firstOf(
//      PuppetPunctuator.RSHIFT,
//      PuppetPunctuator.LSHIFT));
//
//    b.rule(A_OPER).is(b.firstOf(
//      PuppetPunctuator.PLUS,
//      PuppetPunctuator.MINUS));
//
//    b.rule(M_OPER).is(b.firstOf(
//      PuppetPunctuator.MUL,
//      PuppetPunctuator.DIV,
//      PuppetPunctuator.MODULO));
//
//    // <boolop> ::= "and" | "or"
//    b.rule(BOOL_OPERATOR).is(b.firstOf(
//      PuppetKeyword.AND,
//      PuppetKeyword.OR));
//
//    // <compop> ::= "==" | "!=" | ">" | ">=" | "<=" | "<"
//    b.rule(COMP_OPERATOR).is(b.firstOf(
//      PuppetPunctuator.ISEQUAL,
//      PuppetPunctuator.NOTEQUAL,
//      PuppetPunctuator.GREATERTHAN,
//      PuppetPunctuator.GREATEREQUAL,
//      PuppetPunctuator.LESSEQUAL,
//      PuppetPunctuator.LESSTHAN));
//
//    // <matchop> ::= "=~" | "!~"
//    b.rule(MATCH_OPERATOR).is(b.firstOf(
//      PuppetPunctuator.MATCH,
//      PuppetPunctuator.NOMATCH));
//
//    b.rule(RIGHT_VALUE).is(b.firstOf(
//      STRING,
//      SELECTOR,
//      FUNCRVALUE,
//      NUMBER,
//      NAME,
//      PuppetKeyword.TRUE, PuppetKeyword.FALSE,
//      HASH_ARRAY_ACCESSES,
//      VARIABLE,
//      ARRAY,
//      RESOURCE_REF,
//      TYPE,
//      PuppetKeyword.UNDEF)).skip();
//
// }
}
