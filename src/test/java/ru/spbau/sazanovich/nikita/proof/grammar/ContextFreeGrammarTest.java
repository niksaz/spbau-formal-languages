package ru.spbau.sazanovich.nikita.proof.grammar;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static com.google.common.truth.Truth.assertThat;

public class ContextFreeGrammarTest {
  @Test
  public void removeLongProductions() throws Exception {
    ContextFreeGrammar grammar = new ContextFreeGrammar();
    Symbol aNonterminal = Symbol.getSymbolFor("A");
    Symbol bNonterminal = Symbol.getSymbolFor("B");
    Symbol cNonterminal = Symbol.getSymbolFor("C");
    Symbol eps = Symbol.getSymbolFor("eps");
    Symbol aTerminal = Symbol.getSymbolFor("a");
    Symbol bTerminal = Symbol.getSymbolFor("b");
    Symbol cTerminal = Symbol.getSymbolFor("c");
    grammar.setInitial(aNonterminal);
    grammar.addProduction(
        new Production(aNonterminal, Arrays.asList(aTerminal, bNonterminal, bTerminal)));
    grammar.addProduction(
        new Production(bNonterminal, Collections.singletonList(cNonterminal)));
    grammar.addProduction(
        new Production(bNonterminal, Arrays.asList(aTerminal, bNonterminal, bTerminal)));
    grammar.addProduction(
        new Production(cNonterminal, Arrays.asList(cTerminal, cNonterminal)));
    grammar.addProduction(
        new Production(cNonterminal, Collections.singletonList(eps)));
    ContextFreeGrammar shortGrammar = grammar.removeLongProductions();

    ContextFreeGrammar expectedGrammar = new ContextFreeGrammar();
    expectedGrammar.setInitial(aNonterminal);
    Symbol a0Nonterminal = Symbol.getSymbolFor("A0");
    Symbol b0Nonterminal = Symbol.getSymbolFor("B0");
    expectedGrammar.addProduction(
        new Production(aNonterminal, Arrays.asList(aTerminal, a0Nonterminal)));
    expectedGrammar.addProduction(
        new Production(a0Nonterminal, Arrays.asList(bNonterminal, bTerminal)));
    expectedGrammar.addProduction(
        new Production(bNonterminal, Collections.singletonList(cNonterminal)));
    expectedGrammar.addProduction(
        new Production(bNonterminal, Arrays.asList(aTerminal, b0Nonterminal)));
    expectedGrammar.addProduction(
        new Production(b0Nonterminal, Arrays.asList(bNonterminal, bTerminal)));
    expectedGrammar.addProduction(
        new Production(cNonterminal, Arrays.asList(cTerminal, cNonterminal)));
    expectedGrammar.addProduction(
        new Production(cNonterminal, Collections.singletonList(eps)));
    assertThat(shortGrammar).isEqualTo(expectedGrammar);
  }

  @Test
  public void removeEpsProductions() throws Exception {
    ContextFreeGrammar grammar = new ContextFreeGrammar();
    Symbol aNonterminal = Symbol.getSymbolFor("A");
    Symbol bNonterminal = Symbol.getSymbolFor("B");
    Symbol cNonterminal = Symbol.getSymbolFor("C");
    Symbol eps = Symbol.getSymbolFor("eps");
    Symbol bTerminal = Symbol.getSymbolFor("b");
    Symbol cTerminal = Symbol.getSymbolFor("c");
    grammar.setInitial(aNonterminal);
    grammar.addProduction(
        new Production(aNonterminal, Arrays.asList(bNonterminal, cNonterminal, bNonterminal)));
    grammar.addProduction(
        new Production(aNonterminal, Collections.singletonList(bNonterminal)));
    grammar.addProduction(
        new Production(bNonterminal, Collections.singletonList(bTerminal)));
    grammar.addProduction(
        new Production(bNonterminal, Collections.singletonList(eps)));
    grammar.addProduction(
        new Production(cNonterminal, Collections.singletonList(cTerminal)));
    ContextFreeGrammar epsFreeGrammar = grammar.removeEpsProductions();

    ContextFreeGrammar expectedGrammar = new ContextFreeGrammar();
    Symbol aStrokeNonterminal = Symbol.getSymbolFor("A'");
    expectedGrammar.setInitial(aStrokeNonterminal);
    expectedGrammar.addProduction(
        new Production(aStrokeNonterminal, Collections.singletonList(eps)));
    expectedGrammar.addProduction(
        new Production(aStrokeNonterminal, Collections.singletonList(aNonterminal)));
    expectedGrammar.addProduction(
        new Production(aNonterminal, Arrays.asList(bNonterminal, cNonterminal, bNonterminal)));
    expectedGrammar.addProduction(
        new Production(aNonterminal, Arrays.asList(cNonterminal, bNonterminal)));
    expectedGrammar.addProduction(
        new Production(aNonterminal, Arrays.asList(bNonterminal, cNonterminal)));
    expectedGrammar.addProduction(
        new Production(aNonterminal, Collections.singletonList(cNonterminal)));
    expectedGrammar.addProduction(
        new Production(aNonterminal, Collections.singletonList(bNonterminal)));
    expectedGrammar.addProduction(
        new Production(bNonterminal, Collections.singletonList(bTerminal)));
    expectedGrammar.addProduction(
        new Production(cNonterminal, Collections.singletonList(cTerminal)));
    assertThat(epsFreeGrammar).isEqualTo(expectedGrammar);
  }

