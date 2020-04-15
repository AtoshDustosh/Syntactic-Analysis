package analyzer;

import java.util.ArrayList;
import java.util.List;

/**
 * A production is composed of left hand side and right hand side. The
 * left hand side must be a nonterminal, while the right hand side can
 * be any valid grammar symbols.
 * 
 * @author AtoshDustosh
 */
public class Production {
  public static final int PRODUCTION_INVALID = 0;

  public static final String DERIVATION_SYMBOL = "->";

  private GrammarSymbol leftHandSide = new GrammarSymbol();
  private List<GrammarSymbol> rightHandSide = new ArrayList<>();

  public Production(List<GrammarSymbol> symbolList) {
    this.parseGrammarSymbolList(symbolList);
  }

  public Production(GrammarSymbol leftHandSide,
      List<GrammarSymbol> rightHandSide) {
    this.leftHandSide = leftHandSide;
    this.rightHandSide = rightHandSide;
  }

  public Production(Production production) {
    this.leftHandSide = production.getLHS();
    this.rightHandSide = production.getRHS();
  }

  public static void main(String[] args) {
    List<GrammarSymbol> symbolList = new ArrayList<>();
    symbolList.add(new GrammarSymbol("A"));
    symbolList.add(new GrammarSymbol("->"));
    symbolList.add(new GrammarSymbol("A_1"));
    symbolList.add(new GrammarSymbol("|"));
    symbolList.add(new GrammarSymbol("digit"));
    symbolList.add(new GrammarSymbol(";"));

    Production production = new Production(symbolList);
    System.out.println("production: \n" + production.getProductionString());
    System.out.println("production details: \n" + production.toString());

  }

  public GrammarSymbol getLHS() {
    return this.leftHandSide;
  }

  public List<GrammarSymbol> getRHS() {
    return new ArrayList<>(this.rightHandSide);
  }

  public String getProductionString() {
    String str = "";
    str = str + this.leftHandSide.getString() + " -> ";
    for (int i = 0; i < this.rightHandSide.size(); i++) {
      str = str + this.rightHandSide.get(i).getString() + " ";
    }
    return str;
  }

  @Override
  public String toString() {
    String str = "";
    str = str + this.leftHandSide.toString();
    for (int i = 0; i < this.rightHandSide.size(); i++) {
      str = str + "\n" + this.rightHandSide.get(i).toString();
    }
    return str;
  }

  public boolean equals(Production production) {
    GrammarSymbol LHS = production.getLHS();
    List<GrammarSymbol> RHS = production.getRHS();
    boolean result = LHS.equals(this.leftHandSide);
    result = result && (RHS.size() == this.rightHandSide.size());
    for (int i = 0; i < RHS.size(); i++) {
      result = result && (RHS.get(i).equals(this.rightHandSide.get(i)));
    }
    return result;
  }

  private void parseGrammarSymbolList(List<GrammarSymbol> symbolList) {
    if (this.checkGrammarSymbolList(symbolList) == false) {
      System.exit(PRODUCTION_INVALID);
    }
    this.leftHandSide = symbolList.get(0);
    for (int i = 2; i < symbolList.size(); i++) {
      this.rightHandSide.add(symbolList.get(i));
    }
  }

  private boolean checkGrammarSymbolList(List<GrammarSymbol> symbolList) {
    if (symbolList.size() < 3) {
      System.out.println("error: production invalid - too short");
      return false;
    }
    if (symbolList.get(0).isTerminalSymbol()
        || symbolList.get(1).getString().equals(DERIVATION_SYMBOL) == false) {
      System.out
          .println("error: production invalid - first grammar symbol invalid");
      System.out.println(symbolList.get(0).toString());
      return false;
    }
    for (int i = 2; i < symbolList.size(); i++) {
      if (symbolList.get(i).getType().equals(GrammarSymbolType.UNDEFINED)) {
        System.out
            .println("error: production invalid - right hand symbol invalid");
        return false;
      }
    }
    return true;
  }

}
