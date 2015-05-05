package LeftRecursionRemover.GrammarSyntax;

/**
 * Created by razvan on 5/2/15.
 */
public abstract class Symbol {
  public static final int TYPE_NON_TERMINAL = 0;
  public static final int TYPE_TERMINAL = 1;

  protected String mValue;

  public Symbol(String value) {
    mValue = value;
  }

  public String value() {
    return mValue;
  }

  public abstract int type();
}
