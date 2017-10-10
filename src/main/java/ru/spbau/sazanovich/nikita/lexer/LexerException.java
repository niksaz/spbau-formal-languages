package ru.spbau.sazanovich.nikita.lexer;

public class LexerException extends RuntimeException {

  private final int foundOnLine;

  private final int lineStartPosition;

  public LexerException(String message, int foundOnLine, int lineStartPosition) {
    super(message);
    this.foundOnLine = foundOnLine;
    this.lineStartPosition = lineStartPosition;
  }

  public int getFoundOnLine() {
    return foundOnLine;
  }

  public int getLineStartPosition() {
    return lineStartPosition;
  }
}
