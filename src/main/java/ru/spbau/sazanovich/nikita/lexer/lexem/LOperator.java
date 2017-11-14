package ru.spbau.sazanovich.nikita.lexer.lexem;

public class LOperator extends LToken {

  /** Specifies the operator which the token represents. */
  private final LOperatorType operatorType;

  public LOperator(
      LOperatorType operatorType, int foundOnLine, int lineStartPosition, int lineEndPosition) {
    super(foundOnLine, lineStartPosition, lineEndPosition);
    this.operatorType = operatorType;
  }

  public LOperatorType getOperatorType() {
    return operatorType;
  }

  @Override
  public String toString() {
    return "LOperator{" + operatorType + ", " + positionToString() + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    LOperator lOperator = (LOperator) o;

    return operatorType == lOperator.operatorType;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (operatorType != null ? operatorType.hashCode() : 0);
    return result;
  }

  public enum LOperatorType {
    PLUS,
    MINUS,
    ASTERISK,
    SLASH,
    PERCENT,
    EQUAL,
    NOT_EQUAL,
    MORE,
    MORE_OR_EQUAL,
    LESS,
    LESS_OR_EQUAL,
    AND,
    OR,
    ASSIGNMENT
  }
}
