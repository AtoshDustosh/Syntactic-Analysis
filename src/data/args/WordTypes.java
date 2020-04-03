package data.args;

/**
 * Types of words that can be recognized.
 * 
 * @author AtoshDustosh
 */
public enum WordTypes {
  IDN("IDN"), KEYWORD("KEYWORD"), NUM("NUM"), STRING("STRING"), CHAR("CHAR"), OP_DL_COM(
      "Operator_Delimiter_COM"),;

  // 各类型的种别码
  public static int idn_code = 0;
  public static int keyword_code = 1;
  public static int num_code = 2;
  public static int string_code = 3;
  public static int char_code = 4;
  public static int op_dl_com_code = 5;


  private String type = null;

  private WordTypes(String type) {
    this.type = type;
  }

  public String getType() {
    return this.type;
  }

  /**
   * Returns a type code by type string
   * 
   * @return type code by type string
   */
  public int getCode(String type) {
    if (type.equals("IDN")) {
      return idn_code;
    } else if (type.equals("KEYWORD")) {
      return keyword_code;
    } else if (type.equals("NUM")) {
      return num_code;
    } else if (type.equals("STRING")) {
      return string_code;
    } else if (type.equals("CHAR")) {
      return char_code;
    } else if (type.equals("Operator_Delimiter_COM")) {
      return op_dl_com_code;
    }
    return -1;
  }

}
