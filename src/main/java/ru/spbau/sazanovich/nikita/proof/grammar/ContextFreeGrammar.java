package ru.spbau.sazanovich.nikita.proof.grammar;

import java.util.*;

public class ContextFreeGrammar {
  private final Map<Symbol, List<Production>> symbolProductionMap = new HashMap<>();

  private Symbol initial;

  void setInitial(Symbol initial) {
    this.initial = initial;
  }

  public Map<Symbol, List<Production>> getSymbolProductionMap() {
    return symbolProductionMap;
  }

  public Symbol getInitial() {
    return initial;
  }

  void addProduction(Production production) {
    Symbol trigger = production.getTrigger();
    symbolProductionMap.putIfAbsent(trigger, new ArrayList<>());
    symbolProductionMap.get(trigger).add(production);
  }
}
