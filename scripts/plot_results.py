import csv
import os
import matplotlib.pyplot as plt

BASE = os.path.join(os.path.dirname(__file__), "..", "results")
TABLES = os.path.join(BASE, "tables")
PLOTS = os.path.join(BASE, "plots")


def read_csv(name):
    path = os.path.join(TABLES, name)
    with open(path, newline="") as f:
        return list(csv.DictReader(f))


def plot_workload1():
    rows = read_csv("workload1_random_access.csv")
    ns = sorted(set(int(r["n"]) for r in rows))
    for metric, ylabel, filename in [
        ("avg_time_ns", "Average time (ns)", "workload1_time.png"),
        ("total_accesses", "Total element accesses", "workload1_accesses.png"),
    ]:
        plt.figure(figsize=(7, 5))
        for structure in ["DynamicArray", "LinkedList"]:
            values = [int(r[metric]) for r in rows if r["structure"] == structure]
            plt.plot(ns, values, marker="o", label=structure)
        plt.xscale("log")
        plt.yscale("log")
        plt.xlabel("n")
        plt.ylabel(ylabel)
        plt.title("Workload 1 - Random Access: " + ylabel + " vs n")
        plt.legend()
        plt.grid(True, which="both", alpha=0.3)
        plt.tight_layout()
        plt.savefig(os.path.join(PLOTS, filename))
        plt.close()


def plot_workload2():
    rows = read_csv("workload2_search.csv")
    ns = sorted(set(int(r["n"]) for r in rows))
    for metric, ylabel, filename in [
        ("avg_time_ns", "Average time (ns)", "workload2_time.png"),
        ("total_comparisons", "Total comparisons", "workload2_comparisons.png"),
    ]:
        plt.figure(figsize=(7, 5))
        for structure in ["DynamicArray", "LinkedList"]:
            values = [int(r[metric]) for r in rows if r["structure"] == structure]
            plt.plot(ns, values, marker="o", label=structure)
        plt.xscale("log")
        plt.yscale("log")
        plt.xlabel("n")
        plt.ylabel(ylabel)
        plt.title("Workload 2 - Search: " + ylabel + " vs n")
        plt.legend()
        plt.grid(True, which="both", alpha=0.3)
        plt.tight_layout()
        plt.savefig(os.path.join(PLOTS, filename))
        plt.close()


def plot_workload3():
    rows = read_csv("workload3_insert_remove.csv")
    ns = sorted(set(int(r["n"]) for r in rows))
    combos = [
        ("DynamicArray", "beginning", "insert"),
        ("DynamicArray", "middle", "insert"),
        ("LinkedList", "beginning", "insert"),
        ("LinkedList", "middle", "insert"),
    ]
    plt.figure(figsize=(7, 5))
    for structure, position, operation in combos:
        values = [
            int(r["avg_time_ns"]) for r in rows
            if r["structure"] == structure and r["position"] == position and r["operation"] == operation
        ]
        plt.plot(ns, values, marker="o", label=structure + " (" + position + ")")
    plt.xscale("log")
    plt.yscale("log")
    plt.xlabel("n")
    plt.ylabel("Average time (ns)")
    plt.title("Workload 3 - Insertion time vs n")
    plt.legend()
    plt.grid(True, which="both", alpha=0.3)
    plt.tight_layout()
    plt.savefig(os.path.join(PLOTS, "workload3_insert_time.png"))
    plt.close()

    combos_remove = [
        ("DynamicArray", "beginning", "remove"),
        ("DynamicArray", "middle", "remove"),
        ("LinkedList", "beginning", "remove"),
        ("LinkedList", "middle", "remove"),
    ]
    plt.figure(figsize=(7, 5))
    for structure, position, operation in combos_remove:
        values = [
            int(r["avg_time_ns"]) for r in rows
            if r["structure"] == structure and r["position"] == position and r["operation"] == operation
        ]
        plt.plot(ns, values, marker="o", label=structure + " (" + position + ")")
    plt.xscale("log")
    plt.yscale("log")
    plt.xlabel("n")
    plt.ylabel("Average time (ns)")
    plt.title("Workload 3 - Removal time vs n")
    plt.legend()
    plt.grid(True, which="both", alpha=0.3)
    plt.tight_layout()
    plt.savefig(os.path.join(PLOTS, "workload3_remove_time.png"))
    plt.close()

    plt.figure(figsize=(7, 5))
    for structure, position, operation in combos:
        values = [
            int(r["total_accesses"]) for r in rows
            if r["structure"] == structure and r["position"] == position and r["operation"] == operation
        ]
        plt.plot(ns, values, marker="o", label=structure + " (" + position + ")")
    plt.xscale("log")
    plt.yscale("log")
    plt.xlabel("n")
    plt.ylabel("Total element movements/accesses")
    plt.title("Workload 3 - Insertion accesses vs n")
    plt.legend()
    plt.grid(True, which="both", alpha=0.3)
    plt.tight_layout()
    plt.savefig(os.path.join(PLOTS, "workload3_insert_accesses.png"))
    plt.close()


def plot_workload4():
    rows = read_csv("workload4_priority_processing.csv")
    ns = [int(r["n"]) for r in rows]
    insert_times = [int(r["avg_insert_time_ns"]) for r in rows]
    extract_times = [int(r["avg_extract_time_ns"]) for r in rows]
    comparisons = [int(r["total_comparisons"]) for r in rows]

    plt.figure(figsize=(7, 5))
    plt.plot(ns, insert_times, marker="o", label="Total insert time (n insertions)")
    plt.plot(ns, extract_times, marker="o", label="Total extract time (n extractions)")
    plt.xscale("log")
    plt.yscale("log")
    plt.xlabel("n")
    plt.ylabel("Average time (ns)")
    plt.title("Workload 4 - Min-Heap time vs n")
    plt.legend()
    plt.grid(True, which="both", alpha=0.3)
    plt.tight_layout()
    plt.savefig(os.path.join(PLOTS, "workload4_time.png"))
    plt.close()

    plt.figure(figsize=(7, 5))
    plt.plot(ns, comparisons, marker="o", color="darkred", label="Total comparisons")
    plt.xscale("log")
    plt.yscale("log")
    plt.xlabel("n")
    plt.ylabel("Total comparisons")
    plt.title("Workload 4 - Min-Heap comparisons vs n")
    plt.legend()
    plt.grid(True, which="both", alpha=0.3)
    plt.tight_layout()
    plt.savefig(os.path.join(PLOTS, "workload4_comparisons.png"))
    plt.close()


def main():
    os.makedirs(PLOTS, exist_ok=True)
    plot_workload1()
    plot_workload2()
    plot_workload3()
    plot_workload4()


if __name__ == "__main__":
    main()