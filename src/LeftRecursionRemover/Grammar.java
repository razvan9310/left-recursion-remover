package LeftRecursionRemover;

import LeftRecursionRemover.Exception.GrammarException;
import LeftRecursionRemover.Exception.NonTerminalException;
import LeftRecursionRemover.Exception.ProductionRuleException;
import LeftRecursionRemover.Exception.TerminalException;
import LeftRecursionRemover.GrammarSyntax.NonTerminal;
import LeftRecursionRemover.GrammarSyntax.ProductionRule;
import LeftRecursionRemover.GrammarSyntax.Symbol;
import LeftRecursionRemover.GrammarSyntax.Terminal;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by razvan on 5/2/15.
 */
public class Grammar {
  protected List<NonTerminal> mNonTerminals;
  protected List<Terminal> mTerminals;
  protected List<ProductionRule> mProductionRules;
  protected NonTerminal mStartingSymbol;

  public Grammar(List<NonTerminal> nonTerminals, List<Terminal> terminals,
      List<ProductionRule> productionRules, NonTerminal startingSymbol)
      throws GrammarException {
    if (nonTerminals == null || nonTerminals.isEmpty()) {
      throw new NonTerminalException("Null or empty non-terminals list!");
    }
    if (terminals == null || terminals.isEmpty()) {
      throw new TerminalException("Null or empty terminals list!");
    }
    if (productionRules == null || productionRules.isEmpty()) {
      throw new ProductionRuleException("Null or empty production rules list!");
    }

    for (ProductionRule productionRule : productionRules) {
      for (Symbol symbol : productionRule.leftMember()) {
        if (symbol.type() == Symbol.TYPE_NON_TERMINAL && !nonTerminals.contains(symbol)) {
          throw new ProductionRuleException(
              "Invalid non-terminal in production rule: " + symbol.value());
        }
        if (symbol.type() == Symbol.TYPE_TERMINAL && !terminals.contains(symbol)) {
          throw new ProductionRuleException(
              "Invalid terminal in production rule: " + symbol.value());
        }
      }
      for (Symbol symbol : productionRule.rightMember()) {
        if (symbol.type() == Symbol.TYPE_NON_TERMINAL && !nonTerminals.contains(symbol)) {
          throw new ProductionRuleException(
              "Invalid non-terminal in production rule: " + symbol.value());
        }
        if (symbol.type() == Symbol.TYPE_TERMINAL && !terminals.contains(symbol)
            && !Terminal.EMPTY_VALUE.equals(symbol.value())) {
          throw new ProductionRuleException(
              "Invalid terminal in production rule: " + symbol.value());
        }
      }

      if (productionRule.rightMember().size() > 1) {
        int symbolsRemoved = 0;
        int initialProductionSymbolsCount = productionRule.rightMember().size();
        Iterator<Symbol> symbolIterator = productionRule.rightMember().iterator();
        while (symbolIterator.hasNext()) {
          Symbol symbol = symbolIterator.next();
          if (Terminal.EMPTY_VALUE.equals(symbol.value())
              && symbolsRemoved < initialProductionSymbolsCount - 1) {
            symbolIterator.remove();
            ++symbolsRemoved;
          }
        }
      }
    }

    if (!nonTerminals.contains(startingSymbol)) {
      throw new NonTerminalException("Starting symbol is not a valid non-terminal!");
    }

    mNonTerminals = nonTerminals;
    mTerminals = terminals;
    mProductionRules = productionRules;
    mStartingSymbol = startingSymbol;
  }


  public List<ProductionRule> productionRules() {
    return mProductionRules;
  }

  public boolean hasEmptyProductions() {
    for (ProductionRule productionRule : mProductionRules) {
      if (productionRule.isEmpty()) {
        return true;
      }
    }
    return false;
  }
}
