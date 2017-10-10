package ru.spbau.sazanovich.nikita.lexer.lexem;

public class LKeyword extends LToken {

  /** Specifies the keyword which the token represents. */
  private final LKeywordType keywordType;

  public LKeyword(
      LKeywordType keywordType, int foundOnLine, int lineStartPosition, int lineEndPosition) {
    super(foundOnLine, lineStartPosition, lineEndPosition);
    this.keywordType = keywordType;
  }

  public LKeywordType getKeywordType() {
    return keywordType;
  }

  @Override
  public String toString() {
    return "LKeyword{" + keywordType + ", " + positionToString() + '}';
  }

  public enum LKeywordType {
    IF,
    THEN,
    ELSE,
    WHILE,
    DO,
    READ,
    WRITE,
    BEGIN,
    END
  }
}
