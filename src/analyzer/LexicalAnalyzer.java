package analyzer;

/**
 * 根据程序流程，我要在某个主程序里面实现所有的这些步骤。
 * 主要是读取符号和进入对应的DFA，之后根据DFA自动机的结果判断是否将当前识别的单词录入token，
 * 或者判断为识别错误。因此就需要有一个Lexical Analyzer类，负责这个框架的执行
 * 框架内部，需要有一个读取文件符号的Scanner，一段调用各个类别的DFA自动机的代码，
 * 因此也就需要在内部建立各个类型的DFA。另外，还要有能够根据DFA返回结果录入token的功能。
 * 
 * @author AtoshDustosh
 */
public class LexicalAnalyzer {

  public LexicalAnalyzer() {

    /**
     * \TODO get all DFA analyzer ready
     */
  }

  public static void main(String[] args) {
    /**
     * \TODO read input data file and analyze its tokens.
     */

  }

}
