package ru.spbau.sazanovich.nikita.proof;

import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;
import ru.spbau.sazanovich.nikita.proof.grammar.ContextFreeGrammar;
import ru.spbau.sazanovich.nikita.proof.grammar.ContextFreeGrammarParser;
import ru.spbau.sazanovich.nikita.proof.grammar.Production;
import ru.spbau.sazanovich.nikita.proof.grammar.Symbol;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main {
  public static void main(String[] args) throws IOException {
    MutableGraph automaton =
        Parser.read(new FileInputStream("src/test/resources/automaton.dot"));
    ContextFreeGrammar grammar =
        ContextFreeGrammarParser.read(new FileInputStream("src/test/resources/grammar.txt"));

    System.out.println(grammar.getInitial());
    for (Map.Entry<Symbol, List<Production>> symbolProductions :
        grammar.getSymbolProductionMap().entrySet()) {
      for (Production production : symbolProductions.getValue()) {
        System.out.println(production);
      }
    }
  }
}
