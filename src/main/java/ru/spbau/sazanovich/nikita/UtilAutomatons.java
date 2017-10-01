package ru.spbau.sazanovich.nikita;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.BasicAutomata;

class UtilAutomatons {

  static Automaton getWhitespaceAutomaton() {
    return BasicAutomata.makeChar('_').repeat();
  }

  static Automaton getIntegerAutomaton() {
    Automaton digitsAutomaton = BasicAutomata.makeCharRange('0', '9');
    return digitsAutomaton.concatenate(digitsAutomaton.repeat());
  }

  static Automaton getIdentifierAutomaton() {
    Automaton letterAutomaton = BasicAutomata.makeCharRange('a', 'z');
    return letterAutomaton.concatenate(letterAutomaton.repeat());
  }

  static Automaton getIntegerWithWhitespacesAutomaton() {
    Automaton whitespaceAutomaton = getWhitespaceAutomaton();
    Automaton integerAutomaton = getIntegerAutomaton();
    return whitespaceAutomaton.concatenate(integerAutomaton).concatenate(whitespaceAutomaton);
  }
}
