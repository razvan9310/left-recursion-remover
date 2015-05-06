package LeftRecursionRemover;

import LeftRecursionRemover.Exception.GrammarException;
import LeftRecursionRemover.GrammarSyntax.NonTerminal;
import LeftRecursionRemover.GrammarSyntax.ProductionRule;
import LeftRecursionRemover.GrammarSyntax.Symbol;
import LeftRecursionRemover.GrammarSyntax.Terminal;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by razvan on 5/5/15.
 */
public class Main {
  public static void main(String[] args) throws GrammarException {
    // A -> B
    // A -> $
    // B -> A
    NonTerminal A = new NonTerminal("A");
    NonTerminal B = new NonTerminal("B");
    Terminal empty = new Terminal(Terminal.EMPTY_VALUE);

    ProductionRule p1 = new ProductionRule(Arrays.asList((Symbol) A), Arrays.asList((Symbol) A));
    ProductionRule p2 = new ProductionRule(Arrays.asList((Symbol) A), Arrays.asList((Symbol) empty));
    ProductionRule p3 = new ProductionRule(Arrays.asList((Symbol) B), Arrays.asList((Symbol) A));

    ArrayList<NonTerminal> nonTerminals = new ArrayList<NonTerminal>();
    nonTerminals.add(A);
    nonTerminals.add(B);
    ArrayList<Terminal> terminals = new ArrayList<Terminal>();
    terminals.add(empty);
    ArrayList<ProductionRule> productionRules = new ArrayList<ProductionRule>();
    productionRules.add(p1);
    productionRules.add(p2);
    productionRules.add(p3);

    ContextFreeGrammar CFG = new ContextFreeGrammar(nonTerminals, terminals, productionRules, A);

    ArrayList<ProductionRule> unreachable = CFG.removeUnreachableProductionRules();
    System.out.println("Removed unreachable production rules:");
    for (ProductionRule productionRule : unreachable) {
      System.out.print(productionRule.leftMember().get(0).value() + " ->");
      for (Symbol symbol : productionRule.rightMember()) {
        System.out.print(" " + symbol.value());
      }
      System.out.println();
    }

//    ArrayList<NonTerminal> cycle = CFG.getCycle();
//    System.out.print(cycle.get(0).value());
//    for (int i = 1; i < cycle.size(); ++i) {
//      System.out.print(" => " + cycle.get(i).value());
//    }
  }
}
