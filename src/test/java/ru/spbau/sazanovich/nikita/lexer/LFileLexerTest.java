package ru.spbau.sazanovich.nikita.lexer;

import static com.google.common.truth.Truth.assertThat;
import static ru.spbau.sazanovich.nikita.lexer.lexem.LKeyword.LKeywordType.*;
import static ru.spbau.sazanovich.nikita.lexer.lexem.LOperator.LOperatorType.*;

import org.junit.Test;
import ru.spbau.sazanovich.nikita.lexer.lexem.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class LFileLexerTest {

  private static final String TEST_L_FILES_PATH = "src/test/resources/";

  @Test
  public void parseExample1() throws Exception {
    List<LToken> tokenList = getTokensForTestFile("example1.lang");
    assertTokens(tokenList,
        new LKeyword(READ, 0, 0, 4),
        new LIdentifier("x", 0, 5, 6),
        new LSemicolon(0, 6, 7),
        new LKeyword(IF, 0, 8, 10),
        new LIdentifier("y", 0, 11, 12),
        new LOperator(PLUS, 0, 13, 14),
        new LIntegerLiteral(1, 0, 15, 16),
        new LOperator(EQUAL, 0, 17, 19),
        new LIdentifier("x", 0, 20, 21),
        new LKeyword(THEN, 0, 22, 26),
        new LKeyword(WRITE, 0, 27, 32),
        new LIdentifier("y", 0, 33, 34),
        new LKeyword(ELSE, 0, 35, 39),
        new LKeyword(WRITE, 0, 40, 45),
        new LIdentifier("x", 0, 46, 47));
  }

  @Test
  public void parseExample2() throws Exception {
    List<LToken> tokenList = getTokensForTestFile("example2.lang");
    assertTokens(tokenList,
        new LKeyword(WHILE, 0, 0, 5),
        new LIdentifier("x", 0, 6, 7),
        new LOperator(MORE, 0, 8, 9),
        new LIntegerLiteral(9, 0, 10, 11),
        new LOperator(OR, 0, 12, 14),
        new LIdentifier("y", 0, 15, 16),
        new LOperator(MORE_OR_EQUAL, 0, 17, 19),
        new LIntegerLiteral(1, 0, 20, 21),
        new LOperator(AND, 0, 22, 24),
        new LIdentifier("z", 0, 25, 26),
        new LOperator(LESS, 0, 28, 29),
        new LIntegerLiteral(1, 0, 30, 31),
        new LOperator(OR, 0, 32, 34),
        new LIdentifier("t", 0, 35, 36),
        new LOperator(LESS_OR_EQUAL, 0, 37, 39),
        new LIntegerLiteral(1, 0, 40, 41),
        new LKeyword(DO, 0, 42, 44),
        new LKeyword(BEGIN, 1, 0, 5),
        new LKeyword(IF, 2, 4, 6),
        new LLeftBrace(2, 7, 8),
        new LIdentifier("x", 2, 8, 9),
        new LOperator(PERCENT, 2, 10, 11),
        new LIdentifier("y", 2, 12, 13),
        new LOperator(NOT_EQUAL, 2, 14, 16),
        new LIntegerLiteral(0, 2, 17, 18),
        new LOperator(AND, 2, 19, 21),
        new LIdentifier("__should_log", 2, 22, 34),
        new LOperator(EQUAL, 2, 35, 37),
        new LBooleanLiteral(true, 2, 38, 42),
        new LRightBrace(2, 42, 43),
        new LKeyword(WRITE, 2, 44, 49),
        new LIdentifier("z", 2, 50, 51),
        new LSemicolon(2, 51, 52),
        new LKeyword(END, 3, 0, 3));
  }

  @Test
  public void parseExample3() throws Exception {
    List<LToken> tokenList = getTokensForTestFile("example3.lang");
    assertTokens(tokenList,
        new LLeftBrace(0, 0, 1),
        new LFloatingPointLiteral(987.0f, 0, 1, 5),
        new LOperator(MINUS, 0, 6, 7),
        new LIntegerLiteral(5, 0, 8, 9),
        new LRightBrace(0, 9, 10),
        new LOperator(MORE, 0, 11, 12),
        new LLeftBrace(0, 13, 14),
        new LFloatingPointLiteral(98.0f, 0, 14, 19),
        new LOperator(ASTERISK, 0, 20, 21),
        new LFloatingPointLiteral(1.0f, 0, 23, 26),
        new LOperator(SLASH, 0, 27, 28),
        new LIntegerLiteral(2, 0, 29, 30),
        new LRightBrace(0, 30, 31),
        new LOperator(EQUAL, 0, 32, 34),
        new LBooleanLiteral(false, 0, 35, 40),
        new LComment("It should be false for API method to work", 0, 41, 84),
        new LComment("TODO(niksaz) //Move it out of here", 1, 0, 36));
  }

  @Test(expected = LexerException.class)
  public void parseTooBigInteger() throws Exception {
    getTokensForTestFile("big_integer.lang");
  }

  @Test
  public void parseTooBigFloat() throws Exception {
    List<LToken> tokenList = getTokensForTestFile("big_float.lang");
    assertTokens(tokenList, new LFloatingPointLiteral(Float.POSITIVE_INFINITY, 0, 0, 12));
  }

  @Test(expected = LexerException.class)
  public void parseUnknownSymbols() throws Exception {
    getTokensForTestFile("unknown_symbols.lang");
  }

  private static List<LToken> getTokensForTestFile(String testFileName) throws IOException {
    LFileLexer fileLexer = new LFileLexer(TEST_L_FILES_PATH + testFileName);
    return fileLexer.parse();
  }

  private static void assertTokens(List<LToken> tokenList, LToken ... expectedTokens) {
    assertThat(tokenList).hasSize(expectedTokens.length);
    Iterator<LToken> tokenIterator = tokenList.iterator();
    for (LToken expectedToken : expectedTokens) {
      assertThat(tokenIterator.next()).isEqualTo(expectedToken);
    }
  }
}
