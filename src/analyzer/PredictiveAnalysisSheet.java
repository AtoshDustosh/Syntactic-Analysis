package analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PredictiveAnalysisSheet {
  private ArrayList<GrammarSymbol> nonTerminalList;// 行标：非终结符的集合
  private ArrayList<GrammarSymbol> terminalList;// 列标：终结符的列表
  private Production[][] analysisList;// 分析表
  private boolean[][] synch;
  HashMap<Integer, GrammarSymbol> TokenToTerminal;// Token到输入符号之间的转换

  /**
   * 思路：1，读取文件初始化 行标列标 和 Token到输入符号之间的转换。2，读取Productions，读取Select集合，构建预测分析表
   */

  public PredictiveAnalysisSheet(String path) {
    nonTerminalList = new ArrayList<>();
    terminalList = new ArrayList<>();
    TokenToTerminal = new HashMap<>();
    constructPredictSheet(path);
  }

  public List<GrammarSymbol> getTerminals() {
    return terminalList;
  }

  /**
   * 
   * @return 预测分析表
   */

  public Production[][] getAnalysisList() {
    return analysisList;
  }

  /**
   * 通过读入的格式文件初始化预测分析表
   */

  public void constructPredictSheet(String path) {
    ArrayList<String> formatStrings = new ArrayList<>();
    try {
      BufferedReader br = new BufferedReader(new FileReader(new File(path)));
      String line;
      while ((line = br.readLine()) != null) {
        formatStrings.add(line);
      }
      // 构建TokenToTerminal开始
      for (int i = 2; i < formatStrings.size(); i++) {
        String[] tokenStringline = formatStrings.get(i).split(" ");
        int tokenCode = Integer.parseInt(tokenStringline[1]);
        GrammarSymbol terminalSymbol = new GrammarSymbol(tokenStringline[2]);
        TokenToTerminal.put(tokenCode, terminalSymbol);
      }
      // 构建TokenToTerminal结束


      // 构建terminalList开始
      String terminalLine = formatStrings.get(1);
      terminalLine = terminalLine.replace("Terminal:", "");
      String[] terminalarray = terminalLine.split(" ");
      for (int i = 0; i < terminalarray.length; i++) {
        terminalList.add(new GrammarSymbol(terminalarray[i]));
      }
      // 构建terminalList结束

      // 构建nonTerminalList开始
      String nonTerminalLine = formatStrings.get(0);
      nonTerminalLine = nonTerminalLine.replace("nonTerminal:", "");
      String[] nonTerminalarray = nonTerminalLine.split(",");
      for (int i = 0; i < nonTerminalarray.length; i++) {
        nonTerminalList.add(new GrammarSymbol(nonTerminalarray[i]));
      }
      // 构建nonTerminalList结束

      // 初始化analysisList开始
      analysisList = new Production[nonTerminalList.size()][terminalList.size()];
      for (int i = 0; i < nonTerminalList.size(); i++) {
        for (int j = 0; j < terminalList.size(); j++) {
          analysisList[i][j] = null;
        }
      }
      synch = new boolean[nonTerminalList.size()][terminalList.size()];
      for (int i = 0; i < nonTerminalList.size(); i++) {
        for (int j = 0; j < terminalList.size(); j++) {
          synch[i][j] = false;
        }
      }
      // 初始化analysisList结束

      br.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 根据输入的非终结符返回行标
   * 
   * @param gs
   * @return
   */
  public int getSerialNumberFromNonTerminal(GrammarSymbol gs) {
    int length = nonTerminalList.size();
    for (int i = 0; i < length; i++) {
      if (gs.equals(nonTerminalList.get(i))) {
        return i;
      }
    }
    return -1;// 返回-1代表没有查找到
  }

  /**
   * 根据输入的终结符返回列标
   * 
   * @param gs
   * @return
   */

  public int getSerialNumberFromTermninal(GrammarSymbol gs) {
    int length = terminalList.size();
    for (int i = 0; i < length; i++) {
      if (gs.equals(terminalList.get(i))) {
        return i;
      }
    }
    return -1;// 返回-1代表没有查找到
  }



  /**
   * test
   */
  public static void main(String[] args) {
    PredictiveAnalysisSheet pas = new PredictiveAnalysisSheet("src/data/input/format1.pas");
    ArrayList<GrammarSymbol> t = new ArrayList<>(pas.getTerminals());
    for (GrammarSymbol s : t) {
      System.out.println(s.toString());
    }
  }

}
