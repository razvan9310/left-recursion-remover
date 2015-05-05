package LeftRecursionRemover.GrammarSyntax;

/**
 * Created by razvan on 5/2/15.
 */
public class NonTerminal extends Symbol{
  public NonTerminal(String value) {
    super(value);
  }

  public int type() {
    return Symbol.TYPE_NON_TERMINAL;
  }
}
