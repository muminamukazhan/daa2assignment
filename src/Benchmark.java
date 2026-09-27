import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class Benchmark {

    private static final int[] SIZES = {100, 1000, 10000, 100000};
    private static final int REPETITIONS = 5;
    private static final long SEED = 42;
    private static final String OUTPUT_DIR = "results/tables";

    public static void main(String[] args) throws IOException {
        Files.createDirectories(Paths.get(OUTPUT_DIR));
        runWorkload1();
        runWorkload2();
        runWorkload3();
        runWorkload4();
    }

    private static int[] randomArray(int n, Random random) {
        int[] values = new int[n];
        for (int i = 0; i < n; i++) {
            values[i] = random.nextInt();
        }
        return values;
    }

    private static void runWorkload1() throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(OUTPUT_DIR + "/workload1_random_access.csv"))) {
            out.println("structure,n,avg_time_ns,total_accesses");
            for (int n : SIZES) {
                Random dataRandom = new Random(SEED);
                int[] values = randomArray(n, dataRandom);
                Random indexRandom = new Random(SEED);
                int[] indices = new int[10000];
                for (int i = 0; i < indices.length; i++) {
                    indices[i] = indexRandom.nextInt(n);
                }

                long totalTimeArray = 0;
                long accessesArray = 0;
                for (int rep = 0; rep < REPETITIONS; rep++) {
                    DynamicArray<Integer> array = new DynamicArray<>();
                    for (int v : values) {
                        array.add(v);
                    }
                    DynamicArray.resetCounters();
                    long start = System.nanoTime();
                    for (int idx : indices) {
                        array.get(idx);
                    }
                    long end = System.nanoTime();
                    totalTimeArray += (end - start);
                    accessesArray = DynamicArray.accessCount;
                }

                long totalTimeList = 0;
                long accessesList = 0;
                for (int rep = 0; rep < REPETITIONS; rep++) {
                    LinkedList<Integer> list = new LinkedList<>();
                    for (int v : values) {
                        list.add(v);
                    }
                    LinkedList.resetCounters();
                    long start = System.nanoTime();
                    for (int idx : indices) {
                        list.get(idx);
                    }
                    long end = System.nanoTime();
                    totalTimeList += (end - start);
                    accessesList = LinkedList.accessCount;
                }

                out.println("DynamicArray," + n + "," + (totalTimeArray / REPETITIONS) + "," + accessesArray);
                out.println("LinkedList," + n + "," + (totalTimeList / REPETITIONS) + "," + accessesList);
            }
        }
    }

    private static void runWorkload2() throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(OUTPUT_DIR + "/workload2_search.csv"))) {
            out.println("structure,n,avg_time_ns,total_comparisons");
            for (int n : SIZES) {
                Random dataRandom = new Random(SEED);
                int[] values = randomArray(n, dataRandom);
                Random queryRandom = new Random(SEED);
                int[] queries = new int[1000];
                for (int i = 0; i < queries.length; i++) {
                    if (i % 2 == 0 && n > 0) {
                        queries[i] = values[queryRandom.nextInt(n)];
                    } else {
                        queries[i] = queryRandom.nextInt();
                    }
                }

                long totalTimeArray = 0;
                long comparisonsArray = 0;
                for (int rep = 0; rep < REPETITIONS; rep++) {
                    DynamicArray<Integer> array = new DynamicArray<>();
                    for (int v : values) {
                        array.add(v);
                    }
                    DynamicArray.resetCounters();
                    long start = System.nanoTime();
                    for (int q : queries) {
                        array.contains(q);
                    }
                    long end = System.nanoTime();
                    totalTimeArray += (end - start);
                    comparisonsArray = DynamicArray.comparisonCount;
                }

                long totalTimeList = 0;
                long comparisonsList = 0;
                for (int rep = 0; rep < REPETITIONS; rep++) {
                    LinkedList<Integer> list = new LinkedList<>();
                    for (int v : values) {
                        list.add(v);
                    }
                    LinkedList.resetCounters();
                    long start = System.nanoTime();
                    for (int q : queries) {
                        list.contains(q);
                    }
                    long end = System.nanoTime();
                    totalTimeList += (end - start);
                    comparisonsList = LinkedList.comparisonCount;
                }

                out.println("DynamicArray," + n + "," + (totalTimeArray / REPETITIONS) + "," + comparisonsArray);
                out.println("LinkedList," + n + "," + (totalTimeList / REPETITIONS) + "," + comparisonsList);
            }
        }
    }

    private static void runWorkload3() throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(OUTPUT_DIR + "/workload3_insert_remove.csv"))) {
            out.println("structure,n,position,operation,avg_time_ns,total_accesses");
            int operations = 1000;
            for (int n : SIZES) {
                Random dataRandom = new Random(SEED);
                int[] values = randomArray(n, dataRandom);
                int middle = n / 2;

                benchmarkInsertRemove(out, "DynamicArray", n, 0, operations, values, true);
                benchmarkInsertRemove(out, "DynamicArray", n, middle, operations, values, true);
                benchmarkInsertRemove(out, "LinkedList", n, 0, operations, values, false);
                benchmarkInsertRemove(out, "LinkedList", n, middle, operations, values, false);
            }
        }
    }

    private static void benchmarkInsertRemove(PrintWriter out, String structureName, int n, int position,
                                              int operations, int[] values, boolean isArray) {
        long totalInsertTime = 0;
        long insertAccesses = 0;
        for (int rep = 0; rep < REPETITIONS; rep++) {
            if (isArray) {
                DynamicArray<Integer> array = new DynamicArray<>();
                for (int v : values) {
                    array.add(v);
                }
                DynamicArray.resetCounters();
                long start = System.nanoTime();
                for (int i = 0; i < operations; i++) {
                    array.add(position, i);
                }
                long end = System.nanoTime();
                totalInsertTime += (end - start);
                insertAccesses = DynamicArray.accessCount;
            } else {
                LinkedList<Integer> list = new LinkedList<>();
                for (int v : values) {
                    list.add(v);
                }
                LinkedList.resetCounters();
                long start = System.nanoTime();
                for (int i = 0; i < operations; i++) {
                    list.add(position, i);
                }
                long end = System.nanoTime();
                totalInsertTime += (end - start);
                insertAccesses = LinkedList.accessCount;
            }
        }
        String positionLabel = (position == 0) ? "beginning" : "middle";
        out.println(structureName + "," + n + "," + positionLabel + ",insert," + (totalInsertTime / REPETITIONS) + "," + insertAccesses);

        long totalRemoveTime = 0;
        long removeAccesses = 0;
        for (int rep = 0; rep < REPETITIONS; rep++) {
            if (isArray) {
                DynamicArray<Integer> array = new DynamicArray<>();
                for (int v : values) {
                    array.add(v);
                }
                for (int i = 0; i < operations; i++) {
                    array.add(position, i);
                }
                DynamicArray.resetCounters();
                long start = System.nanoTime();
                for (int i = 0; i < operations; i++) {
                    array.remove(position);
                }
                long end = System.nanoTime();
                totalRemoveTime += (end - start);
                removeAccesses = DynamicArray.accessCount;
            } else {
                LinkedList<Integer> list = new LinkedList<>();
                for (int v : values) {
                    list.add(v);
                }
                for (int i = 0; i < operations; i++) {
                    list.add(position, i);
                }
                LinkedList.resetCounters();
                long start = System.nanoTime();
                for (int i = 0; i < operations; i++) {
                    list.remove(position);
                }
                long end = System.nanoTime();
                totalRemoveTime += (end - start);
                removeAccesses = LinkedList.accessCount;
            }
        }
        out.println(structureName + "," + n + "," + positionLabel + ",remove," + (totalRemoveTime / REPETITIONS) + "," + removeAccesses);
    }

    private static void runWorkload4() throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(OUTPUT_DIR + "/workload4_priority_processing.csv"))) {
            out.println("n,avg_insert_time_ns,avg_extract_time_ns,total_comparisons,non_decreasing_verified");
            for (int n : SIZES) {
                Random dataRandom = new Random(SEED);
                int[] values = randomArray(n, dataRandom);

                long totalInsertTime = 0;
                long totalExtractTime = 0;
                long comparisons = 0;
                boolean nonDecreasing = true;

                for (int rep = 0; rep < REPETITIONS; rep++) {
                    MinHeap heap = new MinHeap();
                    MinHeap.resetCounters();
                    long insertStart = System.nanoTime();
                    for (int v : values) {
                        heap.insert(v);
                    }
                    long insertEnd = System.nanoTime();
                    totalInsertTime += (insertEnd - insertStart);

                    long extractStart = System.nanoTime();
                    int previous = Integer.MIN_VALUE;
                    for (int i = 0; i < n; i++) {
                        int current = heap.extractMin();
                        if (current < previous) {
                            nonDecreasing = false;
                        }
                        previous = current;
                    }
                    long extractEnd = System.nanoTime();
                    totalExtractTime += (extractEnd - extractStart);
                    comparisons = MinHeap.comparisonCount;
                }

                out.println(n + "," + (totalInsertTime / REPETITIONS) + "," + (totalExtractTime / REPETITIONS)
                        + "," + comparisons + "," + nonDecreasing);
            }
        }
    }
}
