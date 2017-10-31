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
    ContextFreeGrammar shortGrammar = removeLongProductions();
    return shortGrammar.removeEpsProductions();
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

  @VisibleForTesting
  ContextFreeGrammar removeEpsProductions() {
    Map<Symbol, Boolean> isEpsGenerating = new HashMap<>();
    isEpsGenerating.put(Symbol.EPS, true);
    getSymbolProductionMap().forEach((trigger, productions) ->
        computeEpsGenerating(trigger, isEpsGenerating)
    );

    ContextFreeGrammar epsFreeGrammar = new ContextFreeGrammar();
    if (isEpsGenerating.get(getInitial())) {
      Symbol initialWithEps = Symbol.getInternalSymbolFor(getInitial().getLabel() + "'");
      epsFreeGrammar.addProduction(
          new Production(initialWithEps, Collections.singletonList(Symbol.EPS)));
      epsFreeGrammar.addProduction(
          new Production(initialWithEps, Collections.singletonList(getInitial())));
      epsFreeGrammar.setInitial(initialWithEps);
    } else {
      epsFreeGrammar.setInitial(getInitial());
    }
    getSymbolProductionMap().forEach((trigger, productions) -> {
      List<Symbol> products = new ArrayList<>();
      for (Production production : productions) {
        generateNonEpsProductions(products, 0, production, isEpsGenerating,
            epsFreeGrammar);
      }
    });
    return epsFreeGrammar;
  }

  private boolean computeEpsGenerating(Symbol symbol, Map<Symbol, Boolean> isEpsGenerating) {
    Boolean computedResult = isEpsGenerating.get(symbol);
    if (computedResult != null) {
      return computedResult;
    }
    boolean epsGenerating = false;
    Set<Production> productions = getSymbolProductionMap().get(symbol);
    if (productions != null) {
      for (Production production : productions) {
        boolean allEpsGenerating = true;
        for (Symbol productSymbol : production.getProducts()) {
          allEpsGenerating = computeEpsGenerating(productSymbol, isEpsGenerating);
          if (!allEpsGenerating) {
            break;
          }
        }
        epsGenerating = allEpsGenerating;
        if (epsGenerating) {
          break;
        }
      }
    }
    isEpsGenerating.put(symbol, epsGenerating);
    return epsGenerating;
  }

  private void generateNonEpsProductions(
      List<Symbol> takenProducts,
      int index,
      Production production,
      Map<Symbol, Boolean> isEpsGenerating,
      ContextFreeGrammar epsFreeGrammar) {
    List<Symbol> products = production.getProducts();
    if (index == products.size()) {
      // Should not add rules like A -> eps.
      if (!(takenProducts.isEmpty() || takenProducts.get(0).equals(Symbol.EPS))) {
        epsFreeGrammar.addProduction(
            new Production(production.getTrigger(), new ArrayList<>(takenProducts)));
      }
      return;
    }
    Symbol currentSymbol = products.get(index);
    // May we skip it? If it is an eps generating symbol, then we can.
    if (Boolean.TRUE.equals(isEpsGenerating.get(currentSymbol))) {
      generateNonEpsProductions(
          takenProducts, index + 1, production, isEpsGenerating, epsFreeGrammar);
    }
    // Do not skip it.
    takenProducts.add(currentSymbol);
    generateNonEpsProductions(
        takenProducts, index + 1, production, isEpsGenerating, epsFreeGrammar);
    takenProducts.remove(takenProducts.size() - 1);
  }
}
