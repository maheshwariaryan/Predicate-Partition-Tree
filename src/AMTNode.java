import java.util.LinkedList;
import java.util.Queue;

/**
 * This class represents a node in an AMT Tree. It holds 2 data values of different parity and
 * contains references to the parent and the two children
 */
public class AMTNode {

  protected boolean isRed = true;

  // even and odd values of the same node
  protected int even;
  protected int odd;

  // references to the left, right, and up nodes
  protected AMTNode left;
  protected AMTNode right;
  protected AMTNode up;

  /**
   * Constructor that creates a new node with the values even and odd. Both parent and child
   * references of the new node are initialized to null.
   *
   * @param value the value the new node stores
   * @param isEven a boolean that indicates if the value is even or odd
   */
  public AMTNode(int value, boolean isEven) {
    if (isEven)
      this.even = value;
    else
      this.odd = value;
  }

  public int even() {
    return this.even;
  }

  public int odd() {
    return this.odd;
  }

  public AMTNode getLeft() {
    return this.left;
  }

  public AMTNode getRight() {
    return this.right;
  }

  public AMTNode getUp() {
    return this.up;
  }

  public void setLeft(AMTNode newLeft) {
    this.left = newLeft;
  }

  public void setRight(AMTNode newRight) {
    this.right = newRight;
  }

  public void setUp(AMTNode newUp) {
    this.up = newUp;
  }

  public String toString() {
    return "" + even() + ", Odd: " + odd();
  }

  /**
   * Returns a string representation of the tree in level order.
   *
   * @return a string representation of the tree in level order
   */
  public String toLevelOrderString() {
    // create a linked list that we'll use as a queue to store inprocessed nodes
    Queue<AMTNode> nodeList = new LinkedList<>();
    // add this node to the queue first
    nodeList.add(this);
    // create the buffer to assemble the string efficiently
    StringBuffer sb = new StringBuffer();
    // add the bracket preceding the list of nodes to the buffer first
    sb.append("[ ");
    // keep processing nodes as long as we have any left on the queue
    while (!nodeList.isEmpty()) {
      // if it exists, add the left child of the head of the queue to the queue
      if (nodeList.peek().getLeft() != null) {
        nodeList.add(nodeList.peek().getLeft());
      }
      // if it exists, add the right child of the head of the queue to the queue
      if (nodeList.peek().getRight() != null) {
        nodeList.add(nodeList.peek().getRight());
      }
      // add the head of the queue to the string buffer and remove from queue
      sb.append(nodeList.poll().toString());
      // add a comma to separate values to the buffer, or close the bracket if
      // we've just added the last node to it
      if (nodeList.isEmpty()) {
        sb.append(" ]");
      } else {
        sb.append(", ");
      }
    }
    // return the string built with the string buffer
    return sb.toString() + (this.isRed() ? "(r)" : "(b)");
  }

  /**
   * Returns a boolean that indicates if this is a red or black node.
   *
   * @return true if the node is red, false if it is black
   */
  public boolean isRed() {
    return this.isRed;
  }

  /**
   * Inverts the color of this node, turning it either from red to black, or from black to red.
   */
  public void flipColor() {
    this.isRed = !this.isRed;
  }

  /**
   * @return true when this node has a parent and is the right child of
   * that parent, otherwise return false
   */
  public boolean isRightChild() {
    return this.getUp() != null && this.getUp().getRight() == this;
  }

  public void setValue(int value, boolean isEven) {
    if (isEven)
      this.even = value;
    else
      this.odd = value;
  }
}
