package LeftRecursionRemover.GrammarSyntax;

/**
 * Created by razvan on 5/2/15.
 */
public class Terminal extends Symbol {
  public static final Terminal EMPTY = new Terminal("\u03B5");

  public Terminal(String value) {
    mValue = value;
  }

  public String value() {
    return mValue;
  }
}
