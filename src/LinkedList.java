public class LinkedList<T> {

    public static long accessCount = 0;
    public static long comparisonCount = 0;

    private static class Node<T> {
        T value;
        Node<T> next;

        Node(T value) {
            this.value = value;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public LinkedList() {
        head = null;
        tail = null;
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

    public void add(T x) {
        Node<T> node = new Node<>(x);
        accessCount++;
        if (tail == null) {
            head = node;
            tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        size++;
    }

    public void add(int index, T x) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (index == size) {
            add(x);
            return;
        }
        Node<T> node = new Node<>(x);
        if (index == 0) {
            node.next = head;
            head = node;
            if (tail == null) {
                tail = node;
            }
            accessCount++;
        } else {
            Node<T> prev = head;
            for (int i = 0; i < index - 1; i++) {
                prev = prev.next;
                accessCount++;
            }
            node.next = prev.next;
            prev.next = node;
            accessCount++;
        }
        size++;
    }

    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        T removed;
        if (index == 0) {
            removed = head.value;
            head = head.next;
            if (head == null) {
                tail = null;
            }
            accessCount++;
        } else {
            Node<T> prev = head;
            for (int i = 0; i < index - 1; i++) {
                prev = prev.next;
                accessCount++;
            }
            Node<T> target = prev.next;
            removed = target.value;
            prev.next = target.next;
            if (target == tail) {
                tail = prev;
            }
            accessCount++;
        }
        size--;
        return removed;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
            accessCount++;
        }
        accessCount++;
        return current.value;
    }

    public boolean contains(T x) {
        Node<T> current = head;
        while (current != null) {
            comparisonCount++;
            if (current.value == null ? x == null : current.value.equals(x)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }
}