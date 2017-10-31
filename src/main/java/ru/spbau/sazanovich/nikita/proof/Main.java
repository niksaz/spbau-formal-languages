package ru.spbau.sazanovich.nikita.proof;

import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;
import ru.spbau.sazanovich.nikita.proof.grammar.ContextFreeGrammar;
import ru.spbau.sazanovich.nikita.proof.grammar.ContextFreeGrammarParser;

import java.io.FileInputStream;
import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {
    MutableGraph automaton =
        Parser.read(new FileInputStream("src/test/resources/automaton.dot"));

    ContextFreeGrammar grammar =
        ContextFreeGrammarParser.read(new FileInputStream("src/test/resources/grammar.txt"));
    System.out.println("Entered grammar:");
    System.out.println(grammar);

    ContextFreeGrammar normalFormGrammar = grammar.toChomskyNormalForm();
    System.out.println("The grammar in Chomsky normal form:");
    System.out.println(normalFormGrammar);
  }
}
