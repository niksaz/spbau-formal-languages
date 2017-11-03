package ru.spbau.sazanovich.nikita.proof.algo;

import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;
import org.junit.Test;
import ru.spbau.sazanovich.nikita.proof.grammar.ContextFreeGrammar;
import ru.spbau.sazanovich.nikita.proof.grammar.ContextFreeGrammarParser;

import java.io.FileInputStream;

import static com.google.common.truth.Truth.assertThat;

/** Integration test for {@link IntersectionGrammarBuilder}. */
public class IntersectionGrammarBuilderTest {
  @Test
  public void testIntersectionGrammar() throws Exception {
    MutableGraph automaton = Parser.read(new FileInputStream("src/test/resources/automaton.dot"));
    ContextFreeGrammar grammar =
        ContextFreeGrammarParser.read(new FileInputStream("src/test/resources/grammar.txt"));
    ContextFreeGrammar normalizedGrammar = grammar.toChomskyNormalForm();

    IntersectionGrammarBuilder intersectionBuilder =
        new IntersectionGrammarBuilder(normalizedGrammar, automaton);
    ContextFreeGrammar intersectionGrammar = intersectionBuilder.build();
    ContextFreeGrammar expectedGrammar =
        ContextFreeGrammarParser.read(new FileInputStream("src/test/resources/expected.txt"));
    assertThat(intersectionGrammar).isEqualTo(expectedGrammar);
  }
}
