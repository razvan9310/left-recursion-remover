package LeftRecursionRemover;

import LeftRecursionRemover.Exception.GrammarException;
import LeftRecursionRemover.Exception.NonTerminalException;
import LeftRecursionRemover.Exception.ProductionRuleException;
import LeftRecursionRemover.Exception.TerminalException;
import LeftRecursionRemover.GrammarSyntax.NonTerminal;
import LeftRecursionRemover.GrammarSyntax.ProductionRule;
import LeftRecursionRemover.GrammarSyntax.Terminal;

import java.util.ArrayList;

/**
 * Created by razvan on 5/2/15.
 */
public class Grammar {
  protected ArrayList<NonTerminal> mNonTerminalSymbols;
  protected ArrayList<Terminal> mTerminalSymbols;
  protected ArrayList<ProductionRule> mProductionRules;
  protected NonTerminal mStartingSymbol;

  public Grammar(ArrayList<NonTerminal> nonTerminalSymbols, ArrayList<Terminal> terminalSymbols,
      ArrayList<ProductionRule> productionRules, NonTerminal startingSymbol)
      throws GrammarException {
    if (nonTerminalSymbols == null || nonTerminalSymbols.isEmpty()) {
      throw new NonTerminalException("Null or empty non-terminals list!");
    }
    if (terminalSymbols == null || terminalSymbols.isEmpty()) {
      throw new TerminalException("Null or empty terminals list!");
    }
    if (productionRules == null || productionRules.isEmpty()) {
      throw new ProductionRuleException("Null or empty production rules list!");
    }
    if (!nonTerminalSymbols.contains(startingSymbol)) {
      throw new NonTerminalException("Invalid starting symbol!");
    }

    mNonTerminalSymbols = nonTerminalSymbols;
    mTerminalSymbols = terminalSymbols;
    mProductionRules = productionRules;
    mStartingSymbol = startingSymbol;
  }
}
