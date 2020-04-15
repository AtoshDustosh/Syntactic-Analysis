package analyzer;

import java.util.ArrayList;
import java.util.List;

public class SelectSets {

  private List<Production> productionList = new ArrayList<>();
  private List<List<GrammarSymbol>> terminalsList = new ArrayList<>();

  public SelectSets() {

  }

  public void addItem(Production production, List<GrammarSymbol> terminalList) {
    this.productionList.add(production);
    this.terminalsList.add(terminalList);
  }

  public List<GrammarSymbol> getTerminalList(Production production) {
    for (int i = 0; i < this.productionList.size(); i++) {
      Production nonterminal = this.productionList.get(i);
      if (nonterminal.equals(production)) {
        return this.terminalsList.get(i);
      }
    }
    return new ArrayList<>();
  }

}
