import java.util.*;
import java.time.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class Benchmark {
  private static final MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
  private static final int WARMUP_ITERATIONS = 5;
  private static final int TEST_ITERATIONS = 10;

  static class BenchmarkResult {
    double avgTimeNanos;
    long memoryUsedBytes;

    BenchmarkResult(double avgTimeNanos, long memoryUsedBytes) {
      this.avgTimeNanos = avgTimeNanos;
      this.memoryUsedBytes = memoryUsedBytes;
    }

    @Override
    public String toString() {
      return String.format("Average time: %.2f ms, Memory used: %.2f MB",
          avgTimeNanos / 1_000_000.0,
          memoryUsedBytes / (1024.0 * 1024.0));
    }
  }

  public static void main(String[] args) {
    // Test different dataset sizes
    int[] dataSizes = {1000, 10000, 100000, 1000000};

    for (int size : dataSizes) {
      System.out.println("\nTesting with dataset size: " + size);
      runBenchmark(size);
    }
  }

  private static void runBenchmark(int dataSize) {
    // Generate test data
    int[] insertData = generateRandomData(dataSize);
    int[] searchData = generateRandomData(dataSize / 10); // 10% of data size for search tests

    // Run insertion benchmarks
    System.out.println("\nInsertion Benchmark:");
    BenchmarkResult amtInsert = benchmarkInsertion(new AMT(), insertData, "AMT");
    BenchmarkResult dualInsert = benchmarkInsertion(new DualRBT(), insertData, "Dual RBT");

    // Run search benchmarks
    System.out.println("\nSearch Benchmark:");
    BenchmarkResult amtSearch = benchmarkSearch(new AMT(), insertData, searchData, "AMT");
    BenchmarkResult dualSearch = benchmarkSearch(new DualRBT(), insertData, searchData, "Dual RBT");

    // Print comparative results
    printComparison("Insertion", amtInsert, dualInsert);
    printComparison("Search", amtSearch, dualSearch);
  }

  private static BenchmarkResult benchmarkInsertion(Object tree, int[] data, String treeName) {
    // Warmup phase
    for (int i = 0; i < WARMUP_ITERATIONS; i++) {
      if (tree instanceof AMT) {
        AMT amtTree = (AMT) tree;
        for (int value : data) {
          amtTree.insert(value);
        }
      } else {
        DualRBT dualTree = (DualRBT) tree;
        for (int value : data) {
          dualTree.insert(value);
        }
      }
    }

    // Actual benchmark
    long totalTime = 0;
    long memoryBefore = getUsedMemory();

    for (int i = 0; i < TEST_ITERATIONS; i++) {
      Object freshTree = (tree instanceof AMT) ? new AMT() : new DualRBT();

      long startTime = System.nanoTime();

      if (freshTree instanceof AMT) {
        AMT amtTree = (AMT) freshTree;
        for (int value : data) {
          amtTree.insert(value);
        }
      } else {
        DualRBT dualTree = (DualRBT) freshTree;
        for (int value : data) {
          dualTree.insert(value);
        }
      }

      totalTime += System.nanoTime() - startTime;
    }

    long memoryAfter = getUsedMemory();
    double avgTime = totalTime / (double) TEST_ITERATIONS;

    System.out.printf("%s: %s%n", treeName,
        new BenchmarkResult(avgTime, memoryAfter - memoryBefore));

    return new BenchmarkResult(avgTime, memoryAfter - memoryBefore);
  }

  private static BenchmarkResult benchmarkSearch(Object tree, int[] insertData, int[] searchData, String treeName) {
    // First insert all data
    if (tree instanceof AMT) {
      AMT amtTree = (AMT) tree;
      for (int value : insertData) {
        amtTree.insert(value);
      }
    } else {
      DualRBT dualTree = (DualRBT) tree;
      for (int value : insertData) {
        dualTree.insert(value);
      }
    }

    // Warmup phase for search
    for (int i = 0; i < WARMUP_ITERATIONS; i++) {
      if (tree instanceof AMT) {
        AMT amtTree = (AMT) tree;
        for (int value : searchData) {
          amtTree.contains(value);
        }
      } else {
        DualRBT dualTree = (DualRBT) tree;
        for (int value : searchData) {
          dualTree.contains(value);
        }
      }
    }

    // Actual benchmark
    long totalTime = 0;
    long memoryBefore = getUsedMemory();

    for (int i = 0; i < TEST_ITERATIONS; i++) {
      long startTime = System.nanoTime();

      if (tree instanceof AMT) {
        AMT amtTree = (AMT) tree;
        for (int value : searchData) {
          amtTree.contains(value);
        }
      } else {
        DualRBT dualTree = (DualRBT) tree;
        for (int value : searchData) {
          dualTree.contains(value);
        }
      }

      totalTime += System.nanoTime() - startTime;
    }

    long memoryAfter = getUsedMemory();
    double avgTime = totalTime / (double) TEST_ITERATIONS;

    System.out.printf("%s: %s%n", treeName,
        new BenchmarkResult(avgTime, memoryAfter - memoryBefore));

    return new BenchmarkResult(avgTime, memoryAfter - memoryBefore);
  }

  private static int[] generateRandomData(int size) {
    Random random = new Random();
    int[] data = new int[size];
    for (int i = 0; i < size; i++) {
      data[i] = random.nextInt(1000000) + 1;
    }
    return data;
  }

  private static long getUsedMemory() {
    System.gc(); // Request garbage collection before measuring
    try {
      Thread.sleep(100); // Give GC time to run
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
    MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
    return heapUsage.getUsed();
  }

  private static void printComparison(String operation, BenchmarkResult parity, BenchmarkResult regular) {
    System.out.println("\n" + operation + " Comparison:");
    double timeSpeedup = (regular.avgTimeNanos - parity.avgTimeNanos) / regular.avgTimeNanos * 100;
    double memoryReduction = (regular.memoryUsedBytes - parity.memoryUsedBytes) /
        (double) regular.memoryUsedBytes * 100;

    System.out.printf("Time difference: %.2f%% (%s)%n",
        Math.abs(timeSpeedup),
        timeSpeedup > 0 ? "Parity faster" : "Regular faster");

    System.out.printf("Memory difference: %.2f%% (%s)%n",
        Math.abs(memoryReduction),
        memoryReduction > 0 ? "Parity more efficient" : "Regular more efficient");
  }
}
