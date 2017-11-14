package ru.spbau.sazanovich.nikita.lexer.lexem;

public class LComment extends LToken {

  private final String text;

  public LComment(String text, int foundOnLine, int lineStartPosition, int lineEndPosition) {
    super(foundOnLine, lineStartPosition, lineEndPosition);
    this.text = text;
  }

  public String getText() {
    return text;
  }

  @Override
  public String toString() {
    return "LComment{\"" + text + "\", " + positionToString() + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    LComment lComment = (LComment) o;

    return text != null ? text.equals(lComment.text) : lComment.text == null;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (text != null ? text.hashCode() : 0);
    return result;
  }
}
