package ru.spbau.sazanovich.nikita.proof.grammar;

public class Symbol {
  private static final Symbol EPS = new Symbol((char)0);

  static Symbol getSymbolFor(String label) {
    if (label.equals("eps")) {
      return EPS;
    }
    if (label.length() != 1) {
      throw new IllegalArgumentException("Symbol is not a letter: " + label);
    }
    // TODO(niksaz): Cache Symbols for characters.
    return new Symbol(label.charAt(0));
  }

  private final char label;

  private Symbol(char label) {
    this.label = label;
  }

  public char getLabel() {
    return label;
  }

  boolean isTerminal() {
    return Character.isLowerCase(label);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Symbol symbol = (Symbol) o;

    return label == symbol.label;
  }

  @Override
  public int hashCode() {
    return (int) label;
  }

  @Override
  public String toString() {
    return "Symbol{" +
        "label=" + label +
        '}';
  }
}
