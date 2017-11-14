package ru.spbau.sazanovich.nikita;

import ru.spbau.sazanovich.nikita.lexer.LFileLexer;
import ru.spbau.sazanovich.nikita.lexer.LexerException;
import ru.spbau.sazanovich.nikita.lexer.lexem.LToken;

import java.io.IOException;
import java.util.List;

public class Main {

  public static void main(String[] args) {
    if (args.length == 0) {
      System.out.println("Please, specify the path to file written in L language.");
      return;
    }
    if (args.length > 1) {
      System.out.println("Please, specify one L file at a time.");
      return;
    }
    String pathToLFile = args[0];

    LFileLexer fileLexer = new LFileLexer(pathToLFile);
    try {
      List<LToken> tokenList = fileLexer.parse();
      printTokens(tokenList);
    } catch (IOException e) {
      System.out.println("IOError occurred: " + e.getMessage());
    } catch (LexerException e) {
      System.out.println("LexerException occurred at " +
          e.getFoundOnLine() + ":" + e.getLineStartPosition() + ": " + e.getMessage());
    }
  }

  private static void printTokens(List<LToken> tokenList) {
    for (LToken lToken : tokenList) {
      System.out.print(lToken + "; ");
    }
    System.out.println();
  }
}
