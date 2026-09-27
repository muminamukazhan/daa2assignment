import java.util.NoSuchElementException;

public class MinHeap {

    public static long comparisonCount = 0;
    public static long accessCount = 0;

    private int[] data;
    private int size;

    public MinHeap() {
        this(16);
    }

    public MinHeap(int initialCapacity) {
        if (initialCapacity <= 0) {
            initialCapacity = 16;
        }
        data = new int[initialCapacity];
        size = 0;
    }

    public static void resetCounters() {
        comparisonCount = 0;
        accessCount = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity <= data.length) {
            return;
        }
        int newCapacity = data.length;
        while (newCapacity < minCapacity) {
            newCapacity = newCapacity * 2;
        }
        int[] newData = new int[newCapacity];
        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
            accessCount++;
        }
        data = newData;
    }

    private int parent(int i) {
        return (i - 1) / 2;
    }

    private int left(int i) {
        return 2 * i + 1;
    }

    private int right(int i) {
        return 2 * i + 2;
    }

    private void swap(int i, int j) {
        int tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
        accessCount += 2;
    }

    public void insert(int x) {
        ensureCapacity(size + 1);
        data[size] = x;
        accessCount++;
        int i = size;
        size++;
        siftUp(i);
    }

    private void siftUp(int i) {
        while (i > 0) {
            int p = parent(i);
            comparisonCount++;
            if (data[p] <= data[i]) {
                break;
            }
            swap(i, p);
            i = p;
        }
    }

    public int peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        }
        accessCount++;
        return data[0];
    }

    public int extractMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        }
        int min = data[0];
        accessCount++;
        size--;
        data[0] = data[size];
        accessCount++;
        siftDown(0);
        return min;
    }

    private void siftDown(int i) {
        while (true) {
            int l = left(i);
            int r = right(i);
            int smallest = i;
            if (l < size) {
                comparisonCount++;
                if (data[l] < data[smallest]) {
                    smallest = l;
                }
            }
            if (r < size) {
                comparisonCount++;
                if (data[r] < data[smallest]) {
                    smallest = r;
                }
            }
            if (smallest == i) {
                break;
            }
            swap(i, smallest);
            i = smallest;
        }
    }

    public boolean isValidHeap() {
        for (int i = 1; i < size; i++) {
            if (data[parent(i)] > data[i]) {
                return false;
            }
        }
        return true;
    }
}