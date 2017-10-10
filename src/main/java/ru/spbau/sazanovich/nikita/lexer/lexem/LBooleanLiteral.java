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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    LBooleanLiteral that = (LBooleanLiteral) o;

    return value == that.value;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (value ? 1 : 0);
    return result;
  }
}
