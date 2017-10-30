package ru.spbau.sazanovich.nikita.proof.grammar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public final class ContextFreeGrammarParser {
  private static final String WHITESPACE_REGEX = "\\s+";

  public static ContextFreeGrammar read(InputStream inputStream) throws IOException {
    ContextFreeGrammar grammar = new ContextFreeGrammar();
    try (
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))
    ) {
      String nextRule;
      while ((nextRule = reader.readLine()) != null) {
        String[] parts = nextRule.split(":");
        if (parts.length != 2) {
          throw new ParsingException("Rule: " + nextRule + " does not contain exactly two parts");
        }

        String[] leftParts = parts[0].split(WHITESPACE_REGEX);
        if (!((leftParts.length == 1) || (leftParts.length == 2 && leftParts[0].isEmpty()))) {
          throw new ParsingException(
              "Left part for rule: " + nextRule + " is different from one symbol");
        }
        Symbol trigger = Symbol.getSymbolFor(leftParts[leftParts.length - 1]);

        List<Symbol> result = new ArrayList<>();
        String[] rightParts = parts[1].split(WHITESPACE_REGEX);
        for (String rightPart : rightParts) {
          if (rightPart.isEmpty()) {
            continue;
          }
          result.add(Symbol.getSymbolFor(rightPart));
        }

        if (grammar.getInitial() == null) {
          grammar.setInitial(trigger);
        }
        grammar.addProduction(new Production(trigger, result));
      }
    }
    if (grammar.getInitial() == null) {
      throw new ParsingException("Initial nonterminal is not specified.");
    }
    return grammar;
  }
}
