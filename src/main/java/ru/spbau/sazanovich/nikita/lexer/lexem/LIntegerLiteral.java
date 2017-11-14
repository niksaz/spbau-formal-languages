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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    LIntegerLiteral that = (LIntegerLiteral) o;

    return value == that.value;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + value;
    return result;
  }
}
