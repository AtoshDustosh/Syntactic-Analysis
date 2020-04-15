package analyzer;

import java.util.ArrayList;
import java.util.List;

public class FollowSets {

  private List<GrammarSymbol> nonterminalList = new ArrayList<>();
  private List<List<GrammarSymbol>> terminalsList = new ArrayList<>();

  public FollowSets() {

  }

  public static void main(String[] args) {
    FirstSets test = new FirstSets();
    List<GrammarSymbol> list = new ArrayList<>();
    GrammarSymbol s1 = new GrammarSymbol("A");
    GrammarSymbol s2 = new GrammarSymbol("B");
    GrammarSymbol s3 = new GrammarSymbol("A");

    list.clear();
    list.add(new GrammarSymbol("a"));
    list.add(new GrammarSymbol("b"));
    list.add(new GrammarSymbol("b"));
    test.addFirstSet(s1, list);

    System.out.println(test.toString());

    list.clear();
    list.add(new GrammarSymbol("b"));
    list.add(new GrammarSymbol("c"));
    test.addFirstSet(s2, list);

    System.out.println(test.toString());

    list.clear();
    list.add(new GrammarSymbol("b"));
    list.add(new GrammarSymbol("b"));
    list.add(new GrammarSymbol("c"));
    list.add(new GrammarSymbol("c"));
    test.addFirstSet(s3, list);

    System.out.println(test.toString());
  }

  public void addItem(GrammarSymbol nonterminal,
      List<GrammarSymbol> terminalList) {
    terminalList = this.removeRepeatedSymbol(terminalList);
    List<GrammarSymbol> oldTerminalsList = new ArrayList<>();
    int oldNonterminalIndex = -1;
    for (int i = 0; i < this.nonterminalList.size(); i++) {
      if (this.nonterminalList.get(i).equals(nonterminal)) {
        oldNonterminalIndex = i;
        oldTerminalsList = this.terminalsList.get(oldNonterminalIndex);
        break;
      }
    }
    if (oldNonterminalIndex == -1) {
      oldNonterminalIndex = this.nonterminalList.size();
      this.nonterminalList.add(nonterminal);
      this.terminalsList.add(new ArrayList<>());
    }
    for (int i = 0; i < terminalList.size(); i++) {
      boolean alreadyHas = false;
      for (int j = 0; j < oldTerminalsList.size(); j++) {
        GrammarSymbol oldSymbol = oldTerminalsList.get(j);
        GrammarSymbol newSymbol = terminalList.get(i);
        alreadyHas = alreadyHas || oldSymbol.equals(newSymbol);

//        System.out.println(newSymbol + " _ " + oldSymbol + " ~ "
//            + oldSymbol.equals(newSymbol) + " -> " + alreadyHas);
      }
      if (alreadyHas == false) {
        this.terminalsList.get(oldNonterminalIndex).add(terminalList.get(i));
      }
    }
  }

  public List<GrammarSymbol> getTerminalList(GrammarSymbol symbol) {
    if (symbol.getType().equals(GrammarSymbolType.NONTERMINAL) == false) {
      return new ArrayList<>();
    }
    for (int i = 0; i < this.nonterminalList.size(); i++) {
      GrammarSymbol nonterminal = this.nonterminalList.get(i);
      if (nonterminal.equals(symbol)) {
        return this.terminalsList.get(i);
      }
    }
    return new ArrayList<>();
  }

  public boolean ifHasTerminal(GrammarSymbol nonterminal,
      GrammarSymbol terminal) {
    int nonterminalIndex = -1;
    for (int i = 0; i < this.nonterminalList.size(); i++) {
      GrammarSymbol temp = this.nonterminalList.get(i);
      if (temp.equals(nonterminal)) {
        nonterminalIndex = i;
        break;
      }
    }
    if (nonterminalIndex == -1) {
      return false;
    } else {
      List<GrammarSymbol> terminals = this.terminalsList.get(nonterminalIndex);
      for (int i = 0; i < terminals.size(); i++) {
        GrammarSymbol temp = terminals.get(i);
        if (temp.equals(terminal)) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean ifHasNonterminal(GrammarSymbol nonterminal) {
    for (int i = 0; i < this.nonterminalList.size(); i++) {
      GrammarSymbol temp = this.nonterminalList.get(i);
      if (temp.equals(nonterminal)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String toString() {
    String str = "";
    for (int i = 0; i < this.nonterminalList.size(); i++) {
      str = str + this.nonterminalList.get(i).toString() + " - "
          + this.terminalsList.get(i).toString() + "\n";
    }
    return str;
  }

  private List<GrammarSymbol> removeRepeatedSymbol(List<GrammarSymbol> list) {
    List<GrammarSymbol> newList = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      boolean repeated = false;
      GrammarSymbol newSymbol = list.get(i);
      for (int j = 0; j < newList.size(); j++) {
        if (newList.get(j).equals(newSymbol)) {
          repeated = true;
        }
      }
      if (repeated == false) {
        newList.add(newSymbol);
      }
    }
    return newList;
  }

}
