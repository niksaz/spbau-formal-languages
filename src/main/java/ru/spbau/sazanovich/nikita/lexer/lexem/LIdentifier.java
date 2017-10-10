package ru.spbau.sazanovich.nikita.lexer.lexem;

public class LIdentifier extends LToken {

  /* String which represents the identifier. */
  private final String name;

  public LIdentifier(String name, int foundOnLine, int lineStartPosition, int lineEndPosition) {
    super(foundOnLine, lineStartPosition, lineEndPosition);
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return "LIdentifier{\"" + name + "\", " + positionToString() + '}';
  }
}
