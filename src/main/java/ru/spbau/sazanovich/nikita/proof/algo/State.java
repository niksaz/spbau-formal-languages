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

  MutableNode getNodeS() {
    return nodeS;
  }

  Symbol getSymbol() {
    return symbol;
  }

  MutableNode getNodeT() {
    return nodeT;
  }

  @Override
  public String toString() {
    return "[" + nodeS.label() + "," + symbol.getLabel() + "," + nodeT.label() + ']';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    State state = (State) o;

    return (nodeS != null ? nodeS.equals(state.nodeS) : state.nodeS == null)
        && (symbol != null ? symbol.equals(state.symbol) : state.symbol == null)
        && (nodeT != null ? nodeT.equals(state.nodeT) : state.nodeT == null);
  }

  @Override
  public int hashCode() {
    int result = nodeS != null ? nodeS.hashCode() : 0;
    result = 31 * result + (symbol != null ? symbol.hashCode() : 0);
    result = 31 * result + (nodeT != null ? nodeT.hashCode() : 0);
    return result;
  }
}