package analyzer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Grammar symbol in a production.
 * 
 * @author AtoshDustosh
 */
public class GrammarSymbol {

  private String symbolStr = "";

  private GrammarSymbolType symbolType = GrammarSymbolType.UNDEFINED;

  public GrammarSymbol() {

  }

  public GrammarSymbol(String symbolStr) {
    this.symbolStr = symbolStr;
    if (this.isNonterminalSymbol(symbolStr)) {
      this.symbolType = GrammarSymbolType.NONTERMINAL;
    } else if (this.isTerminalSymbol(symbolStr)) {
      this.symbolType = GrammarSymbolType.TERMINAL;
    } else if (this.isOrSymbol(symbolStr)) {
      this.symbolType = GrammarSymbolType.OR;
    } else {
      this.symbolType = GrammarSymbolType.UNDEFINED;
    }
  }

  public static void main(String[] args) {
    String symbolStr = "|";
    GrammarSymbol gS = new GrammarSymbol(symbolStr);
    System.out.println(gS.toString());
  }

  public String getString() {
    return this.symbolStr;
  }

  public GrammarSymbolType getType() {
    return this.symbolType;
  }

  public boolean isNonterminalSymbol() {
    return this.symbolType.equals(GrammarSymbolType.NONTERMINAL);
  }

  public boolean isTerminalSymbol() {
    return this.symbolType.equals(GrammarSymbolType.TERMINAL);
  }

  public boolean isOrSymbol() {
    return this.symbolType.equals(GrammarSymbolType.OR);
  }

  @Override
  public String toString() {
    String str = "";
    str = "[" + this.symbolStr + "," + this.symbolType.toString() + "]";
    return str;
  }

  private boolean isNonterminalSymbol(String str) {
    Pattern p = Pattern.compile("([A-Z]|_|[0-9])+");
    Matcher m = p.matcher(str);
    return m.matches();
  }

  private boolean isTerminalSymbol(String str) {
    Pattern p = Pattern.compile("([A-Z]|_|[0-9])+|\\|");
    Matcher m = p.matcher(str);
    return !m.matches();
  }

  private boolean isOrSymbol(String str) {
    if (str.equals("|")) {
      return true;
    } else {
      return false;
    }
  }

}
