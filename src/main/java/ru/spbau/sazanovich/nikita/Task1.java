package ru.spbau.sazanovich.nikita;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.BasicAutomata;

class Task1 {

  private static final Automaton automatonAOrBRepeat = BasicAutomata.makeCharSet("ab").repeat();

  private static final Automaton automatonAB = BasicAutomata.makeString("ab");

  static Automaton getAutomatonForSubtaskA() {
    Automaton firstAutomaton =
        automatonAOrBRepeat.concatenate(automatonAB).concatenate(automatonAOrBRepeat);
    Automaton secondAutomaton = automatonAOrBRepeat.concatenate(BasicAutomata.makeChar('a'));
    Automaton thirdAutomaton = BasicAutomata.makeChar('b').repeat();
    Automaton finalAutomaton = firstAutomaton.union(secondAutomaton).union(thirdAutomaton);
    finalAutomaton.minimize();
    return finalAutomaton;
  }

  static Automaton getAutomatonForSubtaskB() {
    Automaton automatonABOrBA =
        BasicAutomata.makeString("ab").union(BasicAutomata.makeString("ba"));
    Automaton firstAutomaton =
        automatonAOrBRepeat.concatenate(automatonABOrBA).concatenate(automatonAOrBRepeat);
    Automaton secondAutomaton = BasicAutomata.makeChar('a').repeat();
    Automaton thirdAutomaton = BasicAutomata.makeChar('b').repeat();
    Automaton finalAutomaton = firstAutomaton.union(secondAutomaton).union(thirdAutomaton);
    finalAutomaton.minimize();
    return finalAutomaton;
  }
}
