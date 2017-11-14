package ru.spbau.sazanovich.nikita.lexer;

import ru.spbau.sazanovich.nikita.lexer.lexem.LToken;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LFileLexer {

  private final String filePath;

  public LFileLexer(String filePath) {
    this.filePath = filePath;
  }

  public List<LToken> parse() throws IOException {
    try (
        FileReader fileReader = new FileReader(filePath)) {
      LLexer tokenLexer = new LLexer(fileReader);
      List<LToken> tokenList = new ArrayList<>();
      LToken lToken;
      while ((lToken = tokenLexer.advance()) != null) {
        tokenList.add(lToken);
      }
      return tokenList;
    }
  }
}
