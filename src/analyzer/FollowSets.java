package analyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FollowSets {

  private Map<GrammarSymbol, Set<GrammarSymbol>> followSetMap = new HashMap<>();

  public FollowSets() {

  }

  public static void main(String[] args) {
    FollowSets test = new FollowSets();
    List<GrammarSymbol> list = new ArrayList<>();
    GrammarSymbol s1 = new GrammarSymbol("A");
    GrammarSymbol s2 = new GrammarSymbol("A");
    GrammarSymbol s3 = new GrammarSymbol("A");

    list.clear();
    list.add(new GrammarSymbol("a"));
    list.add(new GrammarSymbol("b"));
    list.add(new GrammarSymbol("b"));
    System.out.println("modified: " + test.addFollowSet(s1, list));

    System.out.println(test.toString());

    list.clear();
    list.add(new GrammarSymbol("b"));
    list.add(new GrammarSymbol("c"));
    System.out.println("modified: " + test.addFollowSet(s2, list));

    System.out.println(test.toString());

    list.clear();
    list.add(new GrammarSymbol("b"));
    list.add(new GrammarSymbol("b"));
    list.add(new GrammarSymbol("c"));
    list.add(new GrammarSymbol("c"));
    System.out.println("modified: " + test.addFollowSet(s3, list));

    System.out.println(test.toString());

    System.out.println(
        "modified: " + test.removeFollowSetItem(s1, new GrammarSymbol("c")));
    System.out.println(test.toString());
    System.out.println(
        "modified: " + test.removeFollowSetItem(s1, new GrammarSymbol("b")));
    System.out.println(test.toString());
    System.out.println(
        "modified: " + test.removeFollowSetItem(s1, new GrammarSymbol("a")));
    System.out.println(test.toString());
    System.out.println(
        "modified: " + test.removeFollowSetItem(s1, new GrammarSymbol("a")));
    System.out.println(test.toString());
  }

  /**
   * Add a new Follow set (nonterminal, list of terminals) into the
   * database.
   * 
   * @param nonterminal  nonterminal grammar symbol
   * @param terminalList terminals in the Follow set of the nonterminal
   * @return true if modified; false otherwise
   */
  public boolean addFollowSet(GrammarSymbol nonterminal,
      List<GrammarSymbol> terminalList) {
    boolean modified = false;
    Set<GrammarSymbol> terminalSet = new HashSet<>();
    for (int i = 0; i < terminalList.size(); i++) {
      terminalSet.add(terminalList.get(i));
    }
    if (this.followSetMap.containsKey(nonterminal)) {
      Set<GrammarSymbol> oldSet = this.followSetMap.get(nonterminal);
      if (oldSet.containsAll(terminalSet) == false) {
        oldSet.addAll(terminalSet);
        modified = true;
      }
    } else {
      this.followSetMap.put(nonterminal, terminalSet);
      modified = true;
    }
    return modified;
  }

  public boolean addFollowSet(GrammarSymbol nonterminal,
      GrammarSymbol terminal) {
    return this.addFollowSet(nonterminal,
        new ArrayList<>(Arrays.asList(terminal)));
  }

  /**
   * Remove a terminal from the Follow set to the nonterminal in this
   * database.
   * 
   * @param nonterminal nonterminal grammar symbol
   * @param terminal    terminal to be removed from Follow set to the
   *                    nonterminal
   * @return true if modified; false otherwise
   */
  public boolean removeFollowSetItem(GrammarSymbol nonterminal,
      GrammarSymbol terminal) {
    boolean modified = false;
    Set<GrammarSymbol> oldSet = this.followSetMap.get(nonterminal);
    modified = oldSet.remove(terminal);
    return modified;
  }

  public List<GrammarSymbol> getTerminalList(GrammarSymbol symbol) {
    Set<GrammarSymbol> oldSet = this.followSetMap.get(symbol);
    List<GrammarSymbol> symbolList = new ArrayList<>();
    if (oldSet == null) {
      return symbolList;
    }
    for (GrammarSymbol gS : oldSet) {
      symbolList.add(new GrammarSymbol(gS));
    }
    return symbolList;
  }

  public boolean ifHasTerminal(GrammarSymbol nonterminal,
      GrammarSymbol terminal) {
    Set<GrammarSymbol> oldSet = this.followSetMap.get(nonterminal);
    if (oldSet == null) {
      return false;
    }
    if (oldSet.contains(terminal)) {
      return true;
    } else {
      return false;
    }
  }

  public boolean ifHasNonterminal(GrammarSymbol nonterminal) {
    return this.followSetMap.containsKey(nonterminal);
  }

  public FollowSets copy() {
    FollowSets copy = new FollowSets();
    for (GrammarSymbol gS : this.followSetMap.keySet()) {
      List<GrammarSymbol> symbolList = new ArrayList<>();
      Set<GrammarSymbol> symbolSet = this.followSetMap.get(gS);
      symbolList.addAll(symbolSet);
      copy.addFollowSet(gS, new ArrayList<>(symbolSet));
    }
    return copy;
  }

  @Override
  public String toString() {
    String str = "";
    for (GrammarSymbol gS : this.followSetMap.keySet()) {
      Set<GrammarSymbol> symbolSet = this.followSetMap.get(gS);
      str = str + gS.toString() + " -> " + symbolSet.toString() + "\n";
    }
    return str;
  }
}
