class DualRBT {
  private RedBlackTree<Integer> evenTree;
  private RedBlackTree<Integer> oddTree;

  public DualRBT() {
    evenTree = new RedBlackTree<Integer>();
    oddTree = new RedBlackTree<Integer>();
  }

  public void insert(int value) {
    if (value < 1) {
      throw new IllegalArgumentException("Value must be greater than or equal to 1");
    }

    if (value % 2 == 0) {
      evenTree.insert(value);
    } else {
      oddTree.insert(value);
    }
  }

  public boolean contains(int value) {
    if (value % 2 == 0) {
      return evenTree.contains(value);
    } else {
      return oddTree.contains(value);
    }
  }
}
