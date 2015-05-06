package LeftRecursionRemover;

import LeftRecursionRemover.Exception.GrammarException;
import LeftRecursionRemover.Exception.NonTerminalException;
import LeftRecursionRemover.Exception.ProductionRuleException;
import LeftRecursionRemover.Exception.TerminalException;
import LeftRecursionRemover.GrammarSyntax.NonTerminal;
import LeftRecursionRemover.GrammarSyntax.ProductionRule;
import LeftRecursionRemover.GrammarSyntax.Symbol;
import LeftRecursionRemover.GrammarSyntax.Terminal;

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
          throw new ProductionRuleException("Invalid non-terminal in production rule!");
        }
        if (symbol.type() == Symbol.TYPE_TERMINAL && !terminals.contains(symbol)) {
          throw new ProductionRuleException("Invalid terminal in production rule!");
        }
      }
      for (Symbol symbol : productionRule.rightMember()) {
        if (symbol.type() == Symbol.TYPE_NON_TERMINAL && !nonTerminals.contains(symbol)) {
          throw new ProductionRuleException("Invalid non-terminal in production rule!");
        }
        if (symbol.type() == Symbol.TYPE_TERMINAL && !terminals.contains(symbol)) {
          throw new ProductionRuleException("Invalid terminal in production rule!");
        }
      }
    }

    if (!nonTerminals.contains(startingSymbol)) {
      throw new NonTerminalException("Invalid starting symbol!");
    }

    mNonTerminals = nonTerminals;
    mTerminals = terminals;
    mProductionRules = productionRules;
    mStartingSymbol = startingSymbol;
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
