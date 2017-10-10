package ru.spbau.sazanovich.nikita.lexer.lexem;

public abstract class LToken {

  /* Line's number in a file, where the token was found. */
  protected final int foundOnLine;

  /* Position in the line from which the token starts. */
  protected final int lineStartPosition;

  /* Position in the line where the token ends. */
  protected final int lineEndPosition;

  LToken(int foundOnLine, int lineStartPosition, int lineEndPosition) {
    this.foundOnLine = foundOnLine;
    this.lineStartPosition = lineStartPosition;
    this.lineEndPosition = lineEndPosition;
  }

  public int getFoundOnLine() {
    return foundOnLine;
  }

  public int getLineStartPosition() {
    return lineStartPosition;
  }

  public int getLineEndPosition() {
    return lineEndPosition;
  }

  protected String positionToString() {
    return foundOnLine + ", " + lineStartPosition + ", " + lineEndPosition;
  }
}
