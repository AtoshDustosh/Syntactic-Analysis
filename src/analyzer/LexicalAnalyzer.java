package analyzer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import data.args.KeyWord;
import data.args.WordTypes;
import data.input.InputFile;

/**
 * This class can analyze a program file of specific format and get
 * all tokens of it.
 * 
 * @author AtoshDustosh
 */
public class LexicalAnalyzer {
  public static final String testFilePath = "src/test/test2.code";

  public static final String ANALYSIS_ERROR = "analysis error";
  public static final String ANALYSIS_UNFINISHED = "analysis unfinished";
  public static final String ANALYSIS_FINISHED = "analysis finished";

  private final Map<String, DfaAnalyzer> analyzerMap = new HashMap<>();

  private int chPointer = 0;
  private List<Character> chList = new ArrayList<>();

  private DfaAnalyzer analyzerUsed = null;
  private String analyzedWordType = "";

  private Tokens tokens = new Tokens(Tokens.wordSerialNumberFilePath);

  public LexicalAnalyzer() {
    this.analyzerMap.put(WordTypes.CHAR.getType(), new DfaAnalyzer(
        WordTypes.CHAR.getType(), InputFile.charDFA.getFilePath()));
    this.analyzerMap.put(WordTypes.NUM.getType(), new DfaAnalyzer(
        WordTypes.NUM.getType(), InputFile.numDFA.getFilePath()));
    this.analyzerMap.put(WordTypes.STRING.getType(), new DfaAnalyzer(
        WordTypes.STRING.getType(), InputFile.stringDFA.getFilePath()));
    this.analyzerMap.put(WordTypes.IDN.getType(), new DfaAnalyzer(
        WordTypes.IDN.getType(), InputFile.idnDFA.getFilePath()));
    this.analyzerMap.put(WordTypes.OP_DL_COM.getType(), new DfaAnalyzer(
        WordTypes.OP_DL_COM.getType(), InputFile.op_dl_comDFA.getFilePath()));

    System.out.println(
        this.analyzerMap.get(WordTypes.OP_DL_COM.getType()).getState());
  }

  public static void main(String[] args) {
    LexicalAnalyzer analyzer = new LexicalAnalyzer();
    Tokens tokens = analyzer.analyzeFile(LexicalAnalyzer.testFilePath);
    System.out.println("tokens:\n" + tokens.toString());
  }

  public Tokens analyzeFile(String filePath) {
    this.convertFileIntoCharList(filePath);
    System.out.println(this.chList.toString());
    this.chPointer = 0;

    System.out.println("file reading completed");
    String analysisResult = "";
    while (this.chList.isEmpty() == false) {
      char ch = this.chList.get(this.chPointer);
      System.out.println("(" + ch + ")");

      if (this.chPointer == 0 && (ch == ' ' || ch == '\t')) {
        this.chList.remove(0);
        this.chPointer = 0;
        continue;
      }

      analysisResult = this.analyzeChar(ch);
      if (analysisResult.equals(ANALYSIS_UNFINISHED)) {
        this.chPointer++;
      } else if (analysisResult.equals(ANALYSIS_ERROR)) {
        this.chList.remove(0);
        this.chPointer = 0;
      } else {
        Token token = this.buildToken(this.analyzedWordType, this.chPointer);
        for (int i = 0; i < this.chPointer; i++) {
          this.chList.remove(0);
        }
        this.tokens.add(token);
        System.out.println("token: " + token.toString());
        this.chPointer = 0;
        this.resetAnalyzer(this.analyzerUsed.getDfaAnalyzerWordType());
      }
    }
    return this.tokens.getTokensCopy();
  }

  public Tokens getTokens() {
    return this.tokens.getTokensCopy();
  }

  private String analyzeChar(char ch) {
    int analyzerState = 0;
    if (this.chPointer == 0) {
      this.selectAnalyzer(ch);
    }
    analyzerState = this.analyzerUsed.enterNextState(ch);
    return this.convertStateToResult(analyzerState);
  }

  private void selectAnalyzer(char ch) {
    if (ch == '\'') {
      this.analyzerUsed = this.analyzerMap.get(WordTypes.CHAR.getType());
    } else if (ch == '\"') {
      this.analyzerUsed = this.analyzerMap.get(WordTypes.STRING.getType());
    } else if (ch >= '0' && ch <= '9') {
      this.analyzerUsed = this.analyzerMap.get(WordTypes.NUM.getType());
    } else if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')
        || ch == '_') {
      this.analyzerUsed = this.analyzerMap.get(WordTypes.IDN.getType());
    } else {
      this.analyzerUsed = this.analyzerMap.get(WordTypes.OP_DL_COM.getType());
    }
    System.out.println("analyzer status: " + this.analyzerUsed.getState());
  }

  private String convertStateToResult(int analyzerState) {
    this.analyzedWordType = this.analyzerUsed.getAnalyzedWordType();
    switch (analyzerState) {
      case DfaAnalyzer.state_error:
        return LexicalAnalyzer.ANALYSIS_ERROR;
      case DfaAnalyzer.state_finished:
        return LexicalAnalyzer.ANALYSIS_FINISHED;
      default:
        return LexicalAnalyzer.ANALYSIS_UNFINISHED;
    }
  }

  private Token buildToken(String wordType, int borderIndex) {
    int wordSerialNumber = 0;
    String wordValue = "";
    for (int i = 0; i < borderIndex; i++) {
      wordValue = wordValue + this.chList.get(i);
    }
    if (wordType.equals(WordTypes.IDN.getType())
        && KeyWord.isKeyWord(wordValue)) {
      // recognize whether this word is a key word
      wordSerialNumber = this.tokens.wordTypeToSerialNumber(wordValue);
    } else {
      wordSerialNumber = this.tokens.wordTypeToSerialNumber(wordType);
    }
    return new Token(wordSerialNumber, wordValue);
  }

  private void convertFileIntoCharList(String filePath) {
    try {
      Scanner scanner = new Scanner(new FileInputStream(filePath));
      // read input file and split into characters
      while (scanner.hasNext()) {
        char[] lineChar = (scanner.nextLine() + "\n").toCharArray();
        for (int i = 0; i < lineChar.length; i++) {
          this.chList.add(lineChar[i]);
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  private void resetAnalyzer(String analyzerType) {
    this.analyzerMap.get(analyzerType).reset();
  }

  private boolean isNonnegativeInteger(String str) {
    Pattern pattern = Pattern.compile("^[\\+]?[\\d]+$");
    return pattern.matcher(str).matches();
  }

}
