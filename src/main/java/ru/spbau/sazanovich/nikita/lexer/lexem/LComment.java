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
}
