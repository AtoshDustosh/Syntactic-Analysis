package analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tokens {

  public static final String wordSerialNumberFilePath = "src/analyzer/CodeList";

  private List<Token> tokenList = new ArrayList<>();
  BufferedReader br;
  private HashMap<String, Integer> type_SerialNum;

  public Tokens() {
    super();
  }

  public Tokens(String wordSerialNumberFilePath) {
    this.loadWordSerialNumberFile(wordSerialNumberFilePath);
  }

  public void add(String wordType, String wordValue) {
    this.add(this.wordTypeToSerialNumber(wordType), wordValue);
  }

  public void add(int wordSerialNumber, String wordValue) {
    this.tokenList.add(new Token(wordSerialNumber, wordValue));
  }

  /**
   * Each line of the format file is read in a "type coded" style and
   * loaded into the Map
   * 
   * @param filePath
   */
  private void loadWordSerialNumberFile(String filePath) {
    try {
      this.br = new BufferedReader(new FileReader(new File(filePath)));
      this.type_SerialNum = new HashMap<>();
      String line;
      while ((line = this.br.readLine()) != null) {
        String[] array_type_num = line.split(" ");
        String type = array_type_num[0];
        int num = Integer.parseInt(array_type_num[1]);
        this.type_SerialNum.put(type, num);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * Get a copy of Tokens
   * 
   * @return copy of Tokens
   */
  public Tokens getTokensCopy() {
    Tokens tks_new = new Tokens();
    tks_new.tokenList = new ArrayList<>(this.tokenList);
    tks_new.br = new BufferedReader(this.br);
    tks_new.type_SerialNum = new HashMap<>(this.type_SerialNum);
    return tks_new;
  }

  /**
   * The type code is generated according to the loaded Map and the
   * input string
   * 
   * @param wordType
   * @return type code
   */
  private int wordTypeToSerialNumber(String wordType) {
    if (this.type_SerialNum.containsKey(wordType)) {
      return this.type_SerialNum.get(wordType);
    }
    return 0;
  }

  /**
   * Returns a character type based on the type code
   * 
   * @param wordSerialNumber
   * @return worTtype
   */
  private String wordSerialNumberToType(int wordSerialNumber) {
    for (String s : this.type_SerialNum.keySet()) {
      if (this.type_SerialNum.get(s) == wordSerialNumber) {
        return s;
      }
    }
    return "None";
  }

  public static void main(String[] args) {
    /* test */
    Tokens ts = new Tokens(wordSerialNumberFilePath);
    System.out.println(ts.wordSerialNumberToType(19));
    System.out.println(ts.wordTypeToSerialNumber("+"));
    Tokens ts1 = ts.getTokensCopy();
    System.out.println(ts.hashCode());
    System.out.println(ts1.hashCode());
  }
}
