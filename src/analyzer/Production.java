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
  private List<List<GrammarSymbol>> rightHandSide = new ArrayList<>();

  public Production() {

  }

  public Production(List<GrammarSymbol> symbolList) {
    this.parseGrammarSymbolList(symbolList);
  }

  public Production(GrammarSymbol leftHandSide,
      List<List<GrammarSymbol>> rightHandSide) {
    this.leftHandSide = leftHandSide;
    this.rightHandSide = rightHandSide;
  }

  public Production(Production production) {
    this.leftHandSide = production.getLHS();
    this.rightHandSide = production.getRHSlist();
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
    System.out.println("production: \n" + production.toString());

  }

  public GrammarSymbol getLHS() {
    return this.leftHandSide;
  }

  public List<List<GrammarSymbol>> getRHSlist() {
    List<List<GrammarSymbol>> rhs = new ArrayList<>();
    for (int i = 0; i < this.rightHandSide.size(); i++) {
      rhs.add(new ArrayList<>(this.rightHandSide.get(i)));
    }
    return rhs;
  }

  @Override
  public String toString() {
    String str = "";
    str = str + this.leftHandSide.getString() + " ->";
    for (int i = 0; i < this.rightHandSide.size(); i++) {
      String rhsPiece = "";
      List<GrammarSymbol> productionPiece = this.rightHandSide.get(i);
      for (int j = 0; j < productionPiece.size(); j++) {
        rhsPiece = rhsPiece + productionPiece.get(j).toString() + " ";
      }
      rhsPiece = "\t" + rhsPiece;
      if (i > 0) {
        rhsPiece = " or" + rhsPiece;
      }
      str = str + rhsPiece + "\n";
    }
    return str;
  }

  public boolean equals(Production production) {
    GrammarSymbol LHS = production.getLHS();
    List<List<GrammarSymbol>> RHS = production.getRHSlist();
    boolean result = LHS.equals(this.leftHandSide);
    result = result && (RHS.size() == this.rightHandSide.size());
    if (result == false) {
      return false;
    }
    for (int i = 0; i < RHS.size(); i++) {
      for (int j = 0; j < RHS.get(i).size(); j++) {
        GrammarSymbol thisSymbol = this.rightHandSide.get(i).get(j);
        GrammarSymbol thatSymbol = RHS.get(i).get(j);
        result = result && (thisSymbol.equals(thatSymbol));
      }
    }
    return result;
  }

  private void parseGrammarSymbolList(List<GrammarSymbol> symbolList) {
    if (this.checkGrammarSymbolList(symbolList) == false) {
      System.exit(PRODUCTION_INVALID);
    }
    this.leftHandSide = symbolList.get(0);
    List<GrammarSymbol> rhsPiece = new ArrayList<>();
    for (int i = 2; i < symbolList.size(); i++) {
      GrammarSymbol symbol = symbolList.get(i);
      if (symbol.getType().equals(GrammarSymbolType.OR) == false) {
        rhsPiece.add(symbol);
      } else {
        this.rightHandSide.add(rhsPiece);
        rhsPiece = new ArrayList<>();
      }
    }
    if (rhsPiece.size() > 0) {
      this.rightHandSide.add(rhsPiece);
    }
  }

  private boolean checkGrammarSymbolList(List<GrammarSymbol> symbolList) {
    if (symbolList.size() < 3) {
      System.out.println(
          "error: production invalid - too short - " + symbolList.toString());
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
