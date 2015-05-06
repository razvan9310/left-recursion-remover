package LeftRecursionRemover.GrammarSyntax;

/**
 * Created by razvan on 5/2/15.
 */
public class Terminal extends Symbol {
  public static final String EMPTY_VALUE = "$";

  public Terminal(String value) {
    super(value);
  }

  public int type() {
    return Symbol.TYPE_TERMINAL;
  }
}
