##Assignment 2 — Algorithmic Analysis, Correctness and Performance Trade-offs

##Student: Mumina Mukazhan
##Group: SE-2530

Overview
This project implements and analyzes three fundamental data structures in Java:
Dynamic Array
Singly Linked List
Min-Heap
The goal is to compare their theoretical complexity with measured performance and explain how the internal organization of each structure affects practical execution time.
The project includes:

* implementations of the required data structures;
* correctness proofs using loop invariants;
* asymptotic complexity analysis;
* controlled performance experiments;
* automated tests;
* benchmark results and plots;
* discussion of theoretical and practical performance differences.

---

## Complexity Analysis

### Dynamic Array

| Operation       | Best |        Average | Worst |                      Auxiliary Space |
| --------------- | ---: | -------------: | ----: | -----------------------------------: |
| `add(x)`        | Θ(1) | Θ(1) amortized |  Θ(n) | O(1), O(n) temporarily during resize |
| `add(index, x)` | Θ(1) |           Θ(n) |  Θ(n) |                                 O(1) |
| `remove(index)` | Θ(1) |           Θ(n) |  Θ(n) |                                 O(1) |
| `get(index)`    | Θ(1) |           Θ(1) |  Θ(1) |                                 O(1) |
| `contains(x)`   | Θ(1) |           Θ(n) |  Θ(n) |                                 O(1) |

For `add(x)`, insertion is normally constant time, but when the internal array becomes full, a larger array must be allocated and elements copied. Therefore, the amortized complexity is Θ(1), while a resize operation takes Θ(n).

For `add(index, x)` and `remove(index)`, elements may need to be shifted using direct array indexing. The exact average complexity depends on the distribution of the selected index; for a typical random index it is Θ(n).

Because the array stores elements contiguously, indexed access is constant time and benefits from good cache locality.

### Singly Linked List

| Operation       | Best | Average | Worst | Auxiliary Space |
| --------------- | ---: | ------: | ----: | --------------: |
| `add(x)`        | Θ(1) |    Θ(1) |  Θ(1) |            O(1) |
| `add(index, x)` | Θ(1) |    Θ(n) |  Θ(n) |            O(1) |
| `remove(index)` | Θ(1) |    Θ(n) |  Θ(n) |            O(1) |
| `get(index)`    | Θ(1) |    Θ(n) |  Θ(n) |            O(1) |
| `contains(x)`   | Θ(1) |    Θ(n) |  Θ(n) |            O(1) |

A linked list does not require shifting elements after insertion or removal. However, accessing a non-first position requires traversing the list node by node.

The average Θ(n) complexity for index-based operations assumes a typical/random distribution of requested indices.

### Min-Heap

| Operation      | Best |  Average |    Worst | Auxiliary Space |
| -------------- | ---: | -------: | -------: | --------------: |
| `insert(x)`    | Θ(1) | Θ(log n) | Θ(log n) |            O(1) |
| `peekMin()`    | Θ(1) |     Θ(1) |     Θ(1) |            O(1) |
| `extractMin()` | Θ(1) | Θ(log n) | Θ(log n) |            O(1) |

`insert(x)` may require moving the new element upward to restore the heap property.

`peekMin()` directly accesses the minimum element at the root, so it always takes constant time.

`extractMin()` removes the root and restores the heap property using downward movement. Its worst-case complexity is Θ(log n).

### Practical Differences

Two operations can have the same asymptotic complexity but different real execution times because of:

* memory locality;
* object allocation;
* reference traversal;
* array copying;
* branch prediction;
* JVM/JIT optimizations;
* constant factors.

For example, both Dynamic Array and Linked List have linear-time search, but Dynamic Array can be faster in practice because its elements are stored contiguously in memory.

---

## Correctness

### Dynamic Array — `add(index, x)`

The operation inserts an element at a specified index and shifts existing elements one position to the right.

**Invariant:** Before each iteration of the shifting loop, all elements originally at positions greater than or equal to the current position have already been moved one position to the right, and no required element has been lost.

**Initialization:** Before the first iteration, the loop starts from the last existing element. No elements have been shifted yet, so the invariant holds.

**Maintenance:** During each iteration, the current element is copied to the position immediately to its right. Therefore, the already processed suffix remains correctly shifted and the invariant continues to hold.

**Termination:** The loop terminates when the insertion index is reached.

**Correctness:** At termination, every element originally at or after the insertion index has been shifted one position to the right. The new element can then be placed at the requested index, so the resulting array contains all original elements in the correct order plus the inserted element.

### Min-Heap — `siftDown`

`siftDown` restores the min-heap property after the root element has been replaced.

**Invariant:** Before each iteration, the subtree below the current position satisfies the min-heap property, except possibly at the current node.

