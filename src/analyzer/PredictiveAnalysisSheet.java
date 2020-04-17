package analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PredictiveAnalysisSheet {
  private ArrayList<GrammarSymbol> nonTerminalList;// �б꣺���ս���ļ���
  private ArrayList<GrammarSymbol> terminalList;// �б꣺�ս�����б�
  private Production[][] analysisList;// ������
  private boolean[][] synch;
  HashMap<Integer, GrammarSymbol> TokenToTerminal;// Token���������֮���ת��

  /**
   * ˼·��1����ȡ�ļ���ʼ�� �б��б� �� Token���������֮���ת����2����ȡProductions����ȡSelect���ϣ�����Ԥ�������
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
   * @return Ԥ�������
   */

  public Production[][] getAnalysisList() {
    return analysisList;
  }

  /**
   * ͨ������ĸ�ʽ�ļ���ʼ��Ԥ�������
   */

  public void constructPredictSheet(String path) {
    ArrayList<String> formatStrings = new ArrayList<>();
    try {
      BufferedReader br = new BufferedReader(new FileReader(new File(path)));
      String line;
      while ((line = br.readLine()) != null) {
        formatStrings.add(line);
      }
      // ����TokenToTerminal��ʼ
      for (int i = 2; i < formatStrings.size(); i++) {
        String[] tokenStringline = formatStrings.get(i).split(" ");
        int tokenCode = Integer.parseInt(tokenStringline[1]);
        GrammarSymbol terminalSymbol = new GrammarSymbol(tokenStringline[2]);
        TokenToTerminal.put(tokenCode, terminalSymbol);
      }
      // ����TokenToTerminal����


      // ����terminalList��ʼ
      String terminalLine = formatStrings.get(1);
      terminalLine = terminalLine.replace("Terminal:", "");
      String[] terminalarray = terminalLine.split(" ");
      for (int i = 0; i < terminalarray.length; i++) {
        terminalList.add(new GrammarSymbol(terminalarray[i]));
      }
      // ����terminalList����

      // ����nonTerminalList��ʼ
      String nonTerminalLine = formatStrings.get(0);
      nonTerminalLine = nonTerminalLine.replace("nonTerminal:", "");
      String[] nonTerminalarray = nonTerminalLine.split(",");
      for (int i = 0; i < nonTerminalarray.length; i++) {
        nonTerminalList.add(new GrammarSymbol(nonTerminalarray[i]));
      }
      // ����nonTerminalList����

      // ��ʼ��analysisList��ʼ
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
      // ��ʼ��analysisList����

      br.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * ��������ķ��ս�������б�
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
    return -1;// ����-1����û�в��ҵ�
  }

  /**
   * ����������ս�������б�
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
    return -1;// ����-1����û�в��ҵ�
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
