public class DynamicArray<T> {

    public static long accessCount = 0;
    public static long comparisonCount = 0;

    private Object[] data;
    private int size;

    public DynamicArray() {
        this(8);
    }

    public DynamicArray(int initialCapacity) {
        if (initialCapacity <= 0) {
            initialCapacity = 8;
        }
        data = new Object[initialCapacity];
        size = 0;
    }

    public static void resetCounters() {
        accessCount = 0;
        comparisonCount = 0;
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
        Object[] newData = new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
            accessCount++;
        }
        data = newData;
    }

    public void add(T x) {
        ensureCapacity(size + 1);
        data[size] = x;
        accessCount++;
        size++;
    }

    public void add(int index, T x) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        ensureCapacity(size + 1);
        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
            accessCount++;
        }
        data[index] = x;
        accessCount++;
        size++;
    }

    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        @SuppressWarnings("unchecked")
        T removed = (T) data[index];
        accessCount++;
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
            accessCount++;
        }
        data[size - 1] = null;
        size--;
        return removed;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        accessCount++;
        @SuppressWarnings("unchecked")
        T value = (T) data[index];
        return value;
    }

    public boolean contains(T x) {
        for (int i = 0; i < size; i++) {
            comparisonCount++;
            @SuppressWarnings("unchecked")
            T value = (T) data[i];
            if (value == null ? x == null : value.equals(x)) {
                return true;
            }
        }
        return false;
    }
}