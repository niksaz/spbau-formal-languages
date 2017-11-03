package ru.spbau.sazanovich.nikita.proof.grammar;

import com.google.common.annotations.VisibleForTesting;

import java.io.PrintStream;
import java.util.*;

public class ContextFreeGrammar {
  private final Map<Symbol, Set<Production>> symbolProductionMap = new HashMap<>();

  private Symbol initial;

  public Symbol getInitial() {
    return initial;
  }

  public void setInitial(Symbol initial) {
    this.initial = initial;
  }

  public Map<Symbol, Set<Production>> getSymbolProductionMap() {
    return symbolProductionMap;
  }

  public void addProduction(Production production) {
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

  public void printTo(PrintStream printStream) {
    // Need to process the initial symbol separately because an initial symbol should be a first
    // rule.
    Symbol initial = getInitial();
    Set<Production> initialProductions = getSymbolProductionMap().get(initial);
    if (initialProductions == null || initialProductions.isEmpty()) {
      // Should print something to indicate that this symbol is initial.
      printStream.print(initial.getLabel());
      printStream.print(": ");
      printStream.println(initial.getLabel());
    } else {
      printProductions(printStream, initialProductions);
    }
    getSymbolProductionMap().forEach((trigger, productions) -> {
      if (!trigger.equals(initial)) {
        printProductions(printStream, productions);
      }
    });
  }

  private static void printProductions(PrintStream printStream, Set<Production> productions) {
    for (Production production : productions) {
      printStream.print(production.getTrigger().getLabel());
      printStream.print(':');
      for (Symbol product : production.getProducts()) {
        printStream.print(' ');
        printStream.print(product.getLabel());
      }
      printStream.println();
    }
  }

  public ContextFreeGrammar toChomskyNormalForm() {
    ContextFreeGrammar shortGrammar = removeLongProductions();
    ContextFreeGrammar epsFreeGrammar = shortGrammar.removeEpsProductions();
    ContextFreeGrammar chainFreeGrammar = epsFreeGrammar.removeChainProductions();
    return chainFreeGrammar.removeNonterminalsInLongProductions();
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

  @VisibleForTesting
  ContextFreeGrammar removeChainProductions() {
    ContextFreeGrammar chainFreeGrammar = new ContextFreeGrammar();
    chainFreeGrammar.setInitial(getInitial());
    // We will build a graph only for Symbols which have some production rules in the grammar.
    // Others are meaningless.
    List<Symbol> meaningfulSymbols = new ArrayList<>(getSymbolProductionMap().keySet());
    int n = meaningfulSymbols.size();
    boolean chainProductionable[][] = new boolean[n][];
    for (int i = 0; i < n; i++) {
      chainProductionable[i] = new boolean[n];
    }
    getSymbolProductionMap().forEach((trigger, productions) -> {
      int triggerIndex = meaningfulSymbols.indexOf(trigger);
      for (Production production : productions) {
        List<Symbol> products = production.getProducts();
        if (products.size() != 1) {
          chainFreeGrammar.addProduction(production);
          continue;
        }
        Symbol product = products.get(0);
        int productIndex = meaningfulSymbols.indexOf(product);
        if (productIndex == -1) {
          chainFreeGrammar.addProduction(production);
          continue;
        }
        chainProductionable[triggerIndex][productIndex] = true;
      }
    });
    closeTransitively(chainProductionable);
    getSymbolProductionMap().forEach((trigger, triggerProductions) -> {
      int triggerIndex = meaningfulSymbols.indexOf(trigger);
      for (int productIndex = 0; productIndex < n; productIndex++) {
        if (chainProductionable[triggerIndex][productIndex]) {
          Symbol product = meaningfulSymbols.get(productIndex);
          Set<Production> chainProductions = getSymbolProductionMap().get(product);
          for (Production production : chainProductions) {
            List<Symbol> chainProducts = production.getProducts();
            if (chainProducts.size() == 1 && !chainProducts.get(0).isTerminal()) {
              continue;
            }
            chainFreeGrammar.addProduction(new Production(trigger, chainProducts));
          }
        }
      }
    });
    return chainFreeGrammar;
  }

  @VisibleForTesting
  ContextFreeGrammar removeNonterminalsInLongProductions() {
    ContextFreeGrammar resultGrammar = new ContextFreeGrammar();
    resultGrammar.setInitial(getInitial());
    Set<Symbol> introducedSymbols = new HashSet<>();
    getSymbolProductionMap().forEach((trigger, productions) -> {
      for (Production production : productions) {
        List<Symbol> products = production.getProducts();
        if (products.size() == 1) {
          resultGrammar.addProduction(production);
          continue;
        }
        List<Symbol> transformedProducts = new ArrayList<>();
        for (Symbol product : products) {
          if (product.isTerminal()) {
            Symbol newSymbol = Symbol.getInternalSymbolFor(product.getLabel().toUpperCase() + "L");
            if (!introducedSymbols.contains(newSymbol)) {
              introducedSymbols.add(newSymbol);
              resultGrammar.addProduction(
                  new Production(newSymbol, Collections.singletonList(product)));
            }
            transformedProducts.add(newSymbol);
          } else {
            transformedProducts.add(product);
          }
        }
        resultGrammar.addProduction(new Production(trigger, transformedProducts));
      }
    });
    return resultGrammar;
  }

  private static void closeTransitively(boolean[][] g) {
    int n = g.length;
    for (int k = 0; k < n; k++) {
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
          g[i][j] |= g[i][k] & g[k][j];
        }
      }
    }
  }
}
