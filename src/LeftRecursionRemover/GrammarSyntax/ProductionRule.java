package LeftRecursionRemover.GrammarSyntax;

import LeftRecursionRemover.Exception.ProductionRuleException;

import java.util.Iterator;
import java.util.List;

/**
 * Created by razvan on 5/2/15.
 */
public class ProductionRule {
  public static final String PRODUCTION_RULE_SEPARATOR = "->";

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
      throw new ProductionRuleException("Left member must contain at least one non-terminal" +
              " (exactly one for context-free grammars)!");
    }

    if (leftMember.size() > 1) {
      int symbolsRemoved = 0;
      int initialProductionSymbolsCount = leftMember.size();
      Iterator<Symbol> symbolIterator = leftMember.iterator();
      while (symbolIterator.hasNext()) {
        Symbol symbol = symbolIterator.next();
        if (Terminal.EMPTY_VALUE.equals(symbol.value())
                && symbolsRemoved < initialProductionSymbolsCount - 1) {
          symbolIterator.remove();
          ++symbolsRemoved;
        }
      }
    }

    if (rightMember.size() > 1) {
      int symbolsRemoved = 0;
      int initialProductionSymbolsCount = rightMember.size();
      Iterator<Symbol> symbolIterator = rightMember.iterator();
      while (symbolIterator.hasNext()) {
        Symbol symbol = symbolIterator.next();
        if (Terminal.EMPTY_VALUE.equals(symbol.value())
                && symbolsRemoved < initialProductionSymbolsCount - 1) {
          symbolIterator.remove();
          ++symbolsRemoved;
        }
      }
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

  public boolean isEmpty() {
    return mRightMember.size() == 1 && mRightMember.get(0).type() == Symbol.TYPE_TERMINAL
        && Terminal.EMPTY_VALUE.equals(mRightMember.get(0).value());
  }

  public boolean isLeftRecursive() {
    if (mRightMember.size() < mLeftMember.size()) {
      return false;
    }
    for (int i = 0; i < mLeftMember.size(); ++i) {
      if (!mLeftMember.get(i).equals(mRightMember.get(i))) {
        return false;
      }
    }
    return true;
  }
}
