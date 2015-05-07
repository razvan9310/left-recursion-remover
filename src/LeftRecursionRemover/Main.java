package LeftRecursionRemover;

import LeftRecursionRemover.Exception.GrammarException;
import LeftRecursionRemover.Exception.SymbolException;
import LeftRecursionRemover.GrammarSyntax.NonTerminal;
import LeftRecursionRemover.GrammarSyntax.ProductionRule;
import LeftRecursionRemover.GrammarSyntax.Symbol;
import LeftRecursionRemover.GrammarSyntax.Terminal;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

/**
 * Created by razvan on 5/5/15.
 */
public class Main {
  public static final int EXIT_CODE_EXCEPTION = 1;
  public static final int EXIT_CODE_CYCLIC_GRAMMAR = 2;
  public static final int EXIT_CODE_EMPTY_PRODUCTION = 3;

  public static void printProductionRule(ProductionRule productionRule, PrintStream out) {
    for (Symbol symbol : productionRule.leftMember()) {
      out.print(symbol.value() + " ");
    }
    out.print("->");
    for (Symbol symbol : productionRule.rightMember()) {
      out.print(" " + symbol.value());
    }
    out.println();
  }

  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    ArrayList<NonTerminal> nonTerminals = new ArrayList<>();
    System.out.print("Non-terminals (space-separated): ");
    String nextLine = in.nextLine();
    for (String token : nextLine.split(" ")) {
      try {
        NonTerminal nonTerminal = new NonTerminal(token);
        if (!nonTerminals.contains(nonTerminal)) {
          nonTerminals.add(nonTerminal);
        }
      } catch (SymbolException e) {
        System.out.println(e.getMessage());
        System.exit(EXIT_CODE_EXCEPTION);
      }
    }

    ArrayList<Terminal> terminals = new ArrayList<>();
    System.out.print("Terminals (space-separated): ");
    nextLine = in.nextLine();
    for (String token : nextLine.split(" ")) {
      try {
        Terminal terminal = new Terminal(token);
        if (!terminals.contains(terminal)) {
          terminals.add(terminal);
        }
      } catch (SymbolException e) {
        System.out.println(e.getMessage());
        System.exit(EXIT_CODE_EXCEPTION);
      }
    }

    System.out.print("No. of production rules: ");
    int productionRulesCount = in.nextInt();
    ArrayList<ProductionRule> productionRules = new ArrayList<>(productionRulesCount);
    System.out.println(
        "Production rules (one per line, elements space-separated; '$' is the empty terminal): ");
    in.nextLine();
    for (int i = 0; i < productionRulesCount; ++i) {
      System.out.print(String.valueOf(i + 1) + ". ");
      String[] tokens = in.nextLine().split(" ");
      try {
        ArrayList<Symbol> leftMember = new ArrayList<>();
        ArrayList<Symbol> rightMember = new ArrayList<>();
        boolean foundProductionSeparator = false;

        for (String token : tokens) {
          if (ProductionRule.PRODUCTION_RULE_SEPARATOR.equals(token)) {
            foundProductionSeparator = true;
            continue;
          }

          NonTerminal tokenAsNonTerminal = new NonTerminal(token);
          Terminal tokenAsTerminal = new Terminal(token);
          if (foundProductionSeparator) {
            if (nonTerminals.contains(tokenAsNonTerminal)) {
              rightMember.add(tokenAsNonTerminal);
            } else {
              rightMember.add(tokenAsTerminal);
            }
          } else {
            if (nonTerminals.contains(tokenAsNonTerminal)) {
              leftMember.add(tokenAsNonTerminal);
            } else {
              leftMember.add(tokenAsTerminal);
            }
          }
        }

        if (!foundProductionSeparator) {
          System.out.println("\nMalformed production rule (sides should be separated with \"->\"");
          System.exit(EXIT_CODE_EXCEPTION);
        }
        ProductionRule productionRule = new ProductionRule(leftMember, rightMember);
        if (!productionRules.contains(productionRule)) {
          productionRules.add(productionRule);
        }
      } catch (GrammarException e) {
        System.out.println(e.getMessage());
        System.exit(EXIT_CODE_EXCEPTION);
      }
    }

    NonTerminal startingSymbol = null;
    System.out.print("Starting symbol: ");
    try {
      startingSymbol = new NonTerminal(in.next());
    } catch (SymbolException e) {
      System.out.println(e.getMessage());
      System.exit(EXIT_CODE_EXCEPTION);
    }
    System.out.println();

    ContextFreeGrammar CFG = null;
    try {
      CFG = new ContextFreeGrammar(nonTerminals, terminals, productionRules, startingSymbol);
    } catch (GrammarException e) {
      System.out.println(e.getMessage());
      System.exit(EXIT_CODE_EXCEPTION);
    }

    List<ProductionRule> unreachableProductionRules = CFG.removeUnreachableProductionRules();
    if (!unreachableProductionRules.isEmpty()) {
      System.out.println("Removed following unreachable productions:");
      for (ProductionRule unreachable : unreachableProductionRules) {
        printProductionRule(unreachable, System.out);
      }
    }

