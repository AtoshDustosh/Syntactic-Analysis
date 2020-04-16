package analyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.input.InputFilePaths;

public class PredictiveParsingTableConstructor {

  private Productions productions = null;

  private boolean firstSetBuilt = false;
  private FirstSets firstSets = new FirstSets();
  private boolean followSetBuilt = false;
  private FollowSets followSets = new FollowSets();
  private boolean selectSetBuilt = false;
  private SelectSets selectSets = new SelectSets();

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

  private void recursiveBuildFirstSet(Production production,
      Map<String, Boolean> finished) {
//    System.out.println("select production - " + production.toString());
    GrammarSymbol LHS = production.getLHS();
    List<List<GrammarSymbol>> rhsList = production.getRHSlist();
    // process all RHS of a production
    for (int i = 0; i < rhsList.size(); i++) {
      List<GrammarSymbol> RHS = rhsList.get(i);
      int emptyCount = 0;
      // process all GrammarSymbol of an RHS
      for (int j = 0; j < RHS.size(); j++) {
        GrammarSymbol symbol = RHS.get(j);
//        System.out.println("process symbol: " + symbol);
        if (symbol.getType().equals(GrammarSymbolType.TERMINAL)) {
          this.firstSets.addFirstSet(LHS,
              new ArrayList<>(Arrays.asList(symbol)));
          if (symbol.equals(new GrammarSymbol("empty"))) {
            emptyCount++;
          }
          break;
        } else if (symbol.getType().equals(GrammarSymbolType.NONTERMINAL)) {
          boolean ifFirstSetFinished = finished.get(symbol.toString());
          if (ifFirstSetFinished) {
            List<GrammarSymbol> newFirstSet = this.firstSets
                .getTerminalList(symbol);
            if (emptyCount == j) {
              this.firstSets.addFirstSet(LHS, newFirstSet);
            }
            if (this.listContains(newFirstSet, new GrammarSymbol("empty"))) {
              emptyCount++;
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
        this.firstSets.addFirstSet(LHS,
            new ArrayList<>(Arrays.asList(new GrammarSymbol("empty"))));
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

    this.followSetBuilt = true;
  }

  private void buildSelectSets() {
    if (this.followSetBuilt == false) {
      this.buildFollowSets();
    }

    this.selectSetBuilt = true;
  }

  /**
   * Check whether a list of GrammarSymbol contains a specific
   * GrammarSymbol.
   * 
   * @param list   list of GrammarSymbol
   * @param symbol GrammarSymbol to be looked up for existance
   * @return true if exists; false otherwise
   */
  private boolean listContains(List<GrammarSymbol> list, GrammarSymbol symbol) {
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).equals(symbol)) {
        return true;
      }
    }
    return false;
  }

}
