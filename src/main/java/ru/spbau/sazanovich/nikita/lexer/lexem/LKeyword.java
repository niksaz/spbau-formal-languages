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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    LKeyword lKeyword = (LKeyword) o;

    return keywordType == lKeyword.keywordType;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (keywordType != null ? keywordType.hashCode() : 0);
    return result;
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