**Initialization:** After replacing the minimum element with the last element, only the root may violate the heap property. Therefore, the invariant holds.

**Maintenance:** The current element is compared with its children. If necessary, it is swapped with the smaller child. The violation is then moved downward while the already processed part remains a valid min-heap.

**Termination:** The loop stops when the current element is no greater than both children or when it reaches a leaf.

**Correctness:** At termination, the current subtree satisfies the min-heap property. Since all other subtrees were already valid, the entire heap is restored.

---

## Experimental Setup

All benchmark results presented below were obtained from actual executions of `Benchmark.java`.

### Input Sizes

The following values of `n` were used:

```text
100
1,000
10,000
100,000
```

The workload was kept fixed while changing `n`.

### Benchmarking Rules

* Each experiment was repeated 5 times.
* The average execution time was recorded.
* `System.nanoTime()` was used for timing.
* Random values were generated using a fixed seed (`Random(42)`).
* Input data was generated before timing.
* Input generation and console output were excluded from timed sections.
* Data structures were restored between insertion/removal experiments when necessary.

### Workload 1 — Random Access

Dynamic Array and Linked List were tested.

For each `n`:

* `n` random integer values were generated.
* 10,000 random indices in `[0, n-1]` were generated.
* `get(index)` was called for every index.
* Total execution time and element accesses were recorded.

### Workload 2 — Search

Dynamic Array and Linked List were tested.

For each `n`:

* 1,000 search values were generated.
* `contains(x)` was executed for every search value.
* Execution time and comparisons were recorded.

### Workload 3 — Insertion and Removal

Dynamic Array and Linked List were tested at:

* index `0`;
* index `n / 2`.

For each position:

* 1,000 insertions were performed;
* insertion time was measured;
* 1,000 removals were performed;
* removal time was measured;
* movement/access counters were recorded.

The original state was restored between experiments.

### Workload 4 — Priority Processing

Min-Heap was tested.

For each `n`:

1. An empty heap was created.
2. `n` random integers were inserted.
3. Total insertion time was measured.
4. All `n` elements were extracted using `extractMin()`.
5. Extraction time and comparisons were recorded.
6. The extracted sequence was checked to be non-decreasing.

---

## Results

### Workload 1 — Random Access

|       n | Dynamic Array (ns) | Linked List (ns) |
| ------: | -----------------: | ---------------: |
|     100 |            315,933 |        1,699,508 |
|   1,000 |             67,475 |       11,879,650 |
|  10,000 |             63,233 |      129,475,683 |
| 100,000 |              9,841 |    1,367,599,216 |

The Dynamic Array performs indexed access in Θ(1), while Linked List access requires traversal and is Θ(n) on average.

The number of accesses for Dynamic Array remained fixed at 10,000, while Linked List required substantially more traversal work as `n` increased.

### Workload 2 — Search

|       n | Dynamic Array (ns) | Linked List (ns) | Comparisons |
| ------: | -----------------: | ---------------: | ----------: |
|     100 |            866,675 |          784,858 |      73,084 |
|   1,000 |          1,169,175 |        1,507,808 |     502,234 |
|  10,000 |          2,913,808 |        8,388,825 |   2,654,234 |
| 100,000 |         25,091,033 |       76,147,800 |  24,994,234 |

Both structures have linear worst-case search complexity. However, the measured execution times differ because of memory locality and implementation-level overhead.

### Workload 3 — Insertion and Removal at Index 0

|       n | DA Insert (ns) | DA Remove (ns) | LL Insert (ns) | LL Remove (ns) |
| ------: | -------------: | -------------: | -------------: | -------------: |
|     100 |      2,484,850 |      2,932,800 |        102,991 |         52,216 |
|   1,000 |      1,986,333 |      1,875,408 |         21,550 |          7,574 |
|  10,000 |     13,025,724 |     13,183,467 |          5,308 |          3,349 |
| 100,000 |    760,967,000 |    765,895,558 |         14,391 |          3,333 |

Insertion and removal at the beginning require shifting many elements in a Dynamic Array, resulting in Θ(n) work.

For a Linked List, insertion and removal at the beginning can be performed in Θ(1) when the head is directly available.

### Workload 3 — Insertion and Removal at the Middle

|       n | DA Insert (ns) | DA Remove (ns) | LL Insert (ns) | LL Remove (ns) |
| ------: | -------------: | -------------: | -------------: | -------------: |
|     100 |        810,150 |        772,408 |        364,258 |        195,466 |
|   1,000 |      1,294,608 |      1,267,316 |      1,222,733 |      1,201,849 |
|  10,000 |      7,060,116 |      6,866,583 |     13,380,816 |     12,940,400 |
| 100,000 |    382,129,892 |    388,911,466 |    131,097,291 |    130,839,575 |

