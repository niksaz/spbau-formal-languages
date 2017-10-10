package ru.spbau.sazanovich.nikita.lexer.lexem;

public class LIntegerLiteral extends LToken {

  private final int value;

  public LIntegerLiteral(
      int value, int foundOnLine, int lineStartPosition, int lineEndPosition) {
    super(foundOnLine, lineStartPosition, lineEndPosition);
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "LIntegerLiteral{" + value + ", " + positionToString() + '}';
  }
}
