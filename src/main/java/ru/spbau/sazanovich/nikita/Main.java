package ru.spbau.sazanovich.nikita;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.BasicAutomata;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {

  private static final Set<Character> lettersSet = new HashSet<>();
  private static final Set<Character> digitsSet = new HashSet<>();
  private static final Set<Character> symbolsSet = new HashSet<>();
  private static final Set<Character> completeSet = new HashSet<>();

  private static final Set<String> keywords = new HashSet<>();

  static {
    for (char code = 'a'; code <= 'z'; code++) {
      lettersSet.add(code);
    }
    for (char code = '0'; code <= '9'; code++) {
      digitsSet.add(code);
    }
    symbolsSet.add('_');
    completeSet.addAll(lettersSet);
    completeSet.addAll(digitsSet);
    completeSet.addAll(symbolsSet);

    keywords.add("if");
    keywords.add("then");
    keywords.add("else");
    keywords.add("let");
    keywords.add("in");
    keywords.add("true");
    keywords.add("false");
  }

  public static void main(String[] args) {
    Automaton.setMinimization(Automaton.MINIMIZE_HUFFMAN);

    Automaton anyWordsAutomaton = BasicAutomata.makeCharSet(setToString(completeSet)).repeat();
    anyWordsAutomaton.minimize();

    Automaton identifierWithoutDigitsAutomaton =
        BasicAutomata.makeCharSet(
            setToString(
                completeSet.stream()
                    .filter(character -> !digitsSet.contains(character))
                    .collect(Collectors.toSet())))
        .concatenate(anyWordsAutomaton);
    identifierWithoutDigitsAutomaton.minimize();

    Automaton keywordsAutomaton =
        BasicAutomata.makeStringUnion(keywords.stream().toArray(String[]::new));
    keywordsAutomaton.minimize();

    Automaton resultAutomaton = identifierWithoutDigitsAutomaton.minus(keywordsAutomaton);
    resultAutomaton.minimize();

    System.out.println(anyWordsAutomaton.toDot());
    System.out.println(identifierWithoutDigitsAutomaton.toDot());
    System.out.println(keywordsAutomaton.toDot());
    System.out.println(resultAutomaton.toDot());
  }

  private static String setToString(Set<Character> characterSet) {
    StringBuilder builder = new StringBuilder();
    for (char character : characterSet) {
      builder.append(character);
    }
    return builder.toString();
  }
}
