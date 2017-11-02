package ru.spbau.sazanovich.nikita.proof.algo;

import guru.nidi.graphviz.attribute.MutableAttributed;
import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import guru.nidi.graphviz.model.MutableNodePoint;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.spbau.sazanovich.nikita.proof.grammar.ContextFreeGrammar;
import ru.spbau.sazanovich.nikita.proof.grammar.Production;
import ru.spbau.sazanovich.nikita.proof.grammar.Symbol;

import java.util.*;
import java.util.stream.Collectors;

public final class IntersectionGrammarBuilder {
  private static final String START_NODE_COLOR = "red";
  private static final String TERMINAL_NODE_COLOR = "green";

  private final Set<State> states = new HashSet<>();
  private final Queue<State> stateQueue = new LinkedList<>();
  private final ContextFreeGrammar grammar;
  private final MutableGraph automaton;

  private ContextFreeGrammar intersectionGrammar = null;

  public IntersectionGrammarBuilder(ContextFreeGrammar grammar, MutableGraph automaton) {
    this.grammar = grammar;
    this.automaton = automaton;
  }

  public ContextFreeGrammar build() {
    if (intersectionGrammar != null) {
      return intersectionGrammar;
    }
    intersectionGrammar = new ContextFreeGrammar();
    generateInitialProductions();

    Map<String, Set<Symbol>> terminals = computeTerminals();
    Collection<MutableNode> nodes = automaton.nodes();
    for (MutableNode node : nodes) {
      for (Link link : node.links()) {
        String label = linkGetLabel(link);
        MutableNode nodeS = linkFromNode(link);
        MutableNode nodeF = linkToNode(link);
        for (Symbol symbol : terminals.get(label)) {
          reachState(State.of(nodeS, symbol, nodeF));
        }
      }
    }
    return intersectionGrammar;
  }

  private void reachState(State state) {
    if (!states.contains(state)) {
      states.add(state);
      stateQueue.add(state);
      System.out.println("Added " + state);
    }
  }

  private void generateInitialProductions() {
    Symbol intersectionInitialNode = Symbol.getSymbolFor("S");
    intersectionGrammar.setInitial(intersectionInitialNode);
    Symbol initialNode = grammar.getInitial();
    MutableNode automatonStartNode = findStartNode();
    List<MutableNode> terminalNodes = findTerminalNodes();
    for (MutableNode automatonTerminalNode : terminalNodes) {
      State state = State.of(automatonStartNode, initialNode, automatonTerminalNode);
      intersectionGrammar.addProduction(
          new Production(
              intersectionInitialNode,
              Collections.singletonList(Symbol.getInternalSymbolFor(state.toString()))));
    }
  }

  @NotNull
  private MutableNode findStartNode() {
    List<MutableNode> startNodes = findNodesColored(START_NODE_COLOR);
    if (startNodes.isEmpty()) {
      throw new IllegalArgumentException("No start nodes.");
    }
    if (startNodes.size() > 1) {
      throw new IllegalArgumentException("More than one start node.");
    }
    return startNodes.get(0);
  }

  @NotNull
  private List<MutableNode> findTerminalNodes() {
    List<MutableNode> terminalNodes = findNodesColored(TERMINAL_NODE_COLOR);
    if (terminalNodes.isEmpty()) {
      throw new IllegalArgumentException("No terminal nodes.");
    }
    return terminalNodes;
  }

  private List<MutableNode> findNodesColored(@NotNull String wantedColor) {
    return automaton.nodes().stream()
        .filter(node -> wantedColor.equals(nodeGetColor(node)))
        .collect(Collectors.toList());
  }

  private Map<String,Set<Symbol>> computeTerminals() {
    Map<String, Set<Symbol>> terminals = new HashMap<>();
    grammar.getSymbolProductionMap().forEach((trigger, productions) -> {
      for (Production production : productions) {
        List<Symbol> products = production.getProducts();
        if (products.size() != 1) {
          continue;
        }
        Symbol product = products.get(0);
        if (product.isTerminal()) {
          terminals.putIfAbsent(product.getLabel(), new HashSet<>());
          terminals.get(product.getLabel()).add(trigger);
        }
      }
    });
    return terminals;
  }

  @Nullable
  private static String nodeGetColor(MutableNode node) {
    return getAttrValue(node.attrs(), "color");
  }

  @NotNull
  private static String linkGetLabel(Link link) {
    String label = getAttrValue(link.attrs(), "label");
    if (label == null) {
      throw new IllegalArgumentException("Link without a label!");
    }
    return label;
  }

  @Nullable
  private static <T> String getAttrValue(MutableAttributed<T> attrs, String label) {
    for (Map.Entry<String, Object> entry : attrs) {
      if (entry.getKey().equals(label)) {
        return entry.getValue().toString();
      }
    }
    return null;
  }

  private static MutableNode linkFromNode(Link link) {
    return ((MutableNodePoint) link.from()).node();
  }

  private static MutableNode linkToNode(Link link) {
    return ((MutableNodePoint) link.to()).node();
  }
}
