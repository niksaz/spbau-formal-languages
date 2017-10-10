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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    LFloatingPointLiteral that = (LFloatingPointLiteral) o;

    return Float.compare(that.value, value) == 0;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (value != +0.0f ? Float.floatToIntBits(value) : 0);
    return result;
  }
}
