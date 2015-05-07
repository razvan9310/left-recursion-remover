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
import java.util.stream.Collectors;

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
    ArrayList<NonTerminal> queue = new ArrayList<>();
    HashSet<String> visited = new HashSet<>();
    queue.add(mStartingSymbol);
    visited.add(mStartingSymbol.value());
    int currentQueuePosition = 0;

    while (currentQueuePosition < queue.size()) {
      NonTerminal currentNonTerminal = queue.get(currentQueuePosition);
      for (ProductionRule productionRule : mProductionRules) {
        if (!currentNonTerminal.equals(productionRule.leftMember().get(0))) {
          continue;
        }
        for (Symbol symbol : productionRule.rightMember()) {
          if (symbol.type() == Symbol.TYPE_NON_TERMINAL && !visited.contains(symbol.value())) {
            visited.add(symbol.value());
            queue.add((NonTerminal) symbol);
          }
        }
      }
      ++currentQueuePosition;
    }

    Iterator<NonTerminal> nonTerminalIterator = mNonTerminals.iterator();
    while (nonTerminalIterator.hasNext()) {
      NonTerminal nonTerminal = nonTerminalIterator.next();
      if (!visited.contains(nonTerminal.value())) {
        nonTerminalIterator.remove();
      }
    }
  }

  public ArrayList<ProductionRule> removeUnreachableProductionRules() {
    removeUnreachableNonTerminals();
    ArrayList<ProductionRule> removedProductionRules = new ArrayList<>();

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

  private ArrayList<NonTerminal> depthFirstSearch(NonTerminal current, HashSet<String> visited,
      ArrayList<NonTerminal> stack) {
    visited.add(current.value());
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
      if (visited.contains(next.value())) {
        return stack;
      }
      return depthFirstSearch(next, visited, stack);
    }
    return stack;
  }

  public ArrayList<NonTerminal> getCycle() {
    for (NonTerminal nonTerminal : mNonTerminals) {
      HashSet<String> visited = new HashSet<>();
      ArrayList<NonTerminal> stack = new ArrayList<>();
      stack.add(nonTerminal);
      stack = depthFirstSearch(nonTerminal, visited, stack);
      if (stack.size() > 1 && stack.get(0).equals(stack.get(stack.size() - 1))) {
        return stack;
      }
    }
    return null;
  }

  public void removeImmediateLeftRecursion(NonTerminal nonTerminal, String newNonTerminalSuffix)
      throws GrammarException {
    if (nonTerminal == null) {
      return;
    }
    ArrayList<ProductionRule> leftRecursiveProductions = new ArrayList<>();
    ArrayList<ProductionRule> nonLeftRecursiveProductions = new ArrayList<>();
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

    NonTerminal newNonTerminal = new NonTerminal(
        NonTerminal.DEFAULT_NEW_NONTERMINAL_VALUE + newNonTerminalSuffix);

    ArrayList<ProductionRule> newProductionRules = new ArrayList<>();
    for (ProductionRule productionRule : nonLeftRecursiveProductions) {
      ArrayList<Symbol> newRightMember = new ArrayList<>();
      newRightMember.addAll(productionRule.rightMember());
      newRightMember.add(newNonTerminal);
      newProductionRules.add(
          new ProductionRule(Arrays.asList((Symbol) nonTerminal), newRightMember));
    }

    newProductionRules.add(new ProductionRule(Arrays.asList((Symbol) newNonTerminal),
        Arrays.asList((Symbol) new Terminal(Terminal.EMPTY_VALUE))));

    for (ProductionRule productionRule : leftRecursiveProductions) {
      ArrayList<Symbol> newRightMember = new ArrayList<>();
      for (int i = 1; i < productionRule.rightMember().size(); ++i) {
        newRightMember.add(productionRule.rightMember().get(i));
      }
      newRightMember.add(newNonTerminal);

      if (newRightMember.size() > 1) {
        Iterator<Symbol> symbolIterator = newRightMember.iterator();
        while (symbolIterator.hasNext()) {
          Symbol symbol = symbolIterator.next();
          if (Terminal.EMPTY_VALUE.equals(symbol.value())) {
            symbolIterator.remove();
          }
        }
      }

      newProductionRules.add(
          new ProductionRule(Arrays.asList((Symbol) newNonTerminal), newRightMember));
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

  public void removeLeftRecursion() throws GrammarException {
    for (int i = 0; i < mNonTerminals.size(); ++i) {
      // Ai:
      NonTerminal iNonTerminal = mNonTerminals.get(i);
      for (int j = 0; j < i; ++j) {
        // Aj:
        NonTerminal jNonTerminal = mNonTerminals.get(j);
        // Aj -> a1 | a2 | ... | ak:
        List<ProductionRule> jProductionRules = mProductionRules.stream()
            .filter(productionRule -> jNonTerminal.equals(productionRule.leftMember().get(0)))
            .collect(Collectors.toList());

        ArrayList<ProductionRule> newProductionRules = new ArrayList<>();
        Iterator<ProductionRule> productionRuleIterator = mProductionRules.iterator();

        while (productionRuleIterator.hasNext()) {
          ProductionRule productionRule = productionRuleIterator.next();
          if (!iNonTerminal.equals(productionRule.leftMember().get(0))
              || !jNonTerminal.equals(productionRule.rightMember().get(0))) {
            continue;
          }
          // Ai -> Aj b:
          for (ProductionRule jProductionRule : jProductionRules) {
            ArrayList<Symbol> newRightMember = new ArrayList<>();

            for (Symbol symbol : jProductionRule.rightMember()) {
              if (!Terminal.EMPTY_VALUE.equals(symbol.value())/*
                  || jProductionRule.rightMember().size() == 1*/) {
                newRightMember.add(symbol);
              }
            }

            for (int k = 1; k < productionRule.rightMember().size(); ++k) {
              if (!Terminal.EMPTY_VALUE.equals(productionRule.rightMember().get(k).value())) {
                newRightMember.add(productionRule.rightMember().get(k));
              }
            }

            // Ai -> a1 b | a2 b | ... | ak b:
            if (newRightMember.isEmpty()) {
              newRightMember.add(new Terminal(Terminal.EMPTY_VALUE));
            }
            newProductionRules.add(
                new ProductionRule(Arrays.asList((Symbol) iNonTerminal), newRightMember));
          }
          // Remove ai -> Aj b:
          productionRuleIterator.remove();
        }
        // Add all new Ai -> a1 b | a2 b | ... | ak b productions:
        mProductionRules.addAll(newProductionRules);
      }
      // Remove immediate left recursion for Ai:
      removeImmediateLeftRecursion(iNonTerminal, String.valueOf(i));
    }
  }
}