Both structures require linear work for middle insertion/removal, but for different reasons:

* Dynamic Array shifts elements.
* Linked List must traverse the list to reach the required position.

### Workload 4 — Priority Processing

|       n | Insert Time (ns) | Extract Time (ns) | Comparisons | Non-decreasing |
| ------: | ---------------: | ----------------: | ----------: | :------------: |
|     100 |           51,800 |           111,525 |       1,069 |       Yes      |
|   1,000 |           68,858 |           188,458 |      17,322 |       Yes      |
|  10,000 |          652,741 |         1,335,441 |     239,284 |       Yes      |
| 100,000 |        2,430,975 |        14,858,650 |   3,059,283 |       Yes      |

The total cost of processing `n` heap elements grows approximately as expected for an `O(n log n)` workload.

The extracted values were non-decreasing for every tested input size, confirming that the heap maintained the required ordering property.

### Plots

The project includes plots showing:

* execution time vs. `n`;
* number of operations/comparisons/accesses vs. `n`.

---

## Discussion

### 1. How does increasing `n` affect performance?

Increasing `n` increases the amount of work performed by operations whose complexity depends on the input size.

The effect is especially visible for Linked List random access and Dynamic Array insertion/removal at the beginning.

### 2. Do the measured results agree with theoretical complexity?

In general, the measured results follow the expected trends.

Dynamic Array indexed access remains approximately constant, while Linked List indexed access grows substantially with `n`.

Dynamic Array insertion/removal at index 0 becomes increasingly expensive because elements must be shifted.

### 3. Why do Dynamic Array and Linked List behave differently?

The structures organize their elements differently.

A Dynamic Array stores elements in contiguous array positions, which allows direct indexing.

A Linked List stores elements in separate nodes connected by references, so accessing a position requires traversal.

### 4. Why can two operations with the same Big-O complexity have different execution times?

Big-O describes asymptotic growth and does not include all constant factors.

Actual execution can be affected by:

* memory locality;
* reference chasing;
* array copying;
* allocations;
* branches;
* JVM optimizations.

### 5. Why does implementation detail matter?

Two implementations with the same theoretical complexity can have different practical performance because their low-level operations are different.

For example, sequential access through a contiguous array is generally more cache-friendly than following references between separate nodes.

### 6. When is a Dynamic Array preferable?

Based on the tested workloads, Dynamic Array is suitable when fast indexed access is important and insertions/removals in the middle or beginning are relatively infrequent.

### 7. When is a Linked List useful?

Linked List is useful when frequent insertion or removal at the beginning is required and direct indexed access is not the main operation.

### 8. When is a Min-Heap suitable?

A Min-Heap is appropriate when the application repeatedly needs access to and removal of the smallest element.

It provides constant-time `peekMin()` and logarithmic-time insertion and extraction in the general case.

### 9. How does the workload influence the result?

Performance depends strongly on the operation being measured.

For example:

* random access favors Dynamic Array;
* insertion/removal at the beginning favors Linked List;
* priority processing is naturally suited to Min-Heap.

Therefore, the appropriate data structure depends on the dominant operations of the workload.

---

## Design Recommendations

The experiments demonstrate that data structure choice should depend on the required operations rather than only on the number of elements.

* Use **Dynamic Array** when fast indexed access is important.
* Use **Linked List** when frequent insertion/removal at the beginning is required.
* Use **Min-Heap** when repeatedly retrieving the minimum element is required.

The measured results demonstrate the practical consequences of the theoretical complexity of each structure.

---

## Testing

The implementation was tested for:

* empty structures;
* one-element structures;
* multiple elements;
* duplicate values;
* boundary indices;
* large input sizes;
* invalid indices;
* heap property after insertion;
* heap property after extraction;
* non-decreasing order of extracted heap elements.

The tests are executed using the project's `Tests` class.

---

## Reproduction

The project can be compiled and executed with:

```bash
mkdir out
javac -d out src/*.java
java -cp out Tests
java -cp out Benchmark
python3 scripts/plot_results.py
```

The benchmark uses the same input-generation rules described in the Experimental Setup section.

The results in this README correspond to actual benchmark executions rather than simulated results.

---

## Conclusion

The experiments demonstrate that theoretical complexity provides a useful prediction of how data structures behave as the input size grows, while actual execution time is also influenced by implementation details and hardware/runtime effects.

Dynamic Array provides constant-time indexed access and good memory locality, but insertion and removal can require shifting many elements.

Linked List avoids element shifting for operations at the beginning, but indexed access requires traversal.

Min-Heap provides efficient priority processing, with constant-time minimum access and logarithmic-time insertion and extraction in the general case.

The experimental results generally follow the theoretical complexity of the implemented operations and demonstrate why the choice of data structure should depend on the workload.
