package LeftRecursionRemover.GrammarSyntax;

import LeftRecursionRemover.Exception.ProductionRuleException;

import java.util.ArrayList;

/**
 * Created by razvan on 5/2/15.
 */
public class ProductionRule {
  private ArrayList<Symbol> mLeftMember;
  private ArrayList<Symbol> mRightMember;

  public ArrayList<Symbol> leftMember() {
    return mLeftMember;
  }

  public ArrayList<Symbol> rightMember() {
    return mRightMember;
  }

  public void setLeftMember(ArrayList<Symbol> leftMember) throws ProductionRuleException {
    boolean foundNonTerminal = false;
    for (Symbol symbol : leftMember) {
      if (symbol.type() == Symbol.TYPE_NON_TERMINAL) {
        foundNonTerminal = true;
        break;
      }
    }
    if (!foundNonTerminal) {
      throw new ProductionRuleException("Left member must contain at least one non-terminal!");
    }
    mLeftMember = leftMember;
  }

  public void setRightMember(ArrayList<Symbol> rightMember) {
    mRightMember = rightMember;
  }
}
