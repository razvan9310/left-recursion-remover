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
    // A -> B x y | x
    // B -> C D
    // C -> A | c
    // D -> d
    NonTerminal A = new NonTerminal("A");
    NonTerminal B = new NonTerminal("B");
    NonTerminal C = new NonTerminal("C");
    NonTerminal D = new NonTerminal("D");
    Terminal x = new Terminal("x");
    Terminal y = new Terminal("y");
    Terminal c = new Terminal("c");
    Terminal d = new Terminal("d");

    ProductionRule p1 = new ProductionRule(Arrays.asList((Symbol) A), Arrays.asList(B, x, y));
    ProductionRule p2 = new ProductionRule(Arrays.asList((Symbol) A), Arrays.asList((Symbol) x));
    ProductionRule p3 = new ProductionRule(Arrays.asList((Symbol) B), Arrays.asList(C, D));
    ProductionRule p4 = new ProductionRule(Arrays.asList((Symbol) C), Arrays.asList((Symbol) A));
    ProductionRule p5 = new ProductionRule(Arrays.asList((Symbol) C), Arrays.asList((Symbol) c));
    ProductionRule p6 = new ProductionRule(Arrays.asList((Symbol) D), Arrays.asList((Symbol) d));

    ArrayList<NonTerminal> nonTerminals = new ArrayList<>();
    nonTerminals.add(A);
    nonTerminals.add(B);
    nonTerminals.add(C);
    nonTerminals.add(D);
    ArrayList<Terminal> terminals = new ArrayList<>();
    terminals.add(x);
    terminals.add(y);
    terminals.add(c);
    terminals.add(d);
    ArrayList<ProductionRule> productionRules = new ArrayList<>();
    productionRules.add(p1);
    productionRules.add(p2);
    productionRules.add(p3);
    productionRules.add(p4);
    productionRules.add(p5);
    productionRules.add(p6);

    ContextFreeGrammar CFG = new ContextFreeGrammar(nonTerminals, terminals, productionRules, A);

    CFG.removeLeftRecursion();

//    CFG.removeImmediateLeftRecursion(A, "0");
//
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
