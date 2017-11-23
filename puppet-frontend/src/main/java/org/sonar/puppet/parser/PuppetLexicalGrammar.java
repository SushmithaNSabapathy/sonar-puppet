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

import com.sonar.sslr.api.GenericTokenType;
import org.sonar.sslr.grammar.GrammarRuleKey;
import org.sonar.sslr.grammar.LexerlessGrammarBuilder;

public enum PuppetLexicalGrammar implements GrammarRuleKey {

  // High level nodes
  PROGRAM,

  STATEMENT_BLOCK,
  STATEMENT,
  IF_ELSIF_ELSE_STATEMENT,
  IF_STATEMENT,
  ELSIF_STATEMENT,
  ELSE_STATEMENT,

  EXPRESSION,

  HASH,
  HASH_PAIR,

  NODE,
  NODE_NAME,

  COMMA_DELIMITER,

  NUMBER,
  HEXADECIMAL,
  OCTAL,
  DECIMAL,

  STRING,
  REGEX,
  NAME,

  VARIABLE,

  // Tokens
  BOM,
  EOF,

  DIV,
  MUL,
  LBRACK,
  RBRACK,
  LBRACE,
  RBRACE,
  LPAREN,
  RPAREN,
  ISEQUAL,
  MATCH,
  FARROW,
  EQUALS,
  APPENDS,
  PARROW,
  PLUS,
  GREATEREQUAL,
  RSHIFT,
  GREATERTHAN,
  LESSEQUAL,
  LLCOLLECT,
  OUT_EDGE,
  OUT_EDGE_SUB,
  LCOLLECT,
  LSHIFT,
  LESSTHAN,
  NOMATCH,
  NOTEQUAL,
  NOT,
  RRCOLLECT,
  RCOLLECT,
  IN_EDGE,
  IN_EDGE_SUB,
  MINUS,
  COMMA,
  DOT,
  COLON,
  AT,
  SEMIC,
  QMARK,
  BACKSLASH,
  MODULO,
  PIPE,

  // Keywords
  AND,
  CASE,
  CLASS,
  DEFAULT,
  DEFINE,
  ELSE_KEYWORD,
  ELSIF_KEYWORD,
  FALSE,
  IF_KEYWORD,
  IN,
  IMPORT,
  INHERITS_KEYWORD,
  NODE_KEYWORD,
  OR,
  TRUE,
  UNDEF,
  UNLESS,

  // Literals
  HEXADECIMAL_LITERAL,
  OCTAL_LITERAL,
  DECIMAL_LITERAL,

  SINGLE_QUOTED_STRING_LITERAL,
  DOUBLE_QUOTED_STRING_LITERAL,

  REGEX_LITERAL,

  NAME_LITERAL,

  REF,
  VARIABLE_LITERAL,

  // Spacing
  SPACING;

  public static LexerlessGrammarBuilder createGrammar() {
    LexerlessGrammarBuilder b = LexerlessGrammarBuilder.create();
    spacing(b);
    tokens(b);
    keywords(b);
    types(b);
    b.setRootRule(PROGRAM);
    return b;
  }

  private static void tokens(LexerlessGrammarBuilder b) {
    b.rule(BOM).is("\ufeff");
    b.rule(EOF).is(SPACING, b.token(GenericTokenType.EOF, b.endOfInput()));

    b.rule(DIV).is(SPACING, "/");
    b.rule(MUL).is(SPACING, "*");
    b.rule(LBRACK).is(SPACING, "[");
    b.rule(RBRACK).is(SPACING, "]");
    b.rule(LBRACE).is(SPACING, "{");
    b.rule(RBRACE).is(SPACING, "}");
    b.rule(LPAREN).is(SPACING, "(");
    b.rule(RPAREN).is(SPACING, ")");
    b.rule(ISEQUAL).is(SPACING, "==");
    b.rule(MATCH).is(SPACING, "=~");
    b.rule(FARROW).is(SPACING, "=>");
    b.rule(EQUALS).is(SPACING, "=");
    b.rule(APPENDS).is(SPACING, "+=");
    b.rule(PARROW).is(SPACING, "+>");
    b.rule(PLUS).is(SPACING, "+");
    b.rule(GREATEREQUAL).is(SPACING, ">=");
    b.rule(RSHIFT).is(SPACING, ">>");
    b.rule(GREATERTHAN).is(SPACING, ">");
    b.rule(LESSEQUAL).is(SPACING, "<=");
    b.rule(LLCOLLECT).is(SPACING, "<<|");
    b.rule(OUT_EDGE).is(SPACING, "<-");
    b.rule(OUT_EDGE_SUB).is(SPACING, "<~");
    b.rule(LCOLLECT).is(SPACING, "<|");
    b.rule(LSHIFT).is(SPACING, "<<");
    b.rule(LESSTHAN).is(SPACING, "<");
    b.rule(NOMATCH).is(SPACING, "!~");
    b.rule(NOTEQUAL).is(SPACING, "!=");
    b.rule(NOT).is(SPACING, "!");
    b.rule(RRCOLLECT).is(SPACING, "|>>");
    b.rule(RCOLLECT).is(SPACING, "|>");
    b.rule(IN_EDGE).is(SPACING, "->");
    b.rule(IN_EDGE_SUB).is(SPACING, "~>");
    b.rule(MINUS).is(SPACING, "-");
    b.rule(COMMA).is(SPACING, ",");
    b.rule(DOT).is(SPACING, ".");
    b.rule(COLON).is(SPACING, ":");
    b.rule(AT).is(SPACING, "@");
    b.rule(SEMIC).is(SPACING, ";");
    b.rule(QMARK).is(SPACING, "?");
    b.rule(BACKSLASH).is(SPACING, "\\");
    b.rule(MODULO).is(SPACING, "%");
    b.rule(PIPE).is(SPACING, "|");
  }

