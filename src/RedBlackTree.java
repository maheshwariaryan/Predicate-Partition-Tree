import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * This class represents a Red-Black Tree, a self-balancing binary search tree. The tree is
 * structured such that each node has a color attribute, either red or black, and the root node is
 * always black.
 *
 * @param <T>
 */
public class RedBlackTree<T extends Comparable<T>> extends BSTRotation<T> {

  /**
   * Checks if a new red node in the RedBlackTree causes a red property violation by having a red
   * parent. If this is not the case, the method terminates without making any changes to the tree.
   * If a red property violation is detected, then the method repairs this violation and any
   * additional red property violations that are generated as a result of the applied repair
   * operation.
   *
   * @param newRedNode a newly inserted red node, or a node turned red by previous repair
   */
  protected void ensureRedProperty(RBTNode<T> newRedNode) {
    // Base case: if newRedNode is the root, make it black
    if (newRedNode.getUp() == null) {
      newRedNode.isRed = false;
      return;
    }

    // Get parent and grandparent
    RBTNode<T> parent = newRedNode.getUp();
    RBTNode<T> grandparent = parent.getUp();

    // Case 1: Parent is black, no violation
    if (!parent.isRed()) {
      return;
    }

    // Initialize aunt
    RBTNode<T> aunt;

    // Get aunt
    if (parent.isRightChild()) {
      aunt = grandparent.getLeft();
    } else {
      aunt = grandparent.getRight();
    }

    // Case 2: Parent is red, Aunt is red
    if (aunt != null && aunt.isRed()) {

      parent.flipColor();
      aunt.flipColor();
      grandparent.flipColor();

      //Recursive call to ensure red property on the entire tree
      ensureRedProperty(grandparent);
      return;
    }

    // Case 3: Parent is red, Aunt is black or null
    if (aunt == null || !aunt.isRed()) {
      // Left-Left Case
      if (parent == grandparent.getLeft() && newRedNode == parent.getLeft()) {
        rotate(parent, grandparent);
        parent.flipColor();
        grandparent.flipColor();
      }
      // Right-Right Case
      else if (parent == grandparent.getRight() && newRedNode == parent.getRight()) {
        rotate(parent, grandparent);
        grandparent.flipColor();
        parent.flipColor();
      }
      // Left-Right Case
      else if (parent == grandparent.getLeft() && newRedNode == parent.getRight()) {
        rotate(newRedNode, parent);
        rotate(newRedNode, grandparent);
        newRedNode.flipColor();
        grandparent.flipColor();
      }
      // Right-Left Case
      else if (parent == grandparent.getRight() && newRedNode == parent.getLeft()) {
        rotate(newRedNode, parent);
        rotate(newRedNode, grandparent);
        newRedNode.flipColor();
        grandparent.flipColor();
      }
    }
  }

  /**
   * Inserts a new node with the provided data into the RedBlackTree. The new node must be inserted
   * according to the standard BST insertion algorithm. After insertion, the tree must be checked
   * for red property violations and repaired if necessary.
   *
   * @param data the data to insert into the tree
   * @throws NullPointerException if the provided data is null
   */
  @Override
  public void insert(T data) throws NullPointerException {
    if (data == null) {
      throw new NullPointerException("Provided data is null!");
    }
    RBTNode<T> toAdd = new RBTNode<>(data);
    if (this.root == null) {
      toAdd.isRed = false;
      this.root = toAdd;

    } else {
      toAdd.isRed = true;
      insertHelper(toAdd, this.root);
      ensureRedProperty(toAdd);
      ((RBTNode<T>) this.root).isRed = false;
    }
  }

  /**
   * Tests the insertion of a red node to a black parent in the RedBlackTree.
   */
  @Test
  public void testBlackParent() {
    RedBlackTree<Integer> tree = new RedBlackTree<>();

    tree.insert(10);
    tree.insert(20);
    tree.insert(5);
    tree.insert(3);

    // Adding a red node to a black parent
    tree.insert(25);

    String expected = "[ 10(b), 5(b), 20(b), 3(r), 25(r) ]";

    assertEquals(expected, tree.root.toLevelOrderString());
  }

  /**
   * Tests the right-right rotation scenario in the RedBlackTree.
   */
  @Test
  public void testRightRight() {
    RedBlackTree<Integer> bst = new RedBlackTree<>();

    bst.insert(10);
    bst.insert(20);
    bst.insert(30);
    bst.insert(25);

    // Expected structure after RR rotations and rebalancing
    String expectedLevelOrder = "[ 20(b), 10(b), 30(b), 25(r) ]";

    assertEquals(expectedLevelOrder, bst.root.toLevelOrderString());
  }

  /**
   * Tests the left-left rotation scenario in the RedBlackTree.
   */
  @Test
  public void testLeftLeft() {
    RedBlackTree<Integer> bst = new RedBlackTree<>();

    bst.insert(40);
    bst.insert(35);
    bst.insert(25);
    bst.insert(10);
    bst.insert(1);

    String expectedLevelOrder = "[ 35(b), 10(b), 40(b), 1(r), 25(r) ]";

    assertEquals(expectedLevelOrder, bst.root.toLevelOrderString());
  }

  /**
   * Tests the left-right rotation scenario in the RedBlackTree.
   */
  @Test
  public void testLeftRight() {
    RedBlackTree<Integer> bst = new RedBlackTree<>();

    bst.insert(40);
    bst.insert(20);
    bst.insert(30); // Added to test left-right rotation
    bst.insert(55);

    String expectedLevelOrder = "[ 30(b), 20(b), 40(b), 55(r) ]";

    assertEquals(expectedLevelOrder, bst.root.toLevelOrderString());
  }

  /**
   * Tests the right-left rotation scenario in the RedBlackTree.
   */
  @Test
  public void testRightLeft() {
    RedBlackTree<Integer> bst = new RedBlackTree<>();

    bst.insert(50);
    bst.insert(100);
    bst.insert(75); // Added to test right-left rotation

    // Rotation should occur
    String expectedLevelOrder = "[ 75(b), 50(r), 100(r) ]";

    assertEquals(expectedLevelOrder, bst.root.toLevelOrderString());

    bst.insert(80);

    expectedLevelOrder = "[ 75(b), 50(b), 100(b), 80(r) ]";

    assertEquals(expectedLevelOrder, bst.root.toLevelOrderString());
  }

  /**
   * Tests the insertion of multiple nodes in the RedBlackTree and verifies the tree structure.
   */
  @Test
  public void quizTreeTest() {
    RedBlackTree<String> bst = new RedBlackTree<>();

    bst.insert("R");
    bst.insert("I");
    bst.insert("V");
    bst.insert("F");
    bst.insert("M");
    bst.insert("X");
    bst.insert("C");
    bst.insert("H");
    bst.insert("T");

    String expectedLevelOrder = "[ R(b), I(r), V(b), F(b), M(b), T(r), X(r), C(r), H(r) ]";

    assertEquals(expectedLevelOrder, bst.root.toLevelOrderString());
  }
}


