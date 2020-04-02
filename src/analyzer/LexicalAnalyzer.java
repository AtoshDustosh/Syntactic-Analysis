package analyzer;

/**
 * 根据程序流程，我要在某个主程序里面实现所有的这些步骤。
 * 主要是读取符号和进入对应的DFA，之后根据DFA自动机的结果判断是否将当前识别的单词录入token，
 * 或者判断为识别错误。因此就需要有一个Lexical Analyzer类，负责这个框架的执行框架内部，
 * 需要有一个读取文件符号的Scanner，一段调用各个类别的DFA自动机的代码，
 * 因此也就需要在内部建立各个类型的DFA。另外，还要有能够根据DFA返回结果录入token的功能。
 * 不过，如果我要实现图形化界面的话，就需要把所有的流程在另一个图形化程序的main函数中重复一遍， 这样一来，就不能在Lexical
 * Analyzer类的main函数中实现流程， 而是要在它的一个方法中实现。 This class can analyze a
 * program file of specific format and get all tokens of it.
 * 
 * @author AtoshDustosh
 */
public class LexicalAnalyzer {

  public LexicalAnalyzer() {

    /**
     * \TODO get all DFA analyzer ready
     */
  }

  public void analyzeFile(String filePath) {

  }

}
