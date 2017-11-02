package ru.spbau.sazanovich.nikita.proof.grammar;

import org.jetbrains.annotations.NotNull;

public class Symbol {
  static final Symbol EPS = new Symbol("");
  private static final String EPS_TEXT = "eps";

  public static Symbol getSymbolFor(@NotNull String label) {
    if (label.length() != 1 && !label.equals(EPS_TEXT)) {
      throw new IllegalArgumentException("Symbol's length > 1");
    }
    return getInternalSymbolFor(label);
  }

  public static Symbol getInternalSymbolFor(@NotNull String label) {
    if (label.equals(EPS_TEXT)) {
      return EPS;
    }
    if ((label.length() == 1 && Character.isLowerCase(label.charAt(0)))
        || label.toUpperCase().equals(label)) {
      // TODO(niksaz): Cache Symbols.
      return new Symbol(label);
    } else {
      throw new IllegalArgumentException("Illegal label for Symbol: " + label);
    }
  }

  private final String label;

  private Symbol(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

  public boolean isTerminal() {
    return label.length() == 1 && Character.isLowerCase(label.charAt(0));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Symbol symbol = (Symbol) o;

    return label != null ? label.equals(symbol.label) : symbol.label == null;
  }

  @Override
  public int hashCode() {
    return label != null ? label.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "Symbol{" +
        "label='" + (this.equals(EPS) ? EPS_TEXT : label) + '\'' +
        '}';
  }
}
