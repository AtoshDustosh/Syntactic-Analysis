package analyzer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class PredictiveParsingTable {

  private Map<GrammarSymbol, Integer> rowIndexMap = new HashMap<>(); // nonterminal to integer
  private Map<GrammarSymbol, Integer> columnIndexMap = new HashMap<>(); // terminal to integer
  private Vector<Vector<Production>> analysisTable = new Vector<>();
  private Vector<Vector<Boolean>> synchTable = new Vector<>();

  public PredictiveParsingTable(Set<GrammarSymbol> rowSet,
      Set<GrammarSymbol> columnSet) {
    this.initializeTable(rowSet, columnSet);
  }

  public PredictiveParsingTable(Productions productions) {
    this.initializeTable(productions.getNonterminalSet(),
        productions.getTerminalSet());
  }

  public int getRowCount() {
    return this.rowIndexMap.keySet().size();
  }

  public int getColumnCount() {
    return this.columnIndexMap.keySet().size();
  }

  public boolean setProductionTableEntry(GrammarSymbol nonterminal,
      GrammarSymbol terminal,
      Production newEntry) {
    if (nonterminal.getType().equals(GrammarSymbolType.NONTERMINAL) &&
        terminal.getType().equals(GrammarSymbolType.TERMINAL)) {
      int rowIndex = this.rowIndexMap.get(nonterminal);
      int columnIndex = this.columnIndexMap.get(terminal);
      Production oldEntry = this.analysisTable.get(rowIndex).get(columnIndex);
      if (oldEntry.equals(newEntry)) {
        return false;
      } else {
        this.analysisTable.get(rowIndex).set(columnIndex, newEntry);
        return true;
      }
    } else {
      return false;
    }
  }

  public Production getProductionTableEntry(GrammarSymbol nonterminal,
      GrammarSymbol terminal) {
    if (nonterminal.getType().equals(GrammarSymbolType.NONTERMINAL) &&
        terminal.getType().equals(GrammarSymbolType.TERMINAL)) {
      int rowIndex = this.rowIndexMap.get(nonterminal);
      int columnIndex = this.columnIndexMap.get(terminal);
      return this.analysisTable.get(rowIndex).get(columnIndex);
    } else {
      return new Production();
    }
  }

  public boolean setSynchTableEntry(GrammarSymbol nonterminal,
      GrammarSymbol terminal, boolean newEntry) {
    if (nonterminal.getType().equals(GrammarSymbolType.NONTERMINAL) &&
        terminal.getType().equals(GrammarSymbolType.TERMINAL)) {
      int rowIndex = this.rowIndexMap.get(nonterminal);
      int columnIndex = this.columnIndexMap.get(terminal);
      boolean oldEntry = this.synchTable.get(rowIndex).get(columnIndex);
      if (oldEntry == newEntry) {
        return false;
      } else {
        this.synchTable.get(rowIndex).set(columnIndex, newEntry);
        return true;
      }
    } else {
      return false;
    }
  }

  public boolean getSynchTableEntry(GrammarSymbol nonterminal,
      GrammarSymbol terminal) {
    if (nonterminal.getType().equals(GrammarSymbolType.NONTERMINAL) &&
        terminal.getType().equals(GrammarSymbolType.TERMINAL)) {
      int rowIndex = this.rowIndexMap.get(nonterminal);
      int columnIndex = this.columnIndexMap.get(terminal);
      return this.synchTable.get(rowIndex).get(columnIndex);
    } else {
      return false;
    }
  }

  public Set<GrammarSymbol> getRowSet() {
    return new HashSet<>(this.rowIndexMap.keySet());
  }

  public Set<GrammarSymbol> getColumnSet() {
    return new HashSet<>(this.columnIndexMap.keySet());
  }

  public PredictiveParsingTable copy() {
    PredictiveParsingTable ppTable = new PredictiveParsingTable(
        this.rowIndexMap.keySet(), this.columnIndexMap.keySet());
    for (GrammarSymbol rowSymbol : this.rowIndexMap.keySet()) {
      for (GrammarSymbol columnSymbol : this.columnIndexMap.keySet()) {
        ppTable.setProductionTableEntry(rowSymbol, columnSymbol,
            new Production());
        ppTable.setSynchTableEntry(rowSymbol, columnSymbol, false);
      }
    }
    return ppTable;
  }

  private void initializeTable(Set<GrammarSymbol> rowSet,
      Set<GrammarSymbol> columnSet) {
    int rowIndex = 0;
    int columnIndex = 0;
    // initialize the maps
    for (GrammarSymbol symbol : rowSet) {
      this.rowIndexMap.put(symbol, rowIndex++);
    }
    for (GrammarSymbol symbol : columnSet) {
      this.columnIndexMap.put(symbol, columnIndex++);
    }
    if (rowSet.size() == 0 || columnSet.size() == 0) {
      rowIndex = 1;
      columnIndex = 1;
    }
    // initialize the tables
    for (int i = 0; i < rowIndex; i++) {
      Vector<Production> analysisTableRow = new Vector<>();
      Vector<Boolean> synchTableRow = new Vector<>();
      for (int j = 0; j < columnIndex; j++) {
        analysisTableRow.add(new Production());
        synchTableRow.add(false);
      }
      this.analysisTable.add(analysisTableRow);
      this.synchTable.add(synchTableRow);
    }
  }
}
