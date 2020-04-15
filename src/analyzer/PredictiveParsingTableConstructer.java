package analyzer;

import java.util.ArrayList;
import java.util.List;

public class PredictiveParsingTableConstructer {

  private Productions productions = null;

  private boolean firstSetBuilt = false;
  private FirstSets firstSets = new FirstSets();
  private boolean followSetBuilt = false;
  private FollowSets followSets = new FollowSets();
  private boolean selectSetBuilt = false;
  private SelectSets selectSets = new SelectSets();

  public PredictiveParsingTableConstructer(Productions productions) {
    this.productions = productions;
  }

  public static void main(String[] args) {
    /*
     * TODO debug method "private void buildFirstSet()"
     */
  }

  private void buildFirstSet() {
    boolean modified = false;
    int productionIndex = 0;
    do {
      Production production = this.productions.getProduction(productionIndex);
      int countEmpty = 0;
      GrammarSymbol tempLHSsymbol = production.getLHS();
      List<GrammarSymbol> terminalList = new ArrayList<>();
      GrammarSymbol tempRHSsymbol = production.getRHS().get(0);
      if (tempRHSsymbol.getType().equals(GrammarSymbolType.NONTERMINAL)) {
        terminalList = this.firstSets.getTerminalList(tempRHSsymbol);
        modified = modified
            || this.firstSets.addFirstSet(tempLHSsymbol, terminalList);
      } else if (tempRHSsymbol.getType().equals(GrammarSymbolType.TERMINAL)) {
        terminalList.add(tempRHSsymbol);
        modified = modified
            || this.firstSets.addFirstSet(tempLHSsymbol, terminalList);
      }
      for (int i = 0; i < production.getRHS().size(); i++) {
        tempRHSsymbol = production.getRHS().get(i);
        terminalList = this.firstSets.getTerminalList(tempRHSsymbol);
        boolean containsEmpty = false;
        for (int j = 0; j < terminalList.size(); j++) { // see if contains empty
          GrammarSymbol tempTerminal = terminalList.get(j);
          if (tempTerminal.getString().equals("empty")) {
            containsEmpty = true;
            break;
          }
        }
        if (containsEmpty && i < production.getRHS().size() - 1) {
          GrammarSymbol tempRHSsymbolNext = production.getRHS().get(i + 1);
          List<GrammarSymbol> terminalListNext = this.firstSets
              .getTerminalList(tempRHSsymbolNext);
          modified = modified
              || this.firstSets.addFirstSet(tempLHSsymbol, terminalListNext);
          countEmpty++;
        } else if (containsEmpty && i == production.getRHS().size() - 1) {
          countEmpty++;
        } else {
          break;
        }
      }
      if (countEmpty == production.getRHS().size()) {
        terminalList = new ArrayList<>();
        terminalList.add(new GrammarSymbol("empty"));
        modified = modified
            || this.firstSets.addFirstSet(tempLHSsymbol, terminalList);
      } else {
        this.firstSets.removeFirstSetItem(tempLHSsymbol,
            new GrammarSymbol("empty"));
      }
    } while (modified);
    this.firstSetBuilt = true;
  }

  private void buildFollowSet() {
    if (this.firstSetBuilt == false) {
      this.buildFirstSet();
    }

    this.followSetBuilt = true;
  }

  private void buildSelectSet() {
    if (this.followSetBuilt == false) {
      this.buildFollowSet();
    }

    this.selectSetBuilt = true;
  }

}
