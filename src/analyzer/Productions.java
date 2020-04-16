package analyzer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import data.input.InputFilePaths;

/**
 * Productions of the whole grammar.
 * 
 * @author AtoshDustosh
 */
public class Productions {
  private String productionsFilePath = InputFilePaths.PRODUCTIONS.getFilePath();

  private List<Production> productionList = new ArrayList<>();

  public Productions() {

  }

  public Productions(List<Production> productionList) {
    this.productionList = productionList;
  }

  public Productions(String filePath) {
    this.productionsFilePath = filePath;
  }

  public static void main(String[] args) {
    Productions productions = new Productions();
    productions.loadProductionsFile(InputFilePaths.PRODUCTIONS.getFilePath());
    System.out.println("productions\n" + productions.toString());
  }

  public Production getProduction(int index) {
    return new Production(this.productionList.get(index));
  }

  public Production getProduction(GrammarSymbol nonterminal) {
    for (int i = 0; i < this.productionList.size(); i++) {
      GrammarSymbol LHS = this.productionList.get(i).getLHS();
      if (LHS.equals(nonterminal)) {
        return new Production(this.productionList.get(i));
      }
    }
    return new Production();
  }

  public int size() {
    return this.productionList.size();
  }

  public void loadProductionsFile(String filePath) {
    this.productionsFilePath = filePath;
    this.loadProductionsFile();
//    this.breakProductionsIntoPieces();
  }

  /**
   * Break all those production that has "or" in them into pieces and
   * put the pieces and those productions not broken into another
   * instance of Productions.
   * 
   * @return an instance of piece-broken Production for this instance
   */
  public Productions breakIntoPieces() {
    List<Production> brokenProductions = new ArrayList<>();
    for (int i = 0; i < this.productionList.size(); i++) {
      Production production = this.productionList.get(i);
      List<Production> pieces = production.breakIntoPieces();
      for (int k = 0; k < pieces.size(); k++) {
        brokenProductions.add(pieces.get(k));
      }
    }
    return new Productions(brokenProductions);
  }

  @Override
  public String toString() {
    String str = "";
    for (int i = 0; i < this.productionList.size(); i++) {
      str = str + this.productionList.get(i).toString() + "\n\n";
    }
    return str;
  }

  private void loadProductionsFile() {
    System.out
        .println("Loading productions' file: " + this.productionsFilePath);
    try {
      Scanner scanner = new Scanner(
          new FileInputStream(this.productionsFilePath));
      boolean hasNext = scanner.hasNext();
      while (hasNext) {
        String line = scanner.nextLine();
        List<GrammarSymbol> symbolList = this
            .buildGrammarSymbolListFromLine(line);
        if (symbolList.size() > 0) {
          Production production = new Production(symbolList);
          this.productionList.add(production);
        }
        hasNext = scanner.hasNext();
      }
      scanner.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  private List<GrammarSymbol> buildGrammarSymbolListFromLine(String line) {
    List<GrammarSymbol> symbolList = new ArrayList<>();
    line = line.replace("\n", "");
    String[] strArray = line.split("\\t|\\s");
    for (int i = 0; i < strArray.length; i++) {
      if (strArray[i].equals("//")) {
        break;
      } else if (strArray[i].length() == 0) {
        continue;
      }
      symbolList.add(new GrammarSymbol(strArray[i]));
    }
    return symbolList;
  }

  /**
   * Integerate several productions into one production. Note that these
   * productions must have the same LHS.
   * 
   * @param list list of productions
   * @return integerated production if successful; empty production
   *         otherwise
   */
  private Production integerateProductions(List<Production> list) {
    // why did I write this function???
    Production integerated = new Production();
    boolean canDoIntegeration = true;

    GrammarSymbol LHS = list.get(0).getLHS();
    for (int i = 0; i < list.size(); i++) {
      canDoIntegeration = canDoIntegeration
          && LHS.equals(list.get(i).getLHS());
    }
    if (canDoIntegeration == false) {
      return integerated;
    }
    integerated.setLHS(LHS);
    for (int i = 0; i < list.size(); i++) {
      List<List<GrammarSymbol>> rhsList = list.get(i).getRHSlist();
      for (int j = 0; j < rhsList.size(); j++) {
        integerated.addNewRHS(rhsList.get(j));
      }
    }
    return integerated;
  }
}
