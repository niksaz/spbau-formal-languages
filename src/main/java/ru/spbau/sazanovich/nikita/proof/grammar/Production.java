package ru.spbau.sazanovich.nikita.proof.grammar;

import java.util.List;

public class Production {
  private final Symbol trigger;

  private final List<Symbol> products;

  Production(Symbol trigger, List<Symbol> products) {
    if (trigger.isTerminal()) {
      throw new IllegalArgumentException("Trigger symbol is not a terminal: " + trigger);
    }
    if (products.isEmpty()) {
      throw new IllegalArgumentException("Empty result for production!");
    }
    this.trigger = trigger;
    this.products = products;
  }

  Symbol getTrigger() {
    return trigger;
  }

  public List<Symbol> getProducts() {
    return products;
  }

  @Override
  public String toString() {
    return "Production{" +
        "trigger=" + trigger +
        ", products=" + products +
        '}';
  }
}
