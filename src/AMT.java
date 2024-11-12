public class AMT {

  // root of the AMT
  protected AMTNode root;

  public void insert(int value) {
    // Check if value is less than 1
    if (value < 1) {
      throw new IllegalArgumentException("Value must be greater than or equal to 1");
    }

    if (value % 2 == 0) {
      // even
      AMTNode toAdd = new AMTNode(value, true);

      if (this.root == null) {
        toAdd.isRed = false;
        this.root = toAdd;
      } else {
        insertHelperEven(value, this.root);  // Changed to pass value instead of node
        ensureRedProperty(toAdd);
        (this.root).isRed = false;
      }
    } else {
      // odd
      AMTNode toAdd = new AMTNode(value, false);
      if (this.root == null) {
        toAdd.isRed = false;
        this.root = toAdd;
      } else {
        insertHelperOdd(value, this.root);    // Changed to pass value instead of node
        ensureRedProperty(toAdd);
        (this.root).isRed = false;
      }
    }
  }

  protected void insertHelperEven(int value, AMTNode subtree) {
    if (subtree == null) {
      return;
    }

    // If we find a node with even value 0, we can place our value here
    if (subtree.even() == 0) {
      subtree.setValue(value, true);
      return;
    }

    // Case 1: Smaller than subtree
    if (value <= subtree.even()) {
      if (subtree.getLeft() == null) {
        AMTNode newNode = new AMTNode(value, true);
        subtree.setLeft(newNode);
        newNode.setUp(subtree);
      } else {
        insertHelperEven(value, subtree.getLeft());
      }
    }
    // Case 2: Larger than subtree
    else {
      if (subtree.getRight() == null) {
        AMTNode newNode = new AMTNode(value, true);
        subtree.setRight(newNode);
        newNode.setUp(subtree);
      } else {
        insertHelperEven(value, subtree.getRight());
      }
    }
  }

  protected void insertHelperOdd(int value, AMTNode subtree) {
    if (subtree == null) {
      return;
    }

    // If we find a node with odd value 0, we can place our value here
    if (subtree.odd() == 0) {
      subtree.setValue(value, false);
      return;
    }

    // Case 1: Smaller than subtree
    if (value <= subtree.odd()) {
      if (subtree.getLeft() == null) {
        AMTNode newNode = new AMTNode(value, false);
        subtree.setLeft(newNode);
        newNode.setUp(subtree);
      } else {
        insertHelperOdd(value, subtree.getLeft());
      }
    }
    // Case 2: Larger than subtree
    else {
      if (subtree.getRight() == null) {
        AMTNode newNode = new AMTNode(value, false);
        subtree.setRight(newNode);
        newNode.setUp(subtree);
      } else {
        insertHelperOdd(value, subtree.getRight());
      }
    }
  }

  /**
   * Checks if a new red node in the RedBlackTree causes a red property violation by having a red
   * parent. If this is not the case, the method terminates without making any changes to the tree.
   * If a red property violation is detected, then the method repairs this violation and any
   * additional red property violations that are generated as a result of the applied repair
   * operation.
   *
   * @param newRedNode a newly inserted red node, or a node turned red by previous repair
   */
  protected void ensureRedProperty(AMTNode newRedNode) {
    // Base case: if newRedNode is the root, make it black
    if (newRedNode.getUp() == null) {
      newRedNode.isRed = false;
      return;
    }

    // Get parent and grandparent
    AMTNode parent = newRedNode.getUp();
    AMTNode grandparent = parent.getUp();

    // Case 1: Parent is black, no violation
    if (!parent.isRed()) {
      return;
    }

    // Initialize aunt
    AMTNode aunt;

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
  protected void rotate(AMTNode child, AMTNode parent)
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
   * Check whether data is stored in the tree.
   *
   * @param data the value to check for in the collection
   * @return true if the collection contains data one or more times, and false otherwise
   */
  public boolean contains(int data) {
    if(data % 2 == 0) {
      return containsHelperEven(data, this.root);
    } else {
      return containsHelperOdd(data, this.root);
    }
  }

  /**
   * Private helper method to recursively check whether data is stored in the tree.
   *
   * @param data the value to check for in the collection
   * @return true if the collection contains data one or more times, and false otherwise
   */
  private boolean containsHelperEven(int data, AMTNode node) {
    if (node == null) {
      return false;
    }

    if (data == node.even()) {
      return true;
    } else if (data < node.even()) {
      return containsHelperEven(data, node.getLeft());
    } else {
      return containsHelperEven(data, node.getRight());
    }
  }

  /**
   * Private helper method to recursively check whether data is stored in the tree.
   *
   * @param data the value to check for in the collection
   * @return true if the collection contains data one or more times, and false otherwise
   */
  private boolean containsHelperOdd(int data, AMTNode node) {
    if (node == null) {
      return false;
    }

    if (data == node.odd()) {
      return true;
    } else if (data < node.odd()) {
      return containsHelperOdd(data, node.getLeft());
    } else {
      return containsHelperOdd(data, node.getRight());
    }
  }
}
