package ru.spbau.sazanovich.nikita.proof.grammar;

import java.util.*;

public class ContextFreeGrammar {

  private final Set<Symbol> symbols = new HashSet<>();
  private final Map<Symbol, List<Production>> symbolProductions = new HashMap<>();

  private Symbol initial;

  public void setInitial(Symbol initial) {
    this.initial = initial;
  }

  public Set<Symbol> getSymbols() {
    return symbols;
  }

  public Map<Symbol, List<Production>> getSymbolProductions() {
    return symbolProductions;
  }

  public Symbol getInitial() {
    return initial;
  }
}
