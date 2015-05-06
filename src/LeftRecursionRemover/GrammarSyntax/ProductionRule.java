package LeftRecursionRemover.GrammarSyntax;

import LeftRecursionRemover.Exception.ProductionRuleException;

import java.util.List;

/**
 * Created by razvan on 5/2/15.
 */
public class ProductionRule {
  private List<Symbol> mLeftMember;
  private List<Symbol> mRightMember;

  public ProductionRule(List<Symbol> leftMember, List<Symbol> rightMember)
      throws ProductionRuleException {
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
    mRightMember = rightMember;
  }

  public ProductionRule(ProductionRule other) throws ProductionRuleException {
    this(other.leftMember(), other.rightMember());
  }

  public List<Symbol> leftMember() {
    return mLeftMember;
  }

  public List<Symbol> rightMember() {
    return mRightMember;
  }

  public boolean isContextFree() {
    return mLeftMember.size() == 1 && mLeftMember.get(0).type() == Symbol.TYPE_NON_TERMINAL;
  }
}
