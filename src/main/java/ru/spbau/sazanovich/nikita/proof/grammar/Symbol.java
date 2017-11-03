package ru.spbau.sazanovich.nikita.proof.grammar;

import org.jetbrains.annotations.NotNull;

public class Symbol {
  private static final String EPS_TEXT = "eps";
  public static final Symbol EPS = new Symbol(EPS_TEXT);

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
      // TODO(niksaz): Consider caching short Symbols.
      return new Symbol(label);
    } else {
      throw new IllegalArgumentException("Illegal label for Symbol: " + label);
    }
  }

  @NotNull
  private final String label;

  private Symbol(@NotNull String label) {
    this.label = label;
  }

  @NotNull
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

    return label.equals(symbol.label);
  }

  @Override
  public int hashCode() {
    return label.hashCode();
  }

  @Override
  public String toString() {
    return "Symbol{" +
        "label='" + (this.equals(EPS) ? EPS_TEXT : label) + '\'' +
        '}';
  }
}
