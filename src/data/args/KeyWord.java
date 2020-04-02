package data.args;

import java.util.HashSet;
import java.util.Set;

/**
 * This class stores a set of key words and can judge whether a word
 * is a key word.
 * 
 * @author AtoshDustosh
 */
public class KeyWord {

  private Set<String> keyWordSet = new HashSet<String>();

  public KeyWord() {
    this.keyWordSet.add("int");
    this.keyWordSet.add("double");
    this.keyWordSet.add("boolean");
    this.keyWordSet.add("record");
    this.keyWordSet.add("if");
    this.keyWordSet.add("else");
    this.keyWordSet.add("do");
    this.keyWordSet.add("while");
    this.keyWordSet.add("function");
    this.keyWordSet.add("call");
  }

  public boolean isKeyWord(String word) {
    if (this.keyWordSet.contains(word)) {
      return true;
    } else {
      return false;
    }
  }

}
