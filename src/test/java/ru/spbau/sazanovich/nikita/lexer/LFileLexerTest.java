package ru.spbau.sazanovich.nikita.lexer;

import static com.google.common.truth.Truth.assertThat;
import static ru.spbau.sazanovich.nikita.lexer.lexem.LKeyword.LKeywordType.*;
import static ru.spbau.sazanovich.nikita.lexer.lexem.LOperator.LOperatorType.*;

import org.junit.Test;
import ru.spbau.sazanovich.nikita.lexer.lexem.*;

import java.util.Iterator;
import java.util.List;

public class LFileLexerTest {

  @Test
  public void parse() throws Exception {
    LFileLexer fileLexer = new LFileLexer("src/test/resources/example.lang");
    List<LToken> tokenList = fileLexer.parse();
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

  private static void assertTokens(List<LToken> tokenList, LToken ... expectedTokens) {
    assertThat(tokenList).hasSize(expectedTokens.length);
    Iterator<LToken> tokenIterator = tokenList.iterator();
    for (LToken expectedToken : expectedTokens) {
      assertThat(tokenIterator.next()).isEqualTo(expectedToken);
    }
  }
}