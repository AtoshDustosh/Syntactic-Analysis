package grammaAnalysis.tree;

import java.util.ArrayList;
import grammaAnalysis.symbol.GrammaSymbol;

/**
 * Node of the tree
 * 
 * @author lenovo
 *
 */
public class Node {
  private Node parent;//
  private GrammaSymbol grammaSymbol;
  private ArrayList<Node> childrenList;// Child node set


  /**
   * The node is generated according to the list of input children, grammar symbols, and parent
   * nodes
   * 
   * @param p parent
   * @param gs grammar symbols
   * @param cl the list of input children
   */
  public Node(Node p, GrammaSymbol gs, ArrayList<Node> cl) {
    setParent(p);
    setGrammaSymbol(gs);
    setChildrenList(cl);
  }

  /**
   * Construct a node without children
   * 
   * @param p parent
   * @param gs grammar symbols
   * 
   */

  public Node(Node p, GrammaSymbol gs) {
    setParent(p);
    setGrammaSymbol(gs);
    setChildrenList(null);
  }


  /**
   * @return parent
   */
  public Node getParent() {
    return parent;
  }

  /**
   * @param parent 要设置的 parent
   */
  public void setParent(Node parent) {
    this.parent = parent;
  }

  /**
   * @return grammaSymbol
   */
  public GrammaSymbol getGrammaSymbol() {
    return grammaSymbol;
  }

  /**
   * @param grammaSymbol 要设置的 grammaSymbol
   */
  public void setGrammaSymbol(GrammaSymbol grammaSymbol) {
    this.grammaSymbol = grammaSymbol;
  }

  /**
   * @return childrenList
   */
  public ArrayList<Node> getChildrenList() {
    return childrenList;
  }

  /**
   * @param childrenList 要设置的 childrenList
   */
  public void setChildrenList(ArrayList<Node> childrenList) {
    this.childrenList = childrenList;
  }



}
