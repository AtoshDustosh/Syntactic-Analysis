package data.input;

public enum InputFilePaths {
  charDFA("src/data/input/charDFA.format"),
  idnDFA("src/data/input/idnDFA.format"),
  numDFA("src/data/input/numDFA.format"),
  op_dl_comDFA("src/data/input/operator_delimiter_com_DFA.format"),
  stringDFA("src/data/input/stringDFA.format"),
  lexicalTest1("src/test/test1.code"),
  lexicalTestError("src/test/testError.code"),

  ;

  private String filePath = null;

  private InputFilePaths(String filePath) {
    this.filePath = filePath;
  }

  /**
   * Get the path of the input file.
   * 
   * @return file path
   */
  public String getFilePath() {
    return this.filePath;
  }
}
