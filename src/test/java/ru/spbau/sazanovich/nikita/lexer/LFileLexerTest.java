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
  public void parse() throws Exception {
    List<LToken> tokenList = getTokensForTestFile("example.lang");
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