    List<NonTerminal> cycle = CFG.getCycle();
    if (cycle != null) {
      System.out.println("The grammar contains a cycle:");
      System.out.print(cycle.get(0).value());
      for (int i = 1; i < cycle.size(); ++i) {
        System.out.print(" => " + cycle.get(i).value());
      }
      System.out.println("\nCannot apply left recursion removal. The program will stop.");
      System.exit(EXIT_CODE_CYCLIC_GRAMMAR);
    }

//    for (ProductionRule productionRule : productionRules) {
//      if (productionRule.rightMember().size() == 1
//          && productionRule.rightMember().get(0).type() == Symbol.TYPE_TERMINAL
//          && ((Terminal) productionRule.rightMember().get(0)).isEmptyTerminal()) {
//        System.out.println("The grammar has an empty production - The program will only attempt" +
//            " to remove immediate left recursion.");
//        HashSet<NonTerminal> nonTerminalsSet = new HashSet<>();
//        for (ProductionRule productionRule1 : productionRules) {
//          if (productionRule1.isLeftRecursive()) {
//            nonTerminalsSet.add((NonTerminal) productionRule1.leftMember().get(0));
//          }
//        }
//        int newNonTerminalIndex = 0;
//        for (NonTerminal nonTerminal : nonTerminalsSet) {
//          try {
//            CFG.removeImmediateLeftRecursion(nonTerminal, String.valueOf(newNonTerminalIndex));
//            ++newNonTerminalIndex;
//          } catch (GrammarException e) {
//            System.out.println(e.getMessage());
//            System.exit(EXIT_CODE_EXCEPTION);
//          }
//        }
//
//        for (ProductionRule productionRule1 : CFG.productionRules()) {
//          printProductionRule(productionRule1, System.out);
//        }
//        System.exit(EXIT_CODE_EMPTY_PRODUCTION);
//      }
//    }

    System.out.println("Grammar without left recursion:");
    try {
      CFG.removeLeftRecursion();
    } catch (GrammarException e) {
      System.out.println(e.getMessage());
      System.exit(EXIT_CODE_EXCEPTION);
    }
    for (ProductionRule productionRule : CFG.productionRules()) {
      printProductionRule(productionRule, System.out);
    }

//    // A -> B x y | x
//    // B -> C D
//    // C -> A | c
//    // D -> d
//    NonTerminal A = new NonTerminal("A");
//    NonTerminal B = new NonTerminal("B");
//    NonTerminal C = new NonTerminal("C");
//    NonTerminal D = new NonTerminal("D");
//    Terminal x = new Terminal("x");
//    Terminal y = new Terminal("y");
//    Terminal c = new Terminal("c");
//    Terminal d = new Terminal("d");
//
//    ProductionRule p1 = new ProductionRule(Arrays.asList((Symbol) A), Arrays.asList(B, x, y));
//    ProductionRule p2 = new ProductionRule(Arrays.asList((Symbol) A), Arrays.asList((Symbol) x));
//    ProductionRule p3 = new ProductionRule(Arrays.asList((Symbol) B), Arrays.asList(C, D));
//    ProductionRule p4 = new ProductionRule(Arrays.asList((Symbol) C), Arrays.asList((Symbol) A));
//    ProductionRule p5 = new ProductionRule(Arrays.asList((Symbol) C), Arrays.asList((Symbol) c));
//    ProductionRule p6 = new ProductionRule(Arrays.asList((Symbol) D), Arrays.asList((Symbol) d));
//
//    ArrayList<NonTerminal> nonTerminals = new ArrayList<>();
//    nonTerminals.add(A);
//    nonTerminals.add(B);
//    nonTerminals.add(C);
//    nonTerminals.add(D);
//    ArrayList<Terminal> terminals = new ArrayList<>();
//    terminals.add(x);
//    terminals.add(y);
//    terminals.add(c);
//    terminals.add(d);
//    ArrayList<ProductionRule> productionRules = new ArrayList<>();
//    productionRules.add(p1);
//    productionRules.add(p2);
//    productionRules.add(p3);
//    productionRules.add(p4);
//    productionRules.add(p5);
//    productionRules.add(p6);
//
//    ContextFreeGrammar CFG = new ContextFreeGrammar(nonTerminals, terminals, productionRules, A);
//
//    CFG.removeLeftRecursion();
//
////    CFG.removeImmediateLeftRecursion(A, "0");
////
//    for (ProductionRule productionRule : CFG.productionRules()) {
//      System.out.print(productionRule.leftMember().get(0).value() + " ->");
//      for (Symbol symbol : productionRule.rightMember()) {
//        System.out.print(" " + symbol.value());
//      }
//      System.out.println();
//    }
//
////    ArrayList<ProductionRule> unreachable = CFG.removeUnreachableProductionRules();
////    System.out.println("Removed unreachable production rules:");
////    for (ProductionRule productionRule : unreachable) {
////      System.out.print(productionRule.leftMember().get(0).value() + " ->");
////      for (Symbol symbol : productionRule.rightMember()) {
////        System.out.print(" " + symbol.value());
////      }
////      System.out.println();
////    }
//
////    ArrayList<NonTerminal> cycle = CFG.getCycle();
////    System.out.print(cycle.get(0).value());
////    for (int i = 1; i < cycle.size(); ++i) {
////      System.out.print(" => " + cycle.get(i).value());
////    }
  }
}
