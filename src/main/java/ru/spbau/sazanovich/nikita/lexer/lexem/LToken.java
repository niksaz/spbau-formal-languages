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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    LToken lToken = (LToken) o;

    if (foundOnLine != lToken.foundOnLine) return false;
    if (lineStartPosition != lToken.lineStartPosition) return false;
    return lineEndPosition == lToken.lineEndPosition;
  }

  @Override
  public int hashCode() {
    int result = foundOnLine;
    result = 31 * result + lineStartPosition;
    result = 31 * result + lineEndPosition;
    return result;
  }
}
