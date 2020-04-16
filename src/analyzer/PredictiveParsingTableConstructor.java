package analyzer;

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
     * TODO debug method "private void buildFirstSet()"
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
    Map<String, Boolean> finished = new HashMap<>();
    for (int i = 0; i < this.productions.size(); i++) {
      GrammarSymbol LHS = this.productions.getProduction(i).getLHS();
      finished.put(LHS.toString(), false);
    }
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
    List<List<GrammarSymbol>> rhsList = production.getRHSlist();
    /*
     * TODO
     */
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

}
