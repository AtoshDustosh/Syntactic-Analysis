package analyzer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import data.input.InputFilePaths;

public class PredictiveParsingTableConstructor {

  private Productions productions = null;

  private boolean firstSetBuilt = false;
  private FirstSets firstSets = new FirstSets();
  private boolean followSetBuilt = false;
  private FollowSets followSets = new FollowSets();
  private boolean selectSetBuilt = false;
  private SelectSets selectSets = new SelectSets();

  private int debug = 0;

  public PredictiveParsingTableConstructor() {

  }

  public PredictiveParsingTableConstructor(Productions productions) {
    this.loadProductions(productions);
  }

  public static void main(String[] args) {
    /*
     * TODO compliment
     */
    Productions productions = new Productions();
    PredictiveParsingTableConstructor pptConstructor = new PredictiveParsingTableConstructor();

    productions.loadProductionsFile(InputFilePaths.PRODUCTIONS.getFilePath());
    System.out.println("Productions: \n" + productions.toString());

    pptConstructor.loadProductions(productions);
    System.out
        .println("First Sets: \n" + pptConstructor.getFirstSets().toString());
  }

  public void loadProductions(Productions productions) {
    this.productions = productions;
    this.buildFirstSets();
    this.buildFollowSets();
    this.buildSelectSets();
    /*
     * TODO add construction of FollowSets and SelectSets
     */
  }

  public FirstSets getFirstSets() {
    return this.firstSets.copy();
  }

  private void buildFirstSets() {
    // finished - whether the FIRST set of a nonterminal is calculated
    Map<String, Boolean> finished = new HashMap<>();
    // initialize the finished map
    for (int i = 0; i < this.productions.size(); i++) {
      GrammarSymbol LHS = this.productions.getProduction(i).getLHS();
      finished.put(LHS.toString(), false);
    }
    // select a production to process with recursive procedure
    for (int i = 0; i < this.productions.size(); i++) {
      Production production = this.productions.getProduction(i);
      if (finished.get(production.getLHS().toString()) == true) {
        continue;
      } else {
        this.recursiveBuildFirstSet(production, finished);
      }
    }
    this.firstSetBuilt = true;
  }

  /**
   * Recursively build the first set of productions.
   * 
   * @param production production whose first set is to be built
   * @param finished   a map from the string of left hand side symbol of
   *                   the production to the boolean whether the first
   *                   set of this LHS symbol is built.
   */
  private void recursiveBuildFirstSet(Production production,
      Map<String, Boolean> finished) {
//    System.out.println("select production - " + production.toString());
    GrammarSymbol LHS = production.getLHS();
    List<List<GrammarSymbol>> rhsList = production.getRHSlist();
    // process all RHS of a production one by one
    for (int i = 0; i < rhsList.size(); i++) {
      List<GrammarSymbol> RHS = rhsList.get(i);
      int emptyCount = 0;
      // process all GrammarSymbol of an RHS one by one
      for (int j = 0; j < RHS.size(); j++) {
        GrammarSymbol symbol = RHS.get(j);
//        System.out.println("process symbol: " + symbol);
        if (symbol.equals(LHS)) {
          break;
        } else if (symbol.getType().equals(GrammarSymbolType.TERMINAL)) {
          Set<GrammarSymbol> tempSet = new HashSet<>();
          tempSet.add(symbol);
          this.firstSets.addFirstSet(LHS, tempSet);
          if (symbol.equals(new GrammarSymbol("empty"))) {
            emptyCount++;
          }
          break;
        } else if (symbol.getType().equals(GrammarSymbolType.NONTERMINAL)) {
          boolean ifFirstSetFinished = finished.get(symbol.toString());
          if (ifFirstSetFinished) {
            Set<GrammarSymbol> newFirstSet = this.firstSets
                .getTerminalSet(symbol);
            if (emptyCount == j) {
              this.firstSets.addFirstSet(LHS, newFirstSet);
            }
            if (newFirstSet.contains(new GrammarSymbol("empty"))) {
              emptyCount++;
            } else {
              break;
            }
          } else {
            Production newPro = this.productions.getProduction(symbol);
            this.recursiveBuildFirstSet(newPro, finished);
            j = j - 1;
          }
        } else {
          System.out.println("error - invalid production");
          System.exit(-1);
        }
      } // end of loop (j)
      if (emptyCount == RHS.size()) {
        Set<GrammarSymbol> tempSet = new HashSet<>();
        tempSet.add(new GrammarSymbol("empty"));
        this.firstSets.addFirstSet(LHS, tempSet);
      } else {
        this.firstSets.removeFirstSetItem(LHS, new GrammarSymbol("empty"));
      }
    } // end of loop (i)
    finished.put(LHS.toString(), true);
//    System.out.println("set Map finished of " + LHS.toString() + " to true");
  }

  private void buildFollowSets() {
    if (this.firstSetBuilt == false) {
      this.buildFirstSets();
    }
    /*
     * TODO
     */
    this.followSetBuilt = true;
  }

  private void buildSelectSets() {
    if (this.followSetBuilt == false) {
      this.buildFollowSets();
    }
    /*
     * TODO
     */
    this.selectSetBuilt = true;
  }

  /**
   * Calculate the First set of a list of grammar symbols.
   * 
   * @param list grammar symbols organized into a list
   * @return the First set of the grammar list
   */
  private Set<GrammarSymbol> firstSetofSymbolList(List<GrammarSymbol> list) {
    Set<GrammarSymbol> firstSet = new HashSet<>();
    int emptyCount = 0;
    // process all grammar symbols in the input list
    for (int i = 0; i < list.size(); i++) {
      GrammarSymbol symbol = list.get(i);
      if (symbol.getType().equals(GrammarSymbolType.TERMINAL)) {
        firstSet.add(new GrammarSymbol(symbol));
        break;
      } else if (symbol.getType().equals(GrammarSymbolType.NONTERMINAL)) {
        Set<GrammarSymbol> symbolFirstSet = this.firstSets
            .getTerminalSet(symbol);
        firstSet.addAll(symbolFirstSet);
        if (symbolFirstSet.contains(new GrammarSymbol("empty")) == false) {
          break;
        } else {
          emptyCount++;
        }
      }
    }
    if (emptyCount < list.size()) {
      firstSet.remove(new GrammarSymbol("empty"));
    }
    return firstSet;
  }

}
