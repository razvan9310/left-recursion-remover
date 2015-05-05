package LeftRecursionRemover.GrammarSyntax;

/**
 * Created by razvan on 5/2/15.
 */
public class NonTerminal extends Symbol{
  public NonTerminal(String value) {
    mValue = value;
  }

  public String value() {
    return mValue;
  }
}
