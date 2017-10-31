package ru.spbau.sazanovich.nikita.proof.grammar;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static com.google.common.truth.Truth.assertThat;

public class ContextFreeGrammarTest {
  @Test
  public void removeLongProductions() throws Exception {
    ContextFreeGrammar longGrammar = new ContextFreeGrammar();
    Symbol aNonterminal = Symbol.getSymbolFor("A");
    Symbol bNonterminal = Symbol.getSymbolFor("B");
    Symbol cNonterminal = Symbol.getSymbolFor("C");
    Symbol aTerminal = Symbol.getSymbolFor("a");
    Symbol bTerminal = Symbol.getSymbolFor("b");
    Symbol cTerminal = Symbol.getSymbolFor("c");
    longGrammar.setInitial(aNonterminal);
    longGrammar.addProduction(
        new Production(aNonterminal, Arrays.asList(aTerminal, bNonterminal, bTerminal)));
    longGrammar.addProduction(
        new Production(bNonterminal, Collections.singletonList(cNonterminal)));
    longGrammar.addProduction(
        new Production(bNonterminal, Arrays.asList(aTerminal, bNonterminal, bTerminal)));
    longGrammar.addProduction(
        new Production(cNonterminal, Arrays.asList(cTerminal, cNonterminal)));
    longGrammar.addProduction(
        new Production(cNonterminal, Collections.singletonList(Symbol.getSymbolFor("eps"))));
    ContextFreeGrammar shortGrammar = longGrammar.removeLongProductions();

    ContextFreeGrammar expectedGrammar = new ContextFreeGrammar();
    expectedGrammar.setInitial(aNonterminal);
    Symbol a0Nonterminal = Symbol.getInternalSymbolFor("A0");
    Symbol b0Nonterminal = Symbol.getInternalSymbolFor("B0");
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
        new Production(cNonterminal, Collections.singletonList(Symbol.getSymbolFor("eps"))));

    assertThat(shortGrammar).isEqualTo(expectedGrammar);
  }
}