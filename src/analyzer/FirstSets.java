package analyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Store the First sets of a series of nonterminals. The First sets
 * are calculated according to some productions.
 * 
 * @author AtoshDustosh
 */
public class FirstSets {

  private List<GrammarSymbol> nonterminalList = new ArrayList<>();
  private List<List<GrammarSymbol>> terminalsList = new ArrayList<>();

  public FirstSets() {

  }

  public static void main(String[] args) {
    FirstSets test = new FirstSets();
    List<GrammarSymbol> list = new ArrayList<>();
    GrammarSymbol s1 = new GrammarSymbol("A");
    GrammarSymbol s2 = new GrammarSymbol("A");
    GrammarSymbol s3 = new GrammarSymbol("A");

    list.clear();
    list.add(new GrammarSymbol("a"));
    list.add(new GrammarSymbol("b"));
    list.add(new GrammarSymbol("b"));
    System.out.println("modified: " + test.addFirstSet(s1, list));

    System.out.println(test.toString());

    list.clear();
    list.add(new GrammarSymbol("b"));
    list.add(new GrammarSymbol("c"));
    System.out.println("modified: " + test.addFirstSet(s2, list));

    System.out.println(test.toString());

    list.clear();
    list.add(new GrammarSymbol("b"));
    list.add(new GrammarSymbol("b"));
    list.add(new GrammarSymbol("c"));
    list.add(new GrammarSymbol("c"));
    System.out.println("modified: " + test.addFirstSet(s3, list));

    System.out.println(test.toString());

    System.out.println(
        "modified: " + test.removeFirstSetItem(s1, new GrammarSymbol("c")));
    System.out.println(test.toString());
    System.out.println(
        "modified: " + test.removeFirstSetItem(s1, new GrammarSymbol("b")));
    System.out.println(test.toString());
    System.out.println(
        "modified: " + test.removeFirstSetItem(s1, new GrammarSymbol("a")));
    System.out.println(test.toString());
    System.out.println(
        "modified: " + test.removeFirstSetItem(s1, new GrammarSymbol("a")));
    System.out.println(test.toString());
  }

  /**
   * Add a new First set (nonterminal, list of terminals) into the
   * database.
   * 
   * @param nonterminal  nonterminal grammar symbol
   * @param terminalList terminals in the First set of the nonterminal
   * @return true if modified; false otherwise
   */
  public boolean addFirstSet(GrammarSymbol nonterminal,
      List<GrammarSymbol> terminalList) {
    boolean modified = false;
    List<GrammarSymbol> oldTerminalsList = new ArrayList<>();
    int oldNonterminalIndex = -1;
    terminalList = this.removeRepeatedSymbol(terminalList);
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
        modified = true;
      }
    }
    return modified;
  }

  public boolean addFirstSet(GrammarSymbol nonterminal,
      GrammarSymbol terminal) {
    return this.addFirstSet(nonterminal,
        new ArrayList<>(Arrays.asList(terminal)));
  }

  /**
   * Remove a terminal from the First set to the nonterminal in this
   * database.
   * 
   * @param nonterminal nonterminal grammar symbol
   * @param terminal    terminal to be removed from First set to the
   *                    nonterminal
   * @return true if modified; false otherwise
   */
  public boolean removeFirstSetItem(GrammarSymbol nonterminal,
      GrammarSymbol terminal) {
    boolean modified = false;
    int nonterminalIndex = -1;
    for (int i = 0; i < this.nonterminalList.size(); i++) {
      GrammarSymbol tempNonterminal = this.nonterminalList.get(i);
      if (tempNonterminal.equals(nonterminal)) {
        nonterminalIndex = i;
        break;
      }
    }
    if (nonterminalIndex == -1) {
      return modified;
    } else {
      List<GrammarSymbol> terminalList = this.terminalsList
          .get(nonterminalIndex);
      for (int i = 0; i < terminalList.size(); i++) {
        GrammarSymbol tempTerminal = terminalList.get(i);
        if (tempTerminal.equals(terminal)) {
          this.terminalsList.get(nonterminalIndex).remove(i);
          modified = true;
          break;
        }
      }
    }
    return modified;
  }

  public List<GrammarSymbol> getTerminalList(GrammarSymbol symbol) {
    if (symbol.getType().equals(GrammarSymbolType.NONTERMINAL) == false) {
      return new ArrayList<>();
    }
    for (int i = 0; i < this.nonterminalList.size(); i++) {
      GrammarSymbol nonterminal = this.nonterminalList.get(i);
      if (nonterminal.equals(symbol)) {
        return new ArrayList<>(this.terminalsList.get(i));
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

  public FirstSets copy() {
    FirstSets copy = new FirstSets();
    for (int i = 0; i < this.nonterminalList.size(); i++) {
      copy.addFirstSet(this.nonterminalList.get(i), this.terminalsList.get(i));
    }
    return copy;
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
