package ru.spbau.sazanovich.nikita.proof;

import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import guru.nidi.graphviz.parse.Parser;

import java.io.FileInputStream;
import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {
    MutableGraph g = Parser.read(
        new FileInputStream("src/test/resources/input.dot"));

    for (MutableNode node : g.nodes()) {
      System.out.println(node);
    }
  }
}
