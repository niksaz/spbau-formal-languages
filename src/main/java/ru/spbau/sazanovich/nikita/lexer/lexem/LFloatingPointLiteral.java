package ru.spbau.sazanovich.nikita.lexer.lexem;

public class LFloatingPointLiteral extends LToken {

  private final float value;

  public LFloatingPointLiteral(
      float value, int foundOnLine, int lineStartPosition, int lineEndPosition) {
    super(foundOnLine, lineStartPosition, lineEndPosition);
    this.value = value;
  }

  public float getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "LFloatingPointLiteral{" + value + ", " + positionToString() + '}';
  }
}
