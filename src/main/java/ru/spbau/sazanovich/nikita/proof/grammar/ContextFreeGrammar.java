package ru.spbau.sazanovich.nikita.proof.grammar;

import com.google.common.annotations.VisibleForTesting;

import java.util.*;

public class ContextFreeGrammar {
  private final Map<Symbol, Set<Production>> symbolProductionMap = new HashMap<>();

  private Symbol initial;

  void setInitial(Symbol initial) {
    this.initial = initial;
  }

  private Map<Symbol, Set<Production>> getSymbolProductionMap() {
    return symbolProductionMap;
  }

  Symbol getInitial() {
    return initial;
  }

  void addProduction(Production production) {
    Symbol trigger = production.getTrigger();
    symbolProductionMap.putIfAbsent(trigger, new HashSet<>());
    symbolProductionMap.get(trigger).add(production);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ContextFreeGrammar grammar = (ContextFreeGrammar) o;

    if (!symbolProductionMap.equals(grammar.symbolProductionMap)) return false;
    return initial != null ? initial.equals(grammar.initial) : grammar.initial == null;
  }

  @Override
  public int hashCode() {
    int result = symbolProductionMap.hashCode();
    result = 31 * result + (initial != null ? initial.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "ContextFreeGrammar{" +
        "symbolProductionMap=" + symbolProductionMap +
        ", initial=" + initial +
        '}';
  }

  public ContextFreeGrammar toChomskyNormalForm() {
    return this.removeLongProductions();
  }

  @VisibleForTesting
  ContextFreeGrammar removeLongProductions() {
    ContextFreeGrammar shortGrammar = new ContextFreeGrammar();
    shortGrammar.setInitial(getInitial());
    getSymbolProductionMap().forEach((trigger, productions) -> {
      int internalTriggerNumber = 0;
      for (Production production : productions) {
        List<Symbol> products = production.getProducts();
        int productsSize = products.size();
        if (productsSize <= 2) {
          shortGrammar.addProduction(production);
          continue;
        }
        List<Symbol> internalTriggers = new ArrayList<>();
        for (int index = 0; index < productsSize - 2; index++) {
          internalTriggers.add(
              Symbol.getInternalSymbolFor(trigger.getLabel() + internalTriggerNumber++));
        }
        shortGrammar.addProduction(
            new Production(trigger, Arrays.asList(products.get(0), internalTriggers.get(0))));
        for (int index = 0; index < productsSize - 2; index++) {
          shortGrammar.addProduction(
              new Production(
                  internalTriggers.get(index),
                  Arrays.asList(
                      products.get(index + 1),
                      // Is it the last internal trigger?
                      index == productsSize - 3
                          ? products.get(index + 2)
                          : internalTriggers.get(index + 1))));
        }
      }
    });
    return shortGrammar;
  }
}
