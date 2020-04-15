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

  public int size() {
    return this.productionList.size();
  }

  public void loadProductionsFile(String filePath) {
    this.productionsFilePath = filePath;
    this.loadProductionsFile();
    this.breakProductionsIntoPieces();
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
   * Break productions into single productions, which means breaking
   * those productions with "or" into several productions that start
   * with the same left hand side nonterminal.
   */
  private void breakProductionsIntoPieces() {
    List<Production> newProductionList = new ArrayList<>();
    for (Production p : this.productionList) {
      GrammarSymbol LHS = p.getLHS();
      List<GrammarSymbol> RHS = p.getRHS();
      List<List<GrammarSymbol>> newRHSList = new ArrayList<>();
      List<GrammarSymbol> newRHS = new ArrayList<>();
      for (GrammarSymbol symbol : RHS) {
        if (symbol.getType().equals(GrammarSymbolType.OR)) {
          newRHSList.add(newRHS);
          newRHS = new ArrayList<>();
        } else {
          newRHS.add(symbol);
        }
      }
      newRHSList.add(newRHS); // add the last piece of RHS
      for (int i = 0; i < newRHSList.size(); i++) {
        Production newProduction = new Production(LHS, newRHSList.get(i));
        newProductionList.add(newProduction);
      }
    }
    this.productionList = newProductionList;
  }
}
