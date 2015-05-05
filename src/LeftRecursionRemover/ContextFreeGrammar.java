package LeftRecursionRemover;

import LeftRecursionRemover.Exception.ContextException;
import LeftRecursionRemover.Exception.GrammarException;
import LeftRecursionRemover.GrammarSyntax.NonTerminal;
import LeftRecursionRemover.GrammarSyntax.ProductionRule;
import LeftRecursionRemover.GrammarSyntax.Terminal;

import java.util.ArrayList;

/**
 * Created by razvan on 5/5/15.
 */
public class ContextFreeGrammar extends Grammar {
  public ContextFreeGrammar(ArrayList<NonTerminal> nonTerminalSymbols,
      ArrayList<Terminal> terminalSymbols,
      ArrayList<ProductionRule> productionRules,
      NonTerminal startingSymbol) throws GrammarException {
    super(nonTerminalSymbols, terminalSymbols, productionRules, startingSymbol);
    for (ProductionRule productionRule : productionRules) {
      if (!Utils.isContextFreeProductionRule(productionRule)) {
        throw new ContextException("Grammar is not context-free!");
      }
    }
  }
}
