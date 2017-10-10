package ru.spbau.sazanovich.nikita.lexer.lexem;

public class LSemicolon extends LToken {

  public LSemicolon(int foundOnLine, int lineStartPosition, int lineEndPosition) {
    super(foundOnLine, lineStartPosition, lineEndPosition);
  }

  @Override
  public String toString() {
    return "LSemicolon{" + positionToString() + "}";
  }
}
