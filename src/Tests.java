import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;

public class Tests {

    private static int passed = 0;
    private static int failed = 0;

    private static void check(boolean condition, String description) {
        if (condition) {
            passed++;
        } else {
            failed++;
            System.out.println("FAILED: " + description);
        }
    }

    private static void expectException(Runnable action, String description) {
        try {
            action.run();
            failed++;
            System.out.println("FAILED (no exception): " + description);
        } catch (RuntimeException e) {
            passed++;
        }
    }

    public static void main(String[] args) {
        testDynamicArray();
        testLinkedList();
        testMinHeap();
        testAgainstJavaCollections();

        System.out.println();
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void testDynamicArray() {
        DynamicArray<Integer> empty = new DynamicArray<>();
        check(empty.isEmpty(), "DynamicArray empty on construction");
        check(empty.size() == 0, "DynamicArray size 0 on construction");
        check(!empty.contains(1), "DynamicArray empty contains() is false");
        expectException(() -> empty.get(0), "DynamicArray get(0) on empty throws");
        expectException(() -> empty.remove(0), "DynamicArray remove(0) on empty throws");

        DynamicArray<Integer> one = new DynamicArray<>();
        one.add(42);
        check(one.size() == 1, "DynamicArray size 1 after single add");
        check(one.get(0) == 42, "DynamicArray get(0) after single add");
        check(one.contains(42), "DynamicArray contains element after single add");
        check(!one.contains(7), "DynamicArray contains false for missing element");

        DynamicArray<Integer> many = new DynamicArray<>();
        for (int i = 0; i < 20; i++) {
            many.add(i);
        }
        check(many.size() == 20, "DynamicArray size 20 after 20 adds");
        for (int i = 0; i < 20; i++) {
            check(many.get(i) == i, "DynamicArray get(" + i + ") after sequential adds");
        }

        DynamicArray<Integer> duplicates = new DynamicArray<>();
        duplicates.add(5);
        duplicates.add(5);
        duplicates.add(5);
        check(duplicates.size() == 3, "DynamicArray size after duplicate adds");
        check(duplicates.contains(5), "DynamicArray contains works with duplicates");

        DynamicArray<Integer> boundary = new DynamicArray<>();
        boundary.add(0);
        boundary.add(1);
        boundary.add(2);
        boundary.add(0, -1);
        check(boundary.get(0) == -1, "DynamicArray add(0,x) shifts elements right");
        check(boundary.get(3) == 2, "DynamicArray last element preserved after add at front");
        boundary.add(boundary.size(), 99);
        check(boundary.get(boundary.size() - 1) == 99, "DynamicArray add at size appends");
        expectException(() -> boundary.add(-1, 0), "DynamicArray add(-1,x) throws");
        expectException(() -> boundary.add(boundary.size() + 1, 0), "DynamicArray add beyond size throws");
        expectException(() -> boundary.get(-1), "DynamicArray get(-1) throws");
        expectException(() -> boundary.get(boundary.size()), "DynamicArray get(size) throws");
        expectException(() -> boundary.remove(-1), "DynamicArray remove(-1) throws");
        expectException(() -> boundary.remove(boundary.size()), "DynamicArray remove(size) throws");

        int removed = boundary.remove(0);
        check(removed == -1, "DynamicArray remove(0) returns removed element");
        check(boundary.get(0) == 0, "DynamicArray remove(0) shifts elements left");

        DynamicArray<Integer> large = new DynamicArray<>();
        Random rnd = new Random(42);
        for (int i = 0; i < 100000; i++) {
            large.add(rnd.nextInt());
        }
        check(large.size() == 100000, "DynamicArray handles large input size");
    }

    private static void testLinkedList() {
        LinkedList<Integer> empty = new LinkedList<>();
        check(empty.isEmpty(), "LinkedList empty on construction");
        check(empty.size() == 0, "LinkedList size 0 on construction");
        check(!empty.contains(1), "LinkedList empty contains() is false");
        expectException(() -> empty.get(0), "LinkedList get(0) on empty throws");
        expectException(() -> empty.remove(0), "LinkedList remove(0) on empty throws");

        LinkedList<Integer> one = new LinkedList<>();
        one.add(42);
        check(one.size() == 1, "LinkedList size 1 after single add");
        check(one.get(0) == 42, "LinkedList get(0) after single add");
        check(one.contains(42), "LinkedList contains element after single add");

        LinkedList<Integer> many = new LinkedList<>();
        for (int i = 0; i < 20; i++) {
            many.add(i);
        }
        check(many.size() == 20, "LinkedList size 20 after 20 adds");
        for (int i = 0; i < 20; i++) {
            check(many.get(i) == i, "LinkedList get(" + i + ") after sequential adds");
        }

        LinkedList<Integer> duplicates = new LinkedList<>();
        duplicates.add(5);
        duplicates.add(5);
        duplicates.add(5);
        check(duplicates.size() == 3, "LinkedList size after duplicate adds");
        check(duplicates.contains(5), "LinkedList contains works with duplicates");

        LinkedList<Integer> boundary = new LinkedList<>();
        boundary.add(0);
        boundary.add(1);
        boundary.add(2);
        boundary.add(0, -1);
        check(boundary.get(0) == -1, "LinkedList add(0,x) inserts at head");
        check(boundary.get(3) == 2, "LinkedList tail preserved after add at front");
        boundary.add(boundary.size(), 99);
        check(boundary.get(boundary.size() - 1) == 99, "LinkedList add at size appends at tail");
        expectException(() -> boundary.add(-1, 0), "LinkedList add(-1,x) throws");
        expectException(() -> boundary.add(boundary.size() + 1, 0), "LinkedList add beyond size throws");
        expectException(() -> boundary.get(-1), "LinkedList get(-1) throws");
        expectException(() -> boundary.get(boundary.size()), "LinkedList get(size) throws");
        expectException(() -> boundary.remove(-1), "LinkedList remove(-1) throws");
        expectException(() -> boundary.remove(boundary.size()), "LinkedList remove(size) throws");

        int removed = boundary.remove(0);
        check(removed == -1, "LinkedList remove(0) returns removed element");
        check(boundary.get(0) == 0, "LinkedList remove(0) advances head");

        LinkedList<Integer> tailCheck = new LinkedList<>();
        tailCheck.add(1);
        tailCheck.add(2);
        tailCheck.remove(1);
        tailCheck.add(3);
        check(tailCheck.get(1) == 3, "LinkedList tail pointer stays correct after remove of last node");

        LinkedList<Integer> large = new LinkedList<>();
        Random rnd = new Random(42);
        for (int i = 0; i < 100000; i++) {
            large.add(rnd.nextInt());
        }
        check(large.size() == 100000, "LinkedList handles large input size");
    }

    private static void testMinHeap() {
        MinHeap empty = new MinHeap();
        check(empty.isEmpty(), "MinHeap empty on construction");
        expectException(empty::peekMin, "MinHeap peekMin on empty throws");
        expectException(empty::extractMin, "MinHeap extractMin on empty throws");

        MinHeap one = new MinHeap();
        one.insert(7);
        check(one.peekMin() == 7, "MinHeap peekMin after single insert");
        check(one.extractMin() == 7, "MinHeap extractMin after single insert");
        check(one.isEmpty(), "MinHeap empty after extracting only element");

        MinHeap duplicates = new MinHeap();
        duplicates.insert(3);
        duplicates.insert(3);
        duplicates.insert(3);
        check(duplicates.isValidHeap(), "MinHeap valid with duplicate keys");
        check(duplicates.extractMin() == 3, "MinHeap extractMin with duplicates");

        MinHeap many = new MinHeap();
        Random rnd = new Random(42);
        int n = 2000;
        int[] values = new int[n];
        for (int i = 0; i < n; i++) {
            values[i] = rnd.nextInt(100000);
            many.insert(values[i]);
            check(many.isValidHeap(), "MinHeap property holds after insertion #" + i);
        }
        java.util.Arrays.sort(values);
        boolean nonDecreasing = true;
        for (int i = 0; i < n; i++) {
            int extracted = many.extractMin();
            if (extracted != values[i]) {
                nonDecreasing = false;
            }
            if (!many.isValidHeap()) {
                check(false, "MinHeap property holds after extraction #" + i);
            }
        }
        check(nonDecreasing, "MinHeap extractMin returns non-decreasing sequence matching sorted input");
        check(many.isEmpty(), "MinHeap empty after extracting all elements");

        MinHeap large = new MinHeap();
        Random rnd2 = new Random(42);
        for (int i = 0; i < 100000; i++) {
            large.insert(rnd2.nextInt());
        }
        check(large.size() == 100000, "MinHeap handles large input size");
        check(large.isValidHeap(), "MinHeap valid after large number of insertions");
    }

    private static void testAgainstJavaCollections() {
        Random rnd = new Random(42);
        ArrayList<Integer> reference = new ArrayList<>();
        DynamicArray<Integer> candidate = new DynamicArray<>();
        for (int i = 0; i < 5000; i++) {
            int value = rnd.nextInt(10000);
            reference.add(value);
            candidate.add(value);
        }
        boolean matches = true;
        for (int i = 0; i < reference.size(); i++) {
            if (!reference.get(i).equals(candidate.get(i))) {
                matches = false;
            }
        }
        check(matches, "DynamicArray matches java.util.ArrayList element order");

        PriorityQueue<Integer> refHeap = new PriorityQueue<>();
        MinHeap candidateHeap = new MinHeap();
        Random rnd2 = new Random(42);
        for (int i = 0; i < 5000; i++) {
            int value = rnd2.nextInt(10000);
            refHeap.add(value);
            candidateHeap.insert(value);
        }
        boolean heapMatches = true;
        while (!refHeap.isEmpty()) {
            int expected = refHeap.poll();
            int actual = candidateHeap.extractMin();
            if (expected != actual) {
                heapMatches = false;
            }
        }
        check(heapMatches, "MinHeap extraction order matches java.util.PriorityQueue");
    }
}