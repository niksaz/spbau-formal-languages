package ru.spbau.sazanovich.nikita.proof.grammar;

public class Symbol {

  public static final Symbol EPS = new Symbol((char)0);

  private final char label;

  private Symbol(char label) {
    this.label = label;
  }

  public char getLabel() {
    return label;
  }

  boolean isTerminal() {
    return label == 0 || Character.isLowerCase(label);
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
