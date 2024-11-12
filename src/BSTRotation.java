/**
 * This class extends the BinarySearchTree_Placeholder class and implements the rotate method. The
 * rotate method performs a left or right rotation on the provided child and parent nodes. The class
 * also contains five test methods that test the rotate method on various scenarios.
 *
 * @param <T> is the type of data that the tree holds
 */
public class BSTRotation<T extends Comparable<T>> extends BinarySearchTree<T> {

  /**
   * Performs the rotation operation on the provided nodes within this tree. When the provided child
   * is a left child of the provided parent, this method will perform a right rotation. When the
   * provided child is a right child of the provided parent, this method will perform a left
   * rotation. When the provided nodes are not related in one of these ways, this method will either
   * throw a NullPointerException: when either reference is null, or otherwise will throw an
   * IllegalArgumentException.
   *
   * @param child  is the node being rotated from child to parent position
   * @param parent is the node being rotated from parent to child position
   * @throws NullPointerException     when either passed argument is null
   * @throws IllegalArgumentException when the provided child and parent nodes are not initially
   *                                  (pre-rotation) related that way
   */
  protected void rotate(BSTNode<T> child, BSTNode<T> parent)
      throws NullPointerException, IllegalArgumentException {

    // Null check on child and parent
    if (child == null || parent == null) {
      throw new NullPointerException("The child or parent node is null");
    }

    // Check if the child and parent node parameters are related
    if (parent.getLeft() != child && parent.getRight() != child) {
      throw new IllegalArgumentException("The child and parent nodes are not related");
    }

    // Performing the rotations
    if (child.isRightChild()) {
      // Left Rotation
      parent.setRight(child.getLeft());

      // Checking if the child has further child nodes
      if (child.getLeft() != null) {
        child.getLeft().setUp(parent);
      }

      child.setLeft(parent);

    } else {
      // Right Rotation
      parent.setLeft(child.getRight());

      // Checking if the child has further child nodes
      if (child.getRight() != null) {
        child.getRight().setUp(parent);
      }

      child.setRight(parent);
    }

    child.setUp(parent.getUp());
    parent.setUp(child);

    // Root rotations
    if (child.getUp() != null) {
      if (child.getUp().getLeft() == parent) {
        child.getUp().setLeft(child);
      } else {
        child.getUp().setRight(child);
      }
    } else {
      this.root = child;
    }
  }

  /**
   * This method tests the rotate method by performing both left and right rotations
   *
   * @return true if the tests pass, and false otherwise
   */
  public boolean test1() {
    BSTRotation<String> bst = new BSTRotation<>();

    bst.insert("E");
    bst.insert("B");
    bst.insert("H");
    bst.insert("A");
    bst.insert("D");
    bst.insert("F");
    bst.insert("J");
    bst.insert("C");
    bst.insert("G");
    bst.insert("I");
    bst.insert("K");

    bst.rotate(bst.root.getLeft().getLeft(), bst.root.getLeft());

    String expected = "[ E, A, H, B, F, J, D, G, I, K, C ]";

    if (!expected.equals(bst.root.toLevelOrderString())) {
      return false;
    }

    bst.rotate(bst.root.getRight().getRight(), bst.root.getRight());

    expected = "[ E, A, J, B, H, K, D, F, I, C, G ]";

    if (!expected.equals(bst.root.toLevelOrderString())) {
      return false;
    }

    return true;
  }

  /**
   * This method tests the rotate method on the root with both left and right rotations
   *
   * @return true if the tests pass, and false otherwise
   */
  public boolean test2() {
    BSTRotation<String> bst = new BSTRotation<>();

    bst.insert("E");
    bst.insert("B");
    bst.insert("H");
    bst.insert("A");
    bst.insert("D");
    bst.insert("F");
    bst.insert("J");
    bst.insert("C");
    bst.insert("G");
    bst.insert("I");
    bst.insert("K");

    bst.rotate(bst.root.getLeft(), bst.root);

    String expected = "[ B, A, E, D, H, C, F, J, G, I, K ]";

    if (!expected.equals(bst.root.toLevelOrderString())) {
      return false;
    }

    bst.rotate(bst.root.getRight(), bst.root);

    expected = "[ E, B, H, A, D, F, J, C, G, I, K ]";

    if (!expected.equals(bst.root.toLevelOrderString())) {
      return false;
    }

    return true;
  }

