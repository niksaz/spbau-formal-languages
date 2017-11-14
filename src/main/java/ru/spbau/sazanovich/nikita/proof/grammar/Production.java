package ru.spbau.sazanovich.nikita.proof.grammar;

import java.util.List;

public class Production {
  private final Symbol trigger;

  private final List<Symbol> products;

  public Production(Symbol trigger, List<Symbol> products) {
    if (trigger.isTerminal()) {
      throw new IllegalArgumentException("Trigger symbol is not a terminal: " + trigger);
    }
    if (products.isEmpty()) {
      throw new IllegalArgumentException("Empty result for production!");
    }
    this.trigger = trigger;
    this.products = products;
  }

  public Symbol getTrigger() {
    return trigger;
  }

  public List<Symbol> getProducts() {
    return products;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Production that = (Production) o;

    if (trigger != null ? !trigger.equals(that.trigger) : that.trigger != null) return false;
    return products != null ? products.equals(that.products) : that.products == null;
  }

  @Override
  public int hashCode() {
    int result = trigger != null ? trigger.hashCode() : 0;
    result = 31 * result + (products != null ? products.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Production{" +
        "trigger=" + trigger +
        ", products=" + products +
        '}';
  }
}
