package analyzer;

/**
 * A multi-branch tree to show the syntax structure
 * 
 * @author lenovo
 */
public class MutiWayTree {

  private Node rootNode;// root node
  private Node node_Current;// The node to which the current pointer points

  public MutiWayTree(Node root) {
    this.rootNode = root;
    this.node_Current = root;
  }

  /**
   * @return root node
   */
  public Node getRoot() {
    return this.rootNode;
  }

  /**
   * get The node to which the current pointer points
   */
  public Node getCurrentNode() {
    return this.node_Current;
  }

  /**
   * Points the pointer to the root node
   */
  public void backToTheRoot() {
    this.node_Current = this.rootNode;
  }

}
