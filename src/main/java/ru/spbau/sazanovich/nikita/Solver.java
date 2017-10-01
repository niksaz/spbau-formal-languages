package ru.spbau.sazanovich.nikita;

import dk.brics.automaton.Automaton;

public class Solver {

  public static void main(String[] args) {
    Automaton automaton1A = Task1.getAutomatonForSubtaskA();
    System.out.println(automaton1A.toDot());
    Automaton automaton1B = Task1.getAutomatonForSubtaskB();
    System.out.println(automaton1B.toDot());

    Automaton automaton3A = Task3A.getAutomaton();
    System.out.println(automaton3A.toDot());

    Automaton automaton3B = Task3B.getAutomaton();
    System.out.println(automaton3B.toDot());
  }
}
