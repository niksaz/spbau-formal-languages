package ru.spbau.sazanovich.nikita.proof.grammar;

import java.util.List;

public class Production {

  private final Symbol trigger;

  private final List<Symbol> result;

  public Production(Symbol trigger, List<Symbol> result) {
    if (trigger.isTerminal()) {
      throw new IllegalArgumentException("Trigger symbol is not a terminal: " + trigger);
    }
    this.trigger = trigger;
    this.result = result;
  }

  public Symbol getTrigger() {
    return trigger;
  }

  public List<Symbol> getResult() {
    return result;
  }
}
