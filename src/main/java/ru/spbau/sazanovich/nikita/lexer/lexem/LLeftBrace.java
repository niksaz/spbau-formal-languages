package ru.spbau.sazanovich.nikita.lexer.lexem;

public class LLeftBrace extends LToken {

  public LLeftBrace(int foundOnLine, int lineStartPosition, int lineEndPosition) {
    super(foundOnLine, lineStartPosition, lineEndPosition);
  }

  @Override
  public String toString() {
    return "LLeftBrace{" + positionToString() + '}';
  }
}
