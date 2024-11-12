//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title:    Binary Search Tree
// Course:   CS 400 Fall 2024
//
// Author:   Aryan Maheshwari
// Email:    maheshwari25@wisc.edu
// Lecturer: Gary Dahl
//
//////////////////////// ASSISTANCE/HELP CITATIONS ////////////////////////////
//
// None
//
///////////////////////////////////////////////////////////////////////////////

/**
 * This class represents a binary search tree that stores data values of type T. It can perform
 * insertion, check if a value is stored in the tree, count the number of values in the tree, check
 * if the tree is empty, and clear the tree.
 */
public class BinarySearchTree<T extends Comparable<T>> implements SortedCollection<T> {

  /**
   * Protected field to store the root of the binary search tree.
   */
  protected BSTNode<T> root;

  /**
   * Inserts a new data value into the sorted collection.
   *
   * @param data the new value being inserted
   * @throws NullPointerException if data argument is null, we do not allow null values to be stored
   *                              within a SortedCollection
   */
  @Override
  public void insert(T data) throws NullPointerException {
    if (data == null) {
      throw new NullPointerException("Provided data is null!");
    }
    BSTNode<T> toAdd = new BSTNode<>(data);
    if (this.root == null) {
      this.root = toAdd;
    } else {
      insertHelper(toAdd, this.root);
    }
  }

  /**
   * Check whether data is stored in the tree.
   *
   * @param data the value to check for in the collection
   * @return true if the collection contains data one or more times, and false otherwise
   */
  @Override
  public boolean contains(Comparable<T> data) {
    if (data == null) {
      return false;
    }

    return containsHelper(data, this.root);
  }

  /**
   * Private helper method to recursively check whether data is stored in the tree.
   *
   * @param data the value to check for in the collection
   * @return true if the collection contains data one or more times, and false otherwise
   */
  private boolean containsHelper(Comparable<T> data, BSTNode<T> node) {
    if (node == null) {
      return false;
    }
    if (data.compareTo(node.getData()) == 0) {
      return true;
    } else if (data.compareTo(node.getData()) < 0) {
      return containsHelper(data, node.getLeft());
    } else {
      return containsHelper(data, node.getRight());
    }
  }

  /**
   * Counts the number of values in the collection, with each duplicate value being counted
   * separately within the value returned.
   *
   * @return the number of values in the collection, including duplicates
   */
  @Override
  public int size() {
    return sizeHelper(this.root);
  }

  /**
   * Recursive helper method to count the number of values in the collection, with each duplicate
   * value being counted separately within the value returned.
   *
   * @return the number of values in the collection, including duplicates
   */
  private int sizeHelper(BSTNode<T> node) {
    if (node == null) {
      return 0;
    }
    return 1 + sizeHelper(node.getLeft()) + sizeHelper(node.getRight());
  }

  /**
   * Checks if the collection is empty.
   *
   * @return true if the collection contains 0 values, false otherwise
   */
  @Override
  public boolean isEmpty() {
    return this.root == null;
  }

  /**
   * Removes all values and duplicates from the collection.
   */
  @Override
  public void clear() {
    this.root = null;
  }

  /**
   * Performs the naive binary search tree insert algorithm to recursively insert the provided
   * newNode (which has already been initialized with a data value) into the provided tree/subtree.
   * When the provided subtree is null, this method does nothing.
   */
  protected void insertHelper(BSTNode<T> newNode, BSTNode<T> subtree) {
    // Base case : return if subtree is null
    if (subtree == null) {
      return;
    }

    // Case 1 : Smaller than subtree
    if (newNode.getData().compareTo(subtree.getData()) <= 0) {
      if (subtree.getLeft() == null) {
        subtree.setLeft(newNode);
        newNode.setUp(subtree);
      } else {
        insertHelper(newNode, subtree.getLeft());
      }
    }

    // Case 2 : Larger than subtree
    if (newNode.getData().compareTo(subtree.getData()) > 0) {
      if (subtree.getRight() == null) {
        subtree.setRight(newNode);
        newNode.setUp(subtree);
      } else {
        insertHelper(newNode, subtree.getRight());
      }
    }

    // Case 3 : Equal to subtree
//    if (newNode.getData().compareTo(subtree.getData()) == 0) {
//      if (subtree.getLeft() == null) {
//        subtree.setLeft(newNode);
//        newNode.setUp(subtree);
//      } else {
//        newNode.setLeft(subtree.getLeft());
//        newNode.setUp(subtree);
//        subtree.setLeft(newNode);
//      }
//    }
  }

