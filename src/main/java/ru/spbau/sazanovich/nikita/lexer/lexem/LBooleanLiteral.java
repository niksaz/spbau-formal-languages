package ru.spbau.sazanovich.nikita.lexer.lexem;

public class LBooleanLiteral extends LToken {

  private final boolean value;

  public LBooleanLiteral(
      boolean value, int foundOnLine, int lineStartPosition, int lineEndPosition) {
    super(foundOnLine, lineStartPosition, lineEndPosition);
    this.value = value;
  }

  public boolean getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "LBooleanLiteral{" + value + ", " + positionToString() +'}';
  }
}
