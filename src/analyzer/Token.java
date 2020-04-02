package analyzer;

public class Token {
  private int wordSerialNumber = 0;
  private String wordValue = "";

  public Token(int wordSerialNumber, String wordValue) {
    this.wordSerialNumber = wordSerialNumber;
    this.wordValue = wordValue;
  }

  public int getWordSerialNumber() {
    return this.wordSerialNumber;
  }

  public String getWordValue() {
    return this.wordValue;
  }

}