  /**
   * This method tests the rotate method by performing rotations on parent-child pairs of nodes that
   * have between them 0, 1, 2, and 3 shared children
   *
   * @return true if the tests pass, and false otherwise
   */
  public boolean test3() {
    BSTRotation<String> bst = new BSTRotation<>();

    bst.insert("E");
    bst.insert("B");
    bst.insert("H");
    bst.insert("A");
    bst.insert("D");
    bst.insert("F");
    bst.insert("J");
    bst.insert("C");
    bst.insert("G");
    bst.insert("I");
    bst.insert("K");

    bst.rotate(bst.root.getLeft().getLeft(), bst.root.getLeft());

    String expected = "[ E, A, H, B, F, J, D, G, I, K, C ]";

    if (!expected.equals(bst.root.toLevelOrderString())) {
      return false;
    }

    bst.rotate(bst.root.getRight().getLeft(), bst.root.getRight());

    expected = "[ E, A, F, B, H, D, G, J, C, I, K ]";

    if (!expected.equals(bst.root.toLevelOrderString())) {
      return false;
    }

    bst.rotate(bst.root.getRight().getRight(), bst.root.getRight());

    expected = "[ E, A, H, B, F, J, D, G, I, K, C ]";

    if (!expected.equals(bst.root.toLevelOrderString())) {
      return false;
    }

    return true;
  }

  /**
   * This method tests the rotate method by passing null references to it
   *
   * @return true if the tests pass, and false otherwise
   */
  public boolean test4() {
    BSTRotation<String> bst = new BSTRotation<>();

    bst.insert("E");
    bst.insert("B");
    bst.insert("H");
    bst.insert("A");
    bst.insert("D");
    bst.insert("F");
    bst.insert("J");
    bst.insert("C");
    bst.insert("G");
    bst.insert("I");
    bst.insert("K");

    try {
      bst.rotate(null, null);
    } catch (NullPointerException ignored) {

    } catch (Exception e) {
      return false;
    }

    try {
      bst.rotate(null, bst.root);
    } catch (NullPointerException ignored) {

    } catch (Exception e) {
      return false;
    }

    try {
      bst.rotate(bst.root, null);
    } catch (NullPointerException ignored) {

    } catch (Exception e) {
      return false;
    }

    return true;
  }

  /**
   * This method tests the rotate method by passing a child-parent pair of nodes that are not
   * related
   *
   * @return true if the tests pass, and false otherwise
   */
  public boolean test5() {
    BSTRotation<String> bst = new BSTRotation<>();

    bst.insert("E");
    bst.insert("B");
    bst.insert("H");
    bst.insert("A");
    bst.insert("D");
    bst.insert("F");
    bst.insert("J");
    bst.insert("C");
    bst.insert("G");
    bst.insert("I");
    bst.insert("K");

    try {
      bst.rotate(bst.root.getLeft(), bst.root.getRight());
    } catch (IllegalArgumentException ignored) {
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  /**
   * Main method to run the tests.
   *
   * @param args unused
   */
  public static void main(String[] args) {
    BSTRotation<Integer> bst = new BSTRotation<>();
    System.out.println("Test 1: " + (bst.test1() ? "PASSED" : "FAILED"));
    System.out.println("Test 2: " + (bst.test2() ? "PASSED" : "FAILED"));
    System.out.println("Test 3: " + (bst.test3() ? "PASSED" : "FAILED"));
    System.out.println("Test 4: " + (bst.test4() ? "PASSED" : "FAILED"));
    System.out.println("Test 5: " + (bst.test5() ? "PASSED" : "FAILED"));
  }
}


