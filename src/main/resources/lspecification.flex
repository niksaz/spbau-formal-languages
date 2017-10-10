package ru.spbau.sazanovich.nikita.lexer;

import ru.spbau.sazanovich.nikita.lexer.lexem.*;
import ru.spbau.sazanovich.nikita.lexer.lexem.LKeyword.LKeywordType;
import ru.spbau.sazanovich.nikita.lexer.lexem.LOperator.LOperatorType;

/**
 * Lexer for L language.
 */
%%

%class LLexer
%unicode
%line
%column
%function advance
%type LToken

%{
  private LIdentifier lIdentifier() {
    return new LIdentifier(yytext(), yyline, yycolumn, yycolumn + yylength());
  }

  private LKeyword lKeyword(LKeywordType keywordType) {
    return new LKeyword(keywordType, yyline, yycolumn, yycolumn + yylength());
  }

  private LBooleanLiteral lBooleanLiteral(boolean value) {
    return new LBooleanLiteral(value, yyline, yycolumn, yycolumn + yylength());
  }

  private LIntegerLiteral lIntegerLiteral() {
    // TODO: Handle NumberFormatException properly.
    int value = Integer.parseInt(yytext());
    return new LIntegerLiteral(value, yyline, yycolumn, yycolumn + yylength());
  }

  private LFloatingPointLiteral lFloatingPointLiteral() {
    // TODO: Handle NumberFormatException properly.
    float value = Float.parseFloat(yytext());
    return new LFloatingPointLiteral(value, yyline, yycolumn, yycolumn + yylength());
  }

  private LOperator lOperator(LOperatorType operatorType) {
    return new LOperator(operatorType, yyline, yycolumn, yycolumn + yylength());
  }

  private LSemicolon lSemicolon() {
    return new LSemicolon(yyline, yycolumn, yycolumn + yylength());
  }

  private LLeftBrace lLeftBrace() {
    return new LLeftBrace(yyline, yycolumn, yycolumn + yylength());
  }

  private LRightBrace lRightBrace() {
    return new LRightBrace(yyline, yycolumn, yycolumn + yylength());
  }

  private LComment lComment() {
    // Ommiting two slashes in the beginning.
    String text = yytext().subSequence(2, yytext().length()).toString();
    return new LComment(text, yyline, yycolumn, yycolumn + yylength());
  }
%}

LineTerminator  = \r|\n|\r\n
InputCharacter  = [^\r\n]
WhiteSpace      = {LineTerminator} | [ \t\f]

EndOfLineComment = "//" {InputCharacter}*

Identifier = ("_" | [a-z]) ("_" | [a-z] | [0-9])*

/* Integer literals */
IntegerLiteral = {DecimalNumeral}

DecimalNumeral = "0" | {NonZeroDigit} ({Underscores} {Digits})?

Digits = {Digit} ({Underscores} {Digit})*

Digit = "0" | {NonZeroDigit}

NonZeroDigit = "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"

Underscores = "_"*

/* Floating-point literals */
FloatingPointLiteral = {DecimalFloatingPointLiteral}

DecimalFloatingPointLiteral =
      {Digits} "." {Digits}?
    | "." {Digits} {ExponentPart}?
    | {Digits} {ExponentPart}
    | {Digits} {ExponentPart}?

ExponentPart = {ExponentIndicator} {SignedInteger}

ExponentIndicator = "e" | "E"

SignedInteger = {Sign}? {Digits}

Sign = "+" | "-"

%%

<YYINITIAL> {
    /* Keywords */
    "if"                    { return lKeyword(LKeywordType.IF); }
    "then"                  { return lKeyword(LKeywordType.THEN); }
    "else"                  { return lKeyword(LKeywordType.ELSE); }
    "while"                 { return lKeyword(LKeywordType.WHILE); }
    "do"                    { return lKeyword(LKeywordType.DO); }
    "read"                  { return lKeyword(LKeywordType.READ); }
    "write"                 { return lKeyword(LKeywordType.WRITE); }
    "begin"                 { return lKeyword(LKeywordType.BEGIN); }
    "end"                   { return lKeyword(LKeywordType.END); }

    /* Literals */
    "true"                  { return lBooleanLiteral(true); }
    "false"                 { return lBooleanLiteral(false); }
    {IntegerLiteral}        { return lIntegerLiteral(); }
    {FloatingPointLiteral}  { return lFloatingPointLiteral(); }

    /* Identifiers */
    {Identifier}            { return lIdentifier(); }

    /* Operators */
    "+"                     { return lOperator(LOperatorType.PLUS); }
    "-"                     { return lOperator(LOperatorType.MINUS); }
    "∗"                     { return lOperator(LOperatorType.ASTERISK); }
    "/"                     { return lOperator(LOperatorType.SLASH); }
    "%"                     { return lOperator(LOperatorType.PERCENT); }
    "=="                    { return lOperator(LOperatorType.EQUAL); }
    "!="                    { return lOperator(LOperatorType.NOT_EQUAL); }
    ">"                     { return lOperator(LOperatorType.MORE); }
    ">="                    { return lOperator(LOperatorType.MORE_OR_EQUAL); }
    "<"                     { return lOperator(LOperatorType.LESS); }
    "<="                    { return lOperator(LOperatorType.LESS_OR_EQUAL); }
    "&&"                    { return lOperator(LOperatorType.AND); }
    "||"                    { return lOperator(LOperatorType.OR); }

    /* Separators */
    ";"                     { return lSemicolon(); }
    "("                     { return lLeftBrace(); }
    ")"                     { return lRightBrace(); }

    /* Comments */
    {EndOfLineComment}      { return lComment(); }

    /* Whitespace */
    {WhiteSpace}            { /* Ignore whitespace. */ }
}

/* Error fallback */
[^]                         { //TODO: Create custom exception for that case.
                              throw new Error("Illegal character <" + yytext() + ">"); }

