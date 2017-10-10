package ru.spbau.sazanovich.nikita.lexer.lexem;

public class LRightBrace extends LToken {

  public LRightBrace(int foundOnLine, int lineStartPosition, int lineEndPosition) {
    super(foundOnLine, lineStartPosition, lineEndPosition);
  }

  @Override
  public String toString() {
    return "LRightBrace{" + positionToString() + '}';
  }
}
