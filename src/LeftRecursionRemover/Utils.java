package LeftRecursionRemover;

import LeftRecursionRemover.GrammarSyntax.ProductionRule;
import LeftRecursionRemover.GrammarSyntax.Symbol;

/**
 * Created by razvan on 5/5/15.
 */
public class Utils {
  public static boolean isContextFreeProductionRule(ProductionRule productionRule) {
    return productionRule.leftMember().size() == 1
      && productionRule.leftMember().get(0).type() == Symbol.TYPE_NON_TERMINAL;
  }
}
