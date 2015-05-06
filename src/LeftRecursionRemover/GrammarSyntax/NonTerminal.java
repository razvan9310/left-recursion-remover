package LeftRecursionRemover.GrammarSyntax;

import LeftRecursionRemover.Exception.SymbolException;

/**
 * Created by razvan on 5/2/15.
 */
public class NonTerminal extends Symbol{
  public static String DEFAULT_NEW_NONTERMINAL_VALUE = "NEW_";

  public NonTerminal(String value) throws SymbolException {
    super(value);
  }

  public int type() {
    return Symbol.TYPE_NON_TERMINAL;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof NonTerminal && mValue.equals(((NonTerminal) obj).value());
  }
}
