package analyzer;

public class DfaAnalyzer {
  public static final String analysisErrorWordType = "analysis error";

  private String dfaWordType = null;
  private String dfaFilePath = null;

  private String analyzedWordType = null;

  /**
   * \TODO a field to store the DFA table and can be used to make
   * transformation among states.
   */

  /**
   * Set the word type of this DFA analyzer and load corresponding DFA
   * table from file.
   * 
   * @param dfaWordType type of word this DFA analyzer recognize
   * @param dfaFilePath file path of DFA table
   */
  public DfaAnalyzer(String dfaWordType, String dfaFilePath) {
    this.dfaWordType = dfaWordType;
    this.dfaFilePath = dfaFilePath;

  }

  private void loadDfaTableFile(String dfaFilePath) {

  }

  /**
   * Load a character and enter next state according to its DFA table.
   * 
   * @param ch character loaded
   * @return state serial number (>0) if successfully changed state; 0
   *         if error occurs such as when there is no next state
   *         according to DFA table or the analyzing process finishes.
   */
  public int nextState(char ch) {

    return 0;
  }

  /**
   * Get analyzed type name of the input word.
   * 
   * @return analyzed type name of the input word; analysisErrorWordType
   *         otherwise.
   */
  public String getAnalyzedWordType() {

    return null;
  }

  public String getDfaWordType() {
    return this.dfaWordType;
  }

}
