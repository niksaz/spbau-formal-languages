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
    OR
  }
}
