package analyzer;

/**
 * Types of grammar symbols in a production.
 * 
 * @author AtoshDustosh
 */
public enum GrammarSymbolType {
  TERMINAL("terminal"),
  NONTERMINAL("nonterminal"),
  UNDEFINED("undefined"),
  OR("or"),
  ;

  private String type = "";

  GrammarSymbolType(String type) {
    this.type = type;
  }

  public String getType() {
    return this.type;
  }

  public boolean equals(GrammarSymbolType symbolType) {
    if (this.type.equals(symbolType.getType())) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public String toString() {
    return this.type;
  }
}
