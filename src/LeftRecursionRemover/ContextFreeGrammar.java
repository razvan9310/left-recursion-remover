package LeftRecursionRemover;

import LeftRecursionRemover.Exception.ContextException;
import LeftRecursionRemover.Exception.GrammarException;
import LeftRecursionRemover.Exception.ProductionRuleException;
import LeftRecursionRemover.Exception.SymbolException;
import LeftRecursionRemover.GrammarSyntax.NonTerminal;
import LeftRecursionRemover.GrammarSyntax.ProductionRule;
import LeftRecursionRemover.GrammarSyntax.Symbol;
import LeftRecursionRemover.GrammarSyntax.Terminal;

import java.util.*;

/**
 * Created by razvan on 5/5/15.
 */
public class ContextFreeGrammar extends Grammar {
  public ContextFreeGrammar(ArrayList<NonTerminal> nonTerminals, ArrayList<Terminal> terminals,
      ArrayList<ProductionRule> productionRules,
      NonTerminal startingSymbol) throws GrammarException {
    super(nonTerminals, terminals, productionRules, startingSymbol);
    for (ProductionRule productionRule : productionRules) {
      if (!productionRule.isContextFree()) {
        throw new ContextException("Grammar is not context-free!");
      }
    }
  }

  private void removeUnreachableNonTerminals() {
    Iterator<NonTerminal> nonTerminalIterator = mNonTerminals.iterator();
    while (nonTerminalIterator.hasNext()) {
      NonTerminal nonTerminal = nonTerminalIterator.next();
      boolean isReachable = false;
      for (ProductionRule productionRule : mProductionRules) {
        if (productionRule.rightMember().contains(nonTerminal)) {
          isReachable = true;
          break;
        }
      }
      if (!isReachable) {
        nonTerminalIterator.remove();
      }
    }
  }

  public ArrayList<ProductionRule> removeUnreachableProductionRules() {
    removeUnreachableNonTerminals();
    ArrayList<ProductionRule> removedProductionRules = new ArrayList<ProductionRule>();

    Iterator<ProductionRule> productionRuleIterator = mProductionRules.iterator();
    while (productionRuleIterator.hasNext()) {
      ProductionRule productionRule = productionRuleIterator.next();
      NonTerminal leftMember = (NonTerminal) productionRule.leftMember().get(0);
      if (!mNonTerminals.contains(leftMember)) {
        try {
          removedProductionRules.add(new ProductionRule(productionRule));
        } catch (ProductionRuleException e) {
          System.err.println(e.getMessage());
        } finally {
          productionRuleIterator.remove();
        }
      }
    }
    return removedProductionRules;
  }

  private ArrayList<NonTerminal> depthFirstSearch(NonTerminal current, HashSet<NonTerminal> visited,
      ArrayList<NonTerminal> stack) {
    visited.add(current);
    for (ProductionRule productionRule : mProductionRules) {
      if (!productionRule.leftMember().get(0).equals(current)) {
        continue;
      }
      if (productionRule.rightMember().size() != 1) {
        continue;
      }
      if (productionRule.rightMember().get(0).type() != Symbol.TYPE_NON_TERMINAL) {
        continue;
      }
      NonTerminal next = (NonTerminal) productionRule.rightMember().get(0);
      stack.add(next);
      if (visited.contains(next)) {
        return stack;
      }
      return depthFirstSearch(next, visited, stack);
    }
    return stack;
  }

  public ArrayList<NonTerminal> getCycle() {
    for (NonTerminal nonTerminal : mNonTerminals) {
      HashSet<NonTerminal> visited = new HashSet<NonTerminal>();
      ArrayList<NonTerminal> stack = new ArrayList<NonTerminal>();
      stack.add(nonTerminal);
      stack = depthFirstSearch(nonTerminal, visited, stack);
      if (stack.size() > 1 && stack.get(0).equals(stack.get(stack.size() - 1))) {
        return stack;
      }
    }
    return null;
  }

  public void removeImmediateLeftRecursion(NonTerminal nonTerminal, String newNonTerminalSuffix) {
    if (nonTerminal == null) {
      return;
    }
    ArrayList<ProductionRule> leftRecursiveProductions = new ArrayList<ProductionRule>();
    ArrayList<ProductionRule> nonLeftRecursiveProductions = new ArrayList<ProductionRule>();
    for (ProductionRule productionRule : mProductionRules) {
      if (!nonTerminal.equals(productionRule.leftMember().get(0))) {
        continue;
      }
      if (nonTerminal.equals(productionRule.rightMember().get(0))) {
        leftRecursiveProductions.add(productionRule);
      } else {
        nonLeftRecursiveProductions.add(productionRule);
      }
    }
    if (leftRecursiveProductions.isEmpty()) {
      return;
    }

    NonTerminal newNonTerminal;
    try {
      newNonTerminal = new NonTerminal(
          NonTerminal.DEFAULT_NEW_NONTERMINAL_VALUE + newNonTerminalSuffix);
    } catch (SymbolException e) {
      System.err.println("A supernatural error occurred.");
      return;
    }

    ArrayList<ProductionRule> newProductionRules = new ArrayList<ProductionRule>();
    for (ProductionRule productionRule : nonLeftRecursiveProductions) {
      ArrayList<Symbol> newRightMember = new ArrayList<Symbol>();
      for (Symbol symbol : productionRule.rightMember()) {
        newRightMember.add(symbol);
      }
      newRightMember.add(newNonTerminal);
      try {
        newProductionRules.add(
            new ProductionRule(Arrays.asList((Symbol) nonTerminal), newRightMember));
      } catch (ProductionRuleException e) {
        System.err.println("A supernatural error occurred.");
      }
    }

    try {
      newProductionRules.add(new ProductionRule(Arrays.asList((Symbol) newNonTerminal),
          Arrays.asList((Symbol) new Terminal(Terminal.EMPTY_VALUE))));
    } catch (GrammarException e) {
      System.err.println("A supernatural error occurred.");
    }

    for (ProductionRule productionRule : leftRecursiveProductions) {
      ArrayList<Symbol> newRightMember = new ArrayList<Symbol>();
      for (int i = 1; i < productionRule.rightMember().size(); ++i) {
        newRightMember.add(productionRule.rightMember().get(i));
      }
      newRightMember.add(newNonTerminal);
      try {
        newProductionRules.add(
            new ProductionRule(Arrays.asList((Symbol) newNonTerminal), newRightMember));
      } catch (ProductionRuleException e) {
        System.err.println("A supernatural error occurred.");
      }
    }

    Iterator<ProductionRule> productionRuleIterator = mProductionRules.iterator();
    while (productionRuleIterator.hasNext()) {
      ProductionRule productionRule = productionRuleIterator.next();
      if (nonTerminal.equals(productionRule.leftMember().get(0))) {
        productionRuleIterator.remove();
      }
    }
    mProductionRules.addAll(newProductionRules);
  }

  public List<ProductionRule> productionRules() {
    return mProductionRules;
  }
}
