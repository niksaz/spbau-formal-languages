package ru.spbau.sazanovich.nikita.proof.algo;

import guru.nidi.graphviz.model.MutableNode;
import ru.spbau.sazanovich.nikita.proof.grammar.Symbol;

class State {
  static State of(MutableNode nodeS, Symbol symbol, MutableNode nodeT) {
    return new State(nodeS, symbol, nodeT);
  }

  private final MutableNode nodeS;
  private final Symbol symbol;
  private final MutableNode nodeT;

  private State(MutableNode nodeS, Symbol symbol, MutableNode nodeT) {
    this.nodeS = nodeS;
    this.symbol = symbol;
    this.nodeT = nodeT;
  }

  public MutableNode getNodeS() {
    return nodeS;
  }

  public Symbol getSymbol() {
    return symbol;
  }

  public MutableNode getNodeT() {
    return nodeT;
  }

  @Override
  public String toString() {
    return "[" + nodeS.label() + ", " + symbol.getLabel() + ", " + nodeT.label() + ']';
  }
}