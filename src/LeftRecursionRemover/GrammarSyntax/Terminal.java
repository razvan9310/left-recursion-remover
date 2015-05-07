package LeftRecursionRemover.GrammarSyntax;

import LeftRecursionRemover.Exception.SymbolException;

/**
 * Created by razvan on 5/2/15.
 */
public class Terminal extends Symbol {
  public static final String EMPTY_VALUE = "$";

  public Terminal(String value) throws SymbolException {
    super(value);
  }

  public int type() {
    return Symbol.TYPE_TERMINAL;
  }

  public boolean isEmptyTerminal() {
    return EMPTY_VALUE.equals(mValue);
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof Terminal && mValue.equals(((Terminal) obj).value());
  }
}
