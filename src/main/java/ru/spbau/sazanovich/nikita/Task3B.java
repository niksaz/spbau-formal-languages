package ru.spbau.sazanovich.nikita;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.BasicAutomata;

class Task3B {

  static Automaton getAutomaton() {
    Automaton integerAutomaton = UtilAutomatons.getIntegerAutomaton();
    Automaton integerListAutomaton = Task3A.getAutomaton();
    Automaton identifierAutomaton = UtilAutomatons.getIdentifierAutomaton();

    Automaton whitespaceAutomaton = UtilAutomatons.getWhitespaceAutomaton();
    Automaton expressionUnitAutomaton =
        whitespaceAutomaton
            .concatenate(
                integerAutomaton
                    .union(integerListAutomaton)
                    .union(identifierAutomaton))
            .concatenate(whitespaceAutomaton);

    Automaton resultingAutomaton =
        BasicAutomata.makeChar('(')
            .concatenate(whitespaceAutomaton)
            .concatenate(
                expressionUnitAutomaton
                    .concatenate(
                        BasicAutomata.makeChar(',')
                            .concatenate(expressionUnitAutomaton)
                            .repeat())
                    .optional())
            .concatenate(BasicAutomata.makeChar(')'));
    resultingAutomaton.minimize();

    return resultingAutomaton;
  }
}