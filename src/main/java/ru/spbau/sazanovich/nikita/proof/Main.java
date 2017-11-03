package ru.spbau.sazanovich.nikita.proof;

import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;
import ru.spbau.sazanovich.nikita.proof.algo.IntersectionGrammarBuilder;
import ru.spbau.sazanovich.nikita.proof.grammar.ContextFreeGrammar;
import ru.spbau.sazanovich.nikita.proof.grammar.ContextFreeGrammarParser;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class Main {
  public static void main(String[] args) {
    if (args.length != 3) {
      System.out.println(
          "Incorrect number of arguments. There should 3 arguments: " +
          "automatonPath grammarPath outputPath");
      return;
    }
    String automatonPath = args[0];
    String grammarPath = args[1];
    String outputPath = args[2];
    try {
      MutableGraph automaton =
          Parser.read(new FileInputStream(automatonPath));
      ContextFreeGrammar grammar =
          ContextFreeGrammarParser.read(new FileInputStream(grammarPath));
      ContextFreeGrammar normalizedGrammar = grammar.toChomskyNormalForm();

      IntersectionGrammarBuilder intersectionBuilder =
          new IntersectionGrammarBuilder(normalizedGrammar, automaton);
      ContextFreeGrammar intersectionGrammar = intersectionBuilder.build();
      intersectionGrammar.printTo(new PrintStream(new FileOutputStream(outputPath)));
    } catch (IllegalArgumentException e) {
      System.out.println("Incorrect input: " + e.getMessage());
    } catch (IOException e) {
      System.out.println("IO error occurred: " + e.getMessage());
    }
  }
}