  private static void keywords(LexerlessGrammarBuilder b) {
    b.rule(AND).is(SPACING, "and");
    b.rule(CASE).is(SPACING, "case");
    b.rule(CLASS).is(SPACING, "class");
    b.rule(DEFAULT).is(SPACING, "default");
    b.rule(DEFINE).is(SPACING, "define");
    b.rule(ELSE_KEYWORD).is(SPACING, "else");
    b.rule(ELSIF_KEYWORD).is(SPACING, "elsif");
    b.rule(FALSE).is(SPACING, "false");
    b.rule(IF_KEYWORD).is(SPACING, "if");
    b.rule(IMPORT).is(SPACING, "import");
    b.rule(IN).is(SPACING, "in");
    b.rule(INHERITS_KEYWORD).is(SPACING, "inherits");
    b.rule(NODE_KEYWORD).is(SPACING, "node");
    b.rule(OR).is(SPACING, "or");
    b.rule(TRUE).is(SPACING, "true");
    b.rule(UNDEF).is(SPACING, "undef");
    b.rule(UNLESS).is(SPACING, "unless");
  }

  private static void types(LexerlessGrammarBuilder b) {
    b.rule(HEXADECIMAL_LITERAL).is(SPACING, b.regexp("0[xX][0-9a-fA-F]+"));
    b.rule(OCTAL_LITERAL).is(SPACING, b.regexp("0[0-7]+"));
    b.rule(DECIMAL_LITERAL).is(SPACING, b.regexp("\\d+(\\.\\d+)?([eE]-?\\d+)?")); // + is not yet allowed after [eE]

    b.rule(DOUBLE_QUOTED_STRING_LITERAL).is(SPACING, b.regexp("\"([^\"\\\\]*+(\\\\[\\s\\S])?+)*+\""));
    b.rule(SINGLE_QUOTED_STRING_LITERAL).is(SPACING, b.regexp("'([^'\\\\]*+(\\\\[\\s\\S])?+)*+'"));

    b.rule(REGEX_LITERAL).is(SPACING, b.regexp("/([^/]|(?<=\\\\\\\\)/)*/"));

    b.rule(NAME_LITERAL).is(SPACING, b.regexp("((::)?[a-z0-9][-\\\\w]*)(::[a-z0-9][-\\\\w]*)*"));

    b.rule(REF).is(SPACING, b.regexp("(::)?[A-Z]\\w*(::[A-Z]\\w*)*"));
    b.rule(VARIABLE_LITERAL).is(SPACING, b.regexp("\\$(::)?(\\w+::)*\\w+"));
  }

  private static void spacing(LexerlessGrammarBuilder b) {

    String hashLineComment = "#[^\\n\\r]*+";
    String slashLineComment = "//[^\\n\\r]*+";
    String multiLineComment = "/\\*[\\s\\S]*?\\*/";
    String comment = "(?:" + hashLineComment + "|" + slashLineComment + "|" + multiLineComment + ")";

    b.rule(SPACING).is(
      b.skippedTrivia(b.regexp("(?<!\\\\)[\\s]*+")),
      b.zeroOrMore(
        b.commentTrivia(b.regexp(comment)),
        b.skippedTrivia(b.regexp("(?<!\\\\)[\\s]*+"))));
  }

}
