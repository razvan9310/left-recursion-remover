package LeftRecursionRemover;

import LeftRecursionRemover.GrammarSyntax.NonTerminal;
import LeftRecursionRemover.GrammarSyntax.ProductionRule;
import LeftRecursionRemover.GrammarSyntax.Terminal;

import java.util.ArrayList;

/**
 * Created by razvan on 5/2/15.
 */
public class Grammar {
  private ArrayList<NonTerminal> mNonTerminalSymbols;
  private ArrayList<Terminal> mTerminalSymbols;
  private ArrayList<ProductionRule> mProductionRules;
  private NonTerminal mStartingSymbol;

  public Grammar(ArrayList<NonTerminal> nonTerminalSymbols, ArrayList<Terminal> terminalSymbols,
      ArrayList<ProductionRule> productionRules, NonTerminal startingSymbol)
      throws GrammarException {

  }
}
