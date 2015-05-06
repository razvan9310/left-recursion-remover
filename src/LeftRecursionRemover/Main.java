package LeftRecursionRemover;

import LeftRecursionRemover.Exception.GrammarException;
import LeftRecursionRemover.GrammarSyntax.NonTerminal;
import LeftRecursionRemover.GrammarSyntax.ProductionRule;
import LeftRecursionRemover.GrammarSyntax.Symbol;
import LeftRecursionRemover.GrammarSyntax.Terminal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by razvan on 5/5/15.
 */
public class Main {
  public static void main(String[] args) throws GrammarException {
    // A -> B
    // A -> $
    // B -> A
    NonTerminal A = new NonTerminal("A");
    Terminal a = new Terminal("a");
    Terminal b = new Terminal("b");

    ProductionRule p1 = new ProductionRule(Arrays.asList((Symbol) A), Arrays.asList(A, a));
    ProductionRule p2 = new ProductionRule(Arrays.asList((Symbol) A), Arrays.asList((Symbol) b));

    ArrayList<NonTerminal> nonTerminals = new ArrayList<NonTerminal>();
    nonTerminals.add(A);
    ArrayList<Terminal> terminals = new ArrayList<Terminal>();
    terminals.add(a);
    terminals.add(b);
    ArrayList<ProductionRule> productionRules = new ArrayList<ProductionRule>();
    productionRules.add(p1);
    productionRules.add(p2);

    ContextFreeGrammar CFG = new ContextFreeGrammar(nonTerminals, terminals, productionRules, A);

    CFG.removeImmediateLeftRecursion(A, "0");

    for (ProductionRule productionRule : CFG.productionRules()) {
      System.out.print(productionRule.leftMember().get(0).value() + " ->");
      for (Symbol symbol : productionRule.rightMember()) {
        System.out.print(" " + symbol.value());
      }
      System.out.println();
    }

//    ArrayList<ProductionRule> unreachable = CFG.removeUnreachableProductionRules();
//    System.out.println("Removed unreachable production rules:");
//    for (ProductionRule productionRule : unreachable) {
//      System.out.print(productionRule.leftMember().get(0).value() + " ->");
//      for (Symbol symbol : productionRule.rightMember()) {
//        System.out.print(" " + symbol.value());
//      }
//      System.out.println();
//    }

//    ArrayList<NonTerminal> cycle = CFG.getCycle();
//    System.out.print(cycle.get(0).value());
//    for (int i = 1; i < cycle.size(); ++i) {
//      System.out.print(" => " + cycle.get(i).value());
//    }
  }
}
