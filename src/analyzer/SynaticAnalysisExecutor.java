package analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class SynaticAnalysisExecutor {

  private HashMap<Integer, GrammarSymbol> tokenToTerminal;

  /**
   * ��ʼ��token���-�﷨���ŵ��ֵ�
   */
  public void initializeTokenMap() {
    this.tokenToTerminal = new HashMap<>();
    this.tokenToTerminal.put(1, new GrammarSymbol("id"));
    this.tokenToTerminal.put(2, new GrammarSymbol("number"));
    this.tokenToTerminal.put(3, new GrammarSymbol("number"));
    this.tokenToTerminal.put(4, new GrammarSymbol("number"));
    this.tokenToTerminal.put(5, new GrammarSymbol("number"));

    this.tokenToTerminal.put(8, new GrammarSymbol("annotation"));
    this.tokenToTerminal.put(9, new GrammarSymbol("int"));
    this.tokenToTerminal.put(10, new GrammarSymbol("double"));

    this.tokenToTerminal.put(11, new GrammarSymbol("boolean"));
    this.tokenToTerminal.put(12, new GrammarSymbol("record"));
    this.tokenToTerminal.put(13, new GrammarSymbol("if"));
    this.tokenToTerminal.put(14, new GrammarSymbol("else"));
    this.tokenToTerminal.put(15, new GrammarSymbol("do"));
    this.tokenToTerminal.put(16, new GrammarSymbol("while"));
    this.tokenToTerminal.put(17, new GrammarSymbol("proc"));
    this.tokenToTerminal.put(18, new GrammarSymbol("call"));
    this.tokenToTerminal.put(19, new GrammarSymbol("+"));
    this.tokenToTerminal.put(20, new GrammarSymbol("-"));

    this.tokenToTerminal.put(21, new GrammarSymbol("*"));
    this.tokenToTerminal.put(22, new GrammarSymbol("/"));
    this.tokenToTerminal.put(23, new GrammarSymbol("%"));
    this.tokenToTerminal.put(24, new GrammarSymbol("&"));
    this.tokenToTerminal.put(25, new GrammarSymbol("\\|"));
    this.tokenToTerminal.put(26, new GrammarSymbol("~"));
    this.tokenToTerminal.put(27, new GrammarSymbol("^"));
    this.tokenToTerminal.put(28, new GrammarSymbol("!"));
    this.tokenToTerminal.put(29, new GrammarSymbol(">"));
    this.tokenToTerminal.put(30, new GrammarSymbol("<"));

    this.tokenToTerminal.put(31, new GrammarSymbol(">="));
    this.tokenToTerminal.put(32, new GrammarSymbol("<="));
    this.tokenToTerminal.put(33, new GrammarSymbol("=="));
    this.tokenToTerminal.put(34, new GrammarSymbol("!="));
    this.tokenToTerminal.put(35, new GrammarSymbol("++"));
    this.tokenToTerminal.put(36, new GrammarSymbol("--"));
    this.tokenToTerminal.put(37, new GrammarSymbol("="));
    this.tokenToTerminal.put(38, new GrammarSymbol(";"));
    this.tokenToTerminal.put(39, new GrammarSymbol("("));
    this.tokenToTerminal.put(40, new GrammarSymbol(")"));

    this.tokenToTerminal.put(41, new GrammarSymbol("["));
    this.tokenToTerminal.put(42, new GrammarSymbol("]"));
    this.tokenToTerminal.put(43, new GrammarSymbol("{"));
    this.tokenToTerminal.put(44, new GrammarSymbol("}"));
    this.tokenToTerminal.put(45, new GrammarSymbol(","));
    this.tokenToTerminal.put(46, new GrammarSymbol("."));
    this.tokenToTerminal.put(47, new GrammarSymbol("return"));
    this.tokenToTerminal.put(48, new GrammarSymbol("&&"));
    this.tokenToTerminal.put(49, new GrammarSymbol("||"));
    this.tokenToTerminal.put(50, new GrammarSymbol("then"));

  }

  public SynaticAnalysisExecutor() {
    this.initializeTokenMap();
  }

  /**
   * @param filepath
   * @return tokens
   */
  public List<String> getTokensFromFile(String filepath) {
    ArrayList<String> tokens = new ArrayList<>();
    try {
      BufferedReader br = new BufferedReader(
          new FileReader(new File(filepath)));
      String line;
      while ((line = br.readLine()) != null) {
        tokens.add(line);
      }
      br.close();
    } catch (Exception e) {
      // TODO: handle exception
    }
    return tokens;
  }

  /**
   * ����Ԥ��������token���з����﷨��
   * 
   * @param ppt
   * @param tokenpath
   * @return
   */
  public MutiWayTree getGrammaTree(PredictiveParsingTable ppt,
      String tokenpath) {

    Node rootnode = new Node(null, new GrammarSymbol("PROGRAM")); // ���ڵ�
    MutiWayTree mwt = new MutiWayTree(rootnode); // ���ݸ��ڵ㴴����
    Stack<Node> stateStack = new Stack<>();
    stateStack.push(rootnode);
    ArrayList<String> tokens = new ArrayList<>(); // tokens�б�
    tokens = new ArrayList<>(this.getTokensFromFile(tokenpath));

    for (String token : tokens) {
      GrammarSymbol tokenGS = this.tokenToTerminal
          .get(Integer.parseInt(token.split("<|>|,")[0]));// ��ǰToken���﷨����
      Node topNode = stateStack.pop();
      GrammarSymbol top = topNode.getGrammaSymbol();

      if (top.isTerminalSymbol() && top.equals(tokenGS)) {// ���top���ս���Һ͵�ǰ����token�﷨�����ȹ�ͨ���������CurrentNode���ӽڵ㼯�ϣ�����ջ��ָ��ָ����һ���﷨����

        Node kidnode = new Node(mwt.getCurrentNode(), top);
        mwt.setCurrentNode(stateStack.firstElement());

      } else if (top.isNonterminalSymbol()) {
        /*
         * ���top�Ƿ��ս�����������Ȱ�X���STACKջ��,Ȼ��Ѳ���ʽ���Ҳ����Ŵ�������һһ�ƽ�STACKջ(���Ҳ�����Ϊ��,����ζ�Ų���ʲô������ջ).
         * �ڰѲ���ʽ���Ҳ������ƽ�ջ��ͬʱ���������ʽ�Ҳ������﷨������Ϊһ���ڵ���뵽���ṹ��
         */
        Production pbreak = ppt.getProductionTableEntry(top, tokenGS);
        ArrayList<GrammarSymbol> rightGrammars = new ArrayList<>(
            pbreak.getRHSlist().get(0));// ������ô��

        for (int i = rightGrammars.size() - 1; i >= 0; i--) {
          Node node = new Node(mwt.getCurrentNode(), rightGrammars.get(i));
        }
        mwt.setCurrentNode(stateStack.firstElement());
      }
    }
    mwt.backToTheRoot();
    return mwt;
  }

  /**
   * test
   * 
   * @param args
   */
  public static void main(String[] args) {

  }

}
