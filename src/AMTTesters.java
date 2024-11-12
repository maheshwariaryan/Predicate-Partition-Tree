import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class AMTTesters {

  @Test
  public void test1() {
    AMT tree = new AMT();
    tree.insert(2);  // even
    tree.insert(3);  // odd
    tree.insert(4);  // even
    tree.insert(7);  // odd

    System.out.println(tree.contains(2));  // true
    System.out.println(tree.contains(5));  // false
  }
}
