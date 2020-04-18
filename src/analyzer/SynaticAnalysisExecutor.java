package analyzer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class SynaticAnalysisExecutor {



  private HashMap<Integer, GrammarSymbol> tokenToTerminal;

  /**
   * 初始化token序号-语法符号的字典
   */
  public void initializeTokenMap() {
    tokenToTerminal = new HashMap<>();
    tokenToTerminal.put(1, new GrammarSymbol("id"));
    tokenToTerminal.put(2, new GrammarSymbol("number"));
    tokenToTerminal.put(3, new GrammarSymbol("number"));
    tokenToTerminal.put(4, new GrammarSymbol("number"));
    tokenToTerminal.put(5, new GrammarSymbol("number"));

    tokenToTerminal.put(8, new GrammarSymbol("annotation"));
    tokenToTerminal.put(9, new GrammarSymbol("int"));
    tokenToTerminal.put(10, new GrammarSymbol("double"));

    tokenToTerminal.put(11, new GrammarSymbol("boolean"));
    tokenToTerminal.put(12, new GrammarSymbol("record"));
    tokenToTerminal.put(13, new GrammarSymbol("if"));
    tokenToTerminal.put(14, new GrammarSymbol("else"));
    tokenToTerminal.put(15, new GrammarSymbol("do"));
    tokenToTerminal.put(16, new GrammarSymbol("while"));
    tokenToTerminal.put(17, new GrammarSymbol("proc"));
    tokenToTerminal.put(18, new GrammarSymbol("call"));
    tokenToTerminal.put(19, new GrammarSymbol("+"));
    tokenToTerminal.put(20, new GrammarSymbol("-"));

    tokenToTerminal.put(21, new GrammarSymbol("*"));
    tokenToTerminal.put(22, new GrammarSymbol("/"));
    tokenToTerminal.put(23, new GrammarSymbol("%"));
    tokenToTerminal.put(24, new GrammarSymbol("&"));
    tokenToTerminal.put(25, new GrammarSymbol("\\|"));
    tokenToTerminal.put(26, new GrammarSymbol("~"));
    tokenToTerminal.put(27, new GrammarSymbol("^"));
    tokenToTerminal.put(28, new GrammarSymbol("!"));
    tokenToTerminal.put(29, new GrammarSymbol(">"));
    tokenToTerminal.put(30, new GrammarSymbol("<"));

    tokenToTerminal.put(31, new GrammarSymbol(">="));
    tokenToTerminal.put(32, new GrammarSymbol("<="));
    tokenToTerminal.put(33, new GrammarSymbol("=="));
    tokenToTerminal.put(34, new GrammarSymbol("!="));
    tokenToTerminal.put(35, new GrammarSymbol("++"));
    tokenToTerminal.put(36, new GrammarSymbol("--"));
    tokenToTerminal.put(37, new GrammarSymbol("="));
    tokenToTerminal.put(38, new GrammarSymbol(";"));
    tokenToTerminal.put(39, new GrammarSymbol("("));
    tokenToTerminal.put(40, new GrammarSymbol(")"));

    tokenToTerminal.put(41, new GrammarSymbol("["));
    tokenToTerminal.put(42, new GrammarSymbol("]"));
    tokenToTerminal.put(43, new GrammarSymbol("{"));
    tokenToTerminal.put(44, new GrammarSymbol("}"));
    tokenToTerminal.put(45, new GrammarSymbol(","));
    tokenToTerminal.put(46, new GrammarSymbol("."));
    tokenToTerminal.put(47, new GrammarSymbol("return"));
    tokenToTerminal.put(48, new GrammarSymbol("&&"));
    tokenToTerminal.put(49, new GrammarSymbol("\\|\\|"));
    tokenToTerminal.put(50, new GrammarSymbol("then"));
    tokenToTerminal.put(51, new GrammarSymbol("$"));

  }

  public SynaticAnalysisExecutor() {
    initializeTokenMap();
  }

  /**
   * 
   * @param filepath
   * @return tokens
   */
  public ArrayList<String> getTokensFromFile(String filepath) {
    ArrayList<String> tokens = new ArrayList<>();
    try {
      BufferedReader br = new BufferedReader(new FileReader(new File(filepath)));
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
   * test用
   * 
   * @param m
   */
  public void Out(String m) {
    System.out.println(m);
  }

  public void errorLog(String log) {
    try {
      BufferedWriter bw = new BufferedWriter(new FileWriter(new File("src/log/synaticError.log")));
      if (log != null)
        bw.write(log);
      bw.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 根据预测分析表和token序列返回语法树
   * 
   * @param ppt
   * @param tokenpath
   * @return
   */
  public MutiWayTree getGrammaTree(PredictiveParsingTable ppt, String tokenpath) {


    Node rootnode = new Node(null, ppt.getProductions().getProduction(0).getLHS()); // 根节点
    MutiWayTree mwt = new MutiWayTree(rootnode); // 根据根节点创建树
    Stack<Node> stateStack = new Stack<>();
    stateStack.push(rootnode);
    ArrayList<String> tokens = new ArrayList<>(); // tokens列表
    tokens = getTokensFromFile(tokenpath);


    int count = 0;
    String errorString = "";
    for (int k = 0; k < tokens.size(); k++) {
      count++;
      String tokenNumString = tokens.get(k).replace("<", "");
      tokenNumString = tokenNumString.replace(">", ",");

      String[] tokenStringArray = tokenNumString.split(",");
      tokenNumString = tokenStringArray[0];
      String row = "last";
      String column = "last";
      if (!tokenNumString.equals("51")) {
        row = tokenStringArray[2].replace("(", "");
        column = tokenStringArray[3].replace(")", "");
      }


      int tokennum = Integer.parseInt(tokenNumString);
      GrammarSymbol tokenGS = tokenToTerminal.get(tokennum);// 当前Token的语法符号
      Node topNode = stateStack.pop();
      GrammarSymbol top = topNode.getGrammaSymbol();



      if (top.isTerminalSymbol() && top.equals(tokenGS)) {// 如果top是终结符且和当前输入token语法符号先沟通，则将其加入CurrentNode的子节点集合，并让栈顶指针指向下一个语法符号

        // Node kidnode = new Node(mwt.getCurrentNode(), top);

        // 终结符错误判断开始
        if (!top.equals(tokenGS)) {
          errorString += "(" + row + "," + column + ")" + "\n";
          k--;
          continue;
        }
        // 终结符错误判断结束


        mwt.setCurrentNode(stateStack.get(stateStack.size() - 1));

      } else if (top.isNonterminalSymbol()) {
        /*
         * 如果top是非终结符，将其首先把X逐出STACK栈顶,然后把产生式的右部符号串按反序一一推进STACK栈(若右部符号为ε,则意味着不推什么东西进栈).
         * 在把产生式的右部符号推进栈的同时把这个产生式右部所有语法符号作为一个节点加入到树结构中
         */



        Production pbreak = ppt.getProductionTableEntry(top, tokenGS);
        // 非终结符错误判断开始
        if (pbreak.equals(new Production())) {
          if (ppt.getSynchTableEntry(top, tokenGS)) {
            errorString += "(" + row + "," + column + ")" + "\n";
            k--;
            continue;
          } else {
            errorString += "(" + row + "," + column + ")" + "\n";
            stateStack.push(topNode);
            continue;
          }
        }


        // 非终结符错误判断结束

        System.out.println("count =" + count);
        List<GrammarSymbol> rightGrammars = new ArrayList<>();
        if (pbreak.getRHSlist().size() == 0) {
          rightGrammars = new ArrayList<>(Arrays.asList(new GrammarSymbol("empty")));
        } else {
          rightGrammars = pbreak.getRHSlist().get(0);// 是这样么？
        }



        k--;
        for (int i = rightGrammars.size() - 1; i >= 0; i--) {
          Node node = new Node(mwt.getCurrentNode(), rightGrammars.get(i));
          if (!rightGrammars.get(i).getName().equals("empty"))
            stateStack.push(node);
        }

        if (!stateStack.empty())
          mwt.setCurrentNode(stateStack.get(stateStack.size() - 1));
        else
          break;
      }

    }
    errorLog(errorString);
    mwt.backToTheRoot();
    return mwt;
  }



  public MutiWayTree SynaticAnalysis(String productionPath, String tokenPath) {
    Productions pros = new Productions(productionPath);
    PredictiveParsingTableConstructor prtc = new PredictiveParsingTableConstructor(pros);
    System.out.println(prtc.getPPTable().toString());
    SynaticAnalysisExecutor sAE = new SynaticAnalysisExecutor();
    MutiWayTree mwt = sAE.getGrammaTree(prtc.getPPTable(), tokenPath);
    return mwt;
  }

  /**
   * test
   * 
   * @param args
   */
  public static void main(String[] args) {
    // "src/data/input/productions1.format"
    // "src/data/input/tokens.token"
    System.out.println(new SynaticAnalysisExecutor()
        .SynaticAnalysis("src/data/input/productions1.format", "src/data/input/tokensError.token"));



  }

}
