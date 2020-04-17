package grammaAnalysis.tree;

/**
 * A multi-branch tree to show the syntax structure
 * 
 * @author lenovo
 *
 */
public class MutiWayTree {

  private Node rootNode;// root node
  private Node node_Current;// The node to which the current pointer points


  public MutiWayTree(Node root) {
    rootNode = root;
    node_Current = root;
  }

  /**
   * 
   * @return root node
   */
  public Node getRoot() {
    return rootNode;
  }

  /**
   * get The node to which the current pointer points
   */
  public Node getCurrentNode() {
    return node_Current;
  }

  /**
   * Points the pointer to the root node
   */
  public void backToTheRoot() {
    node_Current = rootNode;
  }



}