  /**
   * Test 1: Inserting multiple values to create differently shaped trees and checking if it is
   * inserted correctly
   *
   * @return true if the program works as intended, false otherwise
   */
  public boolean test1() {
    BinarySearchTree<Integer> bst = new BinarySearchTree<>();
    bst.insert(9);
    bst.insert(15);
    bst.insert(5);
    bst.insert(4);
    bst.insert(9);
    bst.insert(1);

    // Checking if values are inserted correctly
    String order = "[ 9, 9, 15, 5, 4, 1 ]";
    if (order.compareTo(bst.root.toLevelOrderString()) != 0) {
      return false;
    }

    // Again inserting a value to check if it is inserted correctly
    bst.insert(11);

    order = "[ 9, 9, 15, 5, 11, 4, 1 ]";

    if (order.compareTo(bst.root.toLevelOrderString()) != 0) {
      return false;
    }

    return (bst.contains(9) && bst.contains(15) && bst.contains(5) && bst.contains(
        4) && bst.contains(9) && bst.contains(1) && bst.contains(11) && !bst.contains(
        8) && !bst.contains(19));
  }

  /**
   * Test 2: Finding values in different positions and ensuring size is correct
   *
   * @return true if the program works as intended, false otherwise
   */
  public boolean test2() {
    BinarySearchTree<String> bst = new BinarySearchTree<>();
    bst.insert("Honda");
    bst.insert("Bugatti");
    bst.insert("Jaguar");
    bst.insert("Acura");
    bst.insert("Chevrolet");
    bst.insert("Infiniti");
    bst.insert("Konigsegg");

    String order = "[ Honda, Bugatti, Jaguar, Acura, Chevrolet, Infiniti, Konigsegg ]";

    if (order.compareTo(bst.root.toLevelOrderString()) != 0) {
      return false;
    }

    return bst.contains("Acura") && bst.contains("Bugatti") && bst.contains(
        "Jaguar") && bst.contains("Honda") && bst.contains("Chevrolet") && bst.contains(
        "Infiniti") && bst.contains("Konigsegg") && bst.size() == 7;
  }

  /**
   * Test 3: Testing size() and clear() methods
   *
   * @return true if the program works as intended, false otherwise
   */
  public boolean test3() {
    BinarySearchTree<Integer> bst = new BinarySearchTree<>();
    bst.insert(1);
    bst.insert(2);
    bst.insert(3);
    bst.insert(4);
    bst.insert(5);

    if (bst.size() != 5) {
      return false;
    }

    bst.clear();

    if (bst.size() != 0) {
      return false;
    }

    return bst.isEmpty();
  }

  /**
   * Test 4: Testing on an empty BST
   *
   * @return true if the program works as intended, false otherwise
   */
  public boolean test4() {
    BinarySearchTree<Integer> bst = new BinarySearchTree<>();
    if (!bst.isEmpty()) {
      return false;
    }

    if (bst.size() != 0) {
      return false;
    }

    if (bst.contains(1)) {
      return false;
    }

    return true;
  }

  /**
   * Test 5: Test on a BST with a single node
   *
   * @return true if the program works as intended, false otherwise
   */
  public boolean test5() {
    BinarySearchTree<Integer> bst = new BinarySearchTree<>();
    bst.insert(1);

    if (bst.isEmpty()) {
      return false;
    }

    if (bst.size() == 0) {
      return false;
    }

    if (!bst.contains(1)) {
      return false;
    }

    return true;
  }

  /**
   * Test 6: Testing using duplicate values
   *
   * @return true if the program works as intended, false otherwise
   */
  public boolean test6() {
    BinarySearchTree<Integer> bst = new BinarySearchTree<>();
    bst.insert(2);
    bst.insert(2);
    return bst.size() == 2 && bst.contains(2);
  }

  /**
   * Test 7: Testing insert by inserting null value
   *
   * @return true if the program works as intended, false otherwise
   */
  public boolean test7() {
    BinarySearchTree<Integer> bst = new BinarySearchTree<>();

    try {
      bst.insert(null);
      return false;
    } catch (NullPointerException ignored) {
    }

    if (bst.size() != 0) {
      return false;
    }

    bst.insert(1);

    if (bst.size() != 1) {
      return false;
    }

    if (!bst.contains(1)) {
      return false;
    }

    return true;
  }

  /**
   * Main method to run the tests.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    BinarySearchTree<Integer> bst = new BinarySearchTree<>();

    System.out.println("Test 1: " + (bst.test1() ? "Test passed" : "Test failed"));
    System.out.println("Test 2: " + (bst.test2() ? "Test passed" : "Test failed"));
    System.out.println("Test 3: " + (bst.test3() ? "Test passed" : "Test failed"));
    System.out.println("Test 4: " + (bst.test4() ? "Test passed" : "Test failed"));
    System.out.println("Test 5: " + (bst.test5() ? "Test passed" : "Test failed"));
    System.out.println("Test 6: " + (bst.test6() ? "Test passed" : "Test failed"));
    System.out.println("Test 7: " + (bst.test7() ? "Test passed" : "Test failed"));
  }
}


