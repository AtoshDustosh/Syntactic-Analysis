package analyzer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
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

  Queue<String> chQueue = new LinkedList<>();

  public LexicalAnalyzer() {

  }

  public Tokens analyzeFile(String filePath) {
    try {
      Scanner scanner = new Scanner(new FileInputStream(filePath));
      boolean hasNext = scanner.hasNext();
      while (hasNext) {
        String result = null;
        char[] lineChars = (scanner.nextLine() + "\n").toCharArray();
        for (int i = 0; i < lineChars.length; i++) {
          if (lineChars[i] == ' ' || lineChars[i] == '\t') {
            continue;
          }
          /*
           * analyze input char and process queue.
           */
        }
        /*
         * 
         */
      }

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    return null;
  }

  private String analyzeChar(char ch) {

    return null;
  }

  private boolean isNonnegativeInteger(String str) {
    Pattern pattern = Pattern.compile("^[\\+]?[\\d]+$");
    return pattern.matcher(str).matches();
  }

}
