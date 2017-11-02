package ru.spbau.sazanovich.nikita.proof;

import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;
import ru.spbau.sazanovich.nikita.proof.algo.IntersectionGrammarBuilder;
import ru.spbau.sazanovich.nikita.proof.grammar.ContextFreeGrammar;
import ru.spbau.sazanovich.nikita.proof.grammar.ContextFreeGrammarParser;

import java.io.FileInputStream;
import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {
    try {
      MutableGraph automaton =
          Parser.read(new FileInputStream("src/test/resources/automaton.dot"));
      ContextFreeGrammar grammar =
          ContextFreeGrammarParser.read(new FileInputStream("src/test/resources/grammar.txt"));
      ContextFreeGrammar normalizedGrammar = grammar.toChomskyNormalForm();

      IntersectionGrammarBuilder intersectionBuilder =
          new IntersectionGrammarBuilder(normalizedGrammar, automaton);
      ContextFreeGrammar intersectionGrammar = intersectionBuilder.build();
      intersectionGrammar.printTo(System.out);
    } catch (IllegalArgumentException e) {
      System.out.println("Incorrect input: " + e.getMessage());
    }
  }
}
