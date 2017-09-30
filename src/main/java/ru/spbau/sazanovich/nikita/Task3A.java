package ru.spbau.sazanovich.nikita;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.BasicAutomata;

class Task3A {

  static Automaton getAutomaton() {
    Automaton whitespaceAutomaton = BasicAutomata.makeChar('_').repeat();
    Automaton digitsAutomaton = BasicAutomata.makeCharRange('0', '9');
    Automaton integerAutomaton = digitsAutomaton.repeat(1);
    Automaton integerWithWhitespacesAutomaton =
        whitespaceAutomaton.concatenate(integerAutomaton).concatenate(whitespaceAutomaton);
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
