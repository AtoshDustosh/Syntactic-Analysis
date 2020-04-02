package data.input; 

public enum InputFile {
  charDFA("src/data/input/charDFA.format"),
  idnDFA("src/data/input/idnDFA.format"),
  numDFA("src/data/input/numDFA.format"),
  operator_delimiter_com_DFA("src/data/input/operator_delimiter_com_DFA.format"),
  stringDFA("src/data/input/stringDFA.format"),
  ;

  private String filePath = null;

  private InputFile(String filePath) {
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
