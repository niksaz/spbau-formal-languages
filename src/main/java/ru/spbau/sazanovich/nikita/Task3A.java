package ru.spbau.sazanovich.nikita;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.BasicAutomata;

class Task3A {

  static Automaton getAutomaton() {
    Automaton whitespaceAutomaton = UtilAutomatons.getWhitespaceAutomaton();
    Automaton integerWithWhitespacesAutomaton = UtilAutomatons.getIntegerWithWhitespacesAutomaton();
    Automaton semicolonAndIntegerWithWhitespacesAutomaton =
        BasicAutomata.makeChar(';')
            .concatenate(integerWithWhitespacesAutomaton);

    Automaton resultingAutomaton =
        BasicAutomata.makeChar('[')
            .concatenate(whitespaceAutomaton)
            .concatenate(
                integerWithWhitespacesAutomaton
                    .concatenate(semicolonAndIntegerWithWhitespacesAutomaton.repeat())
                    .optional())
            .concatenate(BasicAutomata.makeChar(']'));
    resultingAutomaton.minimize();

    return resultingAutomaton;
  }
}
