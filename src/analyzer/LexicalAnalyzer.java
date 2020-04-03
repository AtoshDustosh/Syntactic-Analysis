package analyzer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import data.args.WordTypes;
import data.input.InputFile;

/**
 * 根据程序流程，我要在某个主程序里面实现所有的这些步骤。
 * 主要是读取符号和进入对应的DFA，之后根据DFA自动机的结果判断是否将当前识别的单词录入token，
 * 或者判断为识别错误。因此就需要有一个Lexical Analyzer类，负责这个框架的执行框架内部，
 * 需要有一个读取文件符号的Scanner，一段调用各个类别的DFA自动机的代码，
 * 因此也就需要在内部建立各个类型的DFA。另外，还要有能够根据DFA返回结果录入token的功能。
 * 不过，如果要实现图形化界面的话，就需要把所有的流程在另一个图形化程序的main函数中重复一遍， 这样一来，就不能在Lexical
 * Analyzer类的main函数中实现完整流程， 而是要在它的一个方法中实现。 This class can analyze a
 * program file of specific format and get all tokens of it.
 * 
 * @author AtoshDustosh
 */
public class LexicalAnalyzer {

  private final DfaAnalyzer charAnalyzer = new DfaAnalyzer(
      WordTypes.CHAR.getType(),
      InputFile.charDFA.getFilePath());
  private final DfaAnalyzer numAnalyzer = new DfaAnalyzer(
      WordTypes.NUM.getType(),
      InputFile.numDFA.getFilePath());
  private final DfaAnalyzer stringAnalyzer = new DfaAnalyzer(
      WordTypes.STRING.getType(),
      InputFile.stringDFA.getFilePath());
  private final DfaAnalyzer idnAnalyzer = new DfaAnalyzer(
      WordTypes.IDN.getType(),
      InputFile.idnDFA.getFilePath());
  private final DfaAnalyzer op_dl_comAnalyzer = new DfaAnalyzer(
      WordTypes.OP_DL_COM.getType(),
      InputFile.op_dl_comDFA.getFilePath());

  List<Character> chList = new ArrayList<>();

  Tokens tokens = new Tokens(Tokens.wordSerialNumberFilePath);

  public LexicalAnalyzer() {

  }

  public Tokens analyzeFile(String filePath) {
    int presentChIndexInList = 0;
    try {
      Scanner scanner = new Scanner(new FileInputStream(filePath));
      boolean hasNext = scanner.hasNext();

      // read input file and split into characters
      while (hasNext) {
        String tableEntry = "";
        char[] lineChars = (scanner.nextLine() + "\n").toCharArray();
        for (int i = 0; i < lineChars.length; i++) {
          // analyze characters

          if (lineChars[i] == ' ' || lineChars[i] == '\t') {
            // ignore ' ' and '\t'
            continue;
          } else {
            // load a character from input file
            this.chList.add(lineChars[i]);
            tableEntry = this
                .analyzeChar(this.chList.get(presentChIndexInList));
          }

          // process of analysis
          if (this.isNonnegativeInteger(tableEntry)) {
            // token not recognized - if new entry of the DFA table is a non-negative integer
            int value = Integer.valueOf(tableEntry);
            if (value == 0) {
              // error occurs - ignore temporary character and re-analyze
              char wrongCh = this.chList.remove(0);
              this.resetAllAnalyzer();
              presentChIndexInList = 0;
              System.out.println("analysis error - ignore \"" + wrongCh + "\"");
              continue;
            } else {
              // enter next state successfully - continue analysis
              presentChIndexInList++;
            }
          } else {
            // token recognized - new entry of the DFA table is not a number
            // create a token and remove corresponding characters in chList
            Token token = this.buildToken(tableEntry, presentChIndexInList);
            for (int k = 0; k < presentChIndexInList; k++) {
              this.chList.remove(0);
            }
            this.tokens.add(token);
          }
        }
      }

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return this.tokens.getTokensCopy();
  }

  public Tokens getTokens() {
    return this.tokens.getTokensCopy();
  }

  private String analyzeChar(char ch) {
    /*
     * \TODO
     */
    return null;
  }

  private Token buildToken(String wordType, int borderIndex) {
    int wordSerialNumber = this.tokens.wordTypeToSerialNumber(wordType);
    String wordValue = "";
    for (int i = 0; i < borderIndex; i++) {
      wordValue = wordValue + this.chList.get(i);
    }
    return new Token(wordSerialNumber, wordValue);
  }

  private void resetAllAnalyzer() {
    this.charAnalyzer.reset();
    this.numAnalyzer.reset();
    this.idnAnalyzer.reset();
    this.stringAnalyzer.reset();
    this.op_dl_comAnalyzer.reset();
  }

  private boolean isNonnegativeInteger(String str) {
    Pattern pattern = Pattern.compile("^[\\+]?[\\d]+$");
    return pattern.matcher(str).matches();
  }

}
