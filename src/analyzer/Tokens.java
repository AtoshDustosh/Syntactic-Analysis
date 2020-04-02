package analyzer;

import java.util.ArrayList;
import java.util.List;

public class Tokens {

  private List<Token> tokenList = new ArrayList<>();

  public Tokens(String wordSerialNumberFilePath) {
    this.loadWordSerialNumberFile(wordSerialNumberFilePath);
  }

  public void add(String wordType, String wordValue) {
    this.add(this.wordTypeToSerialNumber(wordType), wordValue);
  }

  public void add(int wordSerialNumber, String wordValue) {
    this.tokenList.add(new Token(wordSerialNumber, wordValue));
  }

  private void loadWordSerialNumberFile(String filePath) {
    /**
     * \TODO
     */
  }

  private int wordTypeToSerialNumber(String wordType) {
    /**
     * \TODO
     */
    return 0;
  }

  private String wordSerialNumberToType(int wordSerialNumber) {
    /**
     * \TODO
     */
    return "";
  }
}