  @Test
  public void removeChainProductions() throws Exception {
    ContextFreeGrammar grammar = new ContextFreeGrammar();
    Symbol aNonterminal = Symbol.getSymbolFor("A");
    Symbol bNonterminal = Symbol.getSymbolFor("B");
    Symbol cNonterminal = Symbol.getSymbolFor("C");
    Symbol bTerminal = Symbol.getSymbolFor("b");
    Symbol cTerminal = Symbol.getSymbolFor("c");
    grammar.setInitial(aNonterminal);
    grammar.addProduction(
        new Production(aNonterminal, Arrays.asList(aNonterminal, bNonterminal)));
    grammar.addProduction(
        new Production(aNonterminal, Collections.singletonList(cNonterminal)));
    grammar.addProduction(
        new Production(cNonterminal, Collections.singletonList(cTerminal)));
    grammar.addProduction(
        new Production(cNonterminal, Arrays.asList(cTerminal, cNonterminal)));
    grammar.addProduction(
        new Production(cNonterminal, Collections.singletonList(bNonterminal)));
    grammar.addProduction(
        new Production(bNonterminal, Collections.singletonList(bTerminal)));
    ContextFreeGrammar chainFreeGrammar = grammar.removeChainProductions();

    ContextFreeGrammar expectedGrammar = new ContextFreeGrammar();
    expectedGrammar.setInitial(aNonterminal);
    expectedGrammar.addProduction(
        new Production(aNonterminal, Arrays.asList(aNonterminal, bNonterminal)));
    expectedGrammar.addProduction(
        new Production(aNonterminal, Collections.singletonList(cTerminal)));
    expectedGrammar.addProduction(
        new Production(aNonterminal, Arrays.asList(cTerminal, cNonterminal)));
    expectedGrammar.addProduction(
        new Production(aNonterminal, Collections.singletonList(bTerminal)));
    expectedGrammar.addProduction(
        new Production(cNonterminal, Collections.singletonList(cTerminal)));
    expectedGrammar.addProduction(
        new Production(cNonterminal, Arrays.asList(cTerminal, cNonterminal)));
    expectedGrammar.addProduction(
        new Production(cNonterminal, Collections.singletonList(bTerminal)));
    expectedGrammar.addProduction(
        new Production(bNonterminal, Collections.singletonList(bTerminal)));
    assertThat(chainFreeGrammar).isEqualTo(expectedGrammar);
  }

  @Test
  public void removeNonterminalsInLongProductions() throws Exception {
    ContextFreeGrammar grammar = new ContextFreeGrammar();
    Symbol aNonterminal = Symbol.getSymbolFor("A");
    Symbol bNonterminal = Symbol.getSymbolFor("B");
    Symbol cNonterminal = Symbol.getSymbolFor("C");
    Symbol aTerminal = Symbol.getSymbolFor("a");
    Symbol bTerminal = Symbol.getSymbolFor("b");
    Symbol cTerminal = Symbol.getSymbolFor("c");
    grammar.setInitial(aNonterminal);
    grammar.addProduction(
        new Production(aNonterminal, Arrays.asList(aTerminal, bNonterminal, bTerminal)));
    grammar.addProduction(
        new Production(bNonterminal, Collections.singletonList(cNonterminal)));
    grammar.addProduction(
        new Production(bNonterminal, Arrays.asList(aTerminal, bNonterminal, bTerminal)));
    grammar.addProduction(
        new Production(cNonterminal, Arrays.asList(cTerminal, cNonterminal)));
    grammar.addProduction(
        new Production(cNonterminal, Collections.singletonList(cTerminal)));
    ContextFreeGrammar resultGrammar = grammar.removeNonterminalsInLongProductions();

    ContextFreeGrammar expectedGrammar = new ContextFreeGrammar();
    expectedGrammar.setInitial(aNonterminal);
    Symbol aLNonterminal = Symbol.getSymbolFor("AL");
    Symbol bLNonterminal = Symbol.getSymbolFor("BL");
    Symbol cLNonterminal = Symbol.getSymbolFor("CL");
    expectedGrammar.addProduction(
        new Production(aNonterminal, Arrays.asList(aLNonterminal, bNonterminal, bLNonterminal)));
    expectedGrammar.addProduction(
        new Production(bNonterminal, Collections.singletonList(cNonterminal)));
    expectedGrammar.addProduction(
        new Production(bNonterminal, Arrays.asList(aLNonterminal, bNonterminal, bLNonterminal)));
    expectedGrammar.addProduction(
        new Production(cNonterminal, Arrays.asList(cLNonterminal, cNonterminal)));
    expectedGrammar.addProduction(
        new Production(cNonterminal, Collections.singletonList(cTerminal)));
    expectedGrammar.addProduction(
        new Production(aLNonterminal, Collections.singletonList(aTerminal)));
    expectedGrammar.addProduction(
        new Production(bLNonterminal, Collections.singletonList(bTerminal)));
    expectedGrammar.addProduction(
        new Production(cLNonterminal, Collections.singletonList(cTerminal)));
    assertThat(resultGrammar).isEqualTo(expectedGrammar);
  }
}