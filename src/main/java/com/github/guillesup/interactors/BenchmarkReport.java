package com.github.guillesup.interactors;

import com.github.guillesup.entities.Benchmark;
import com.github.guillesup.entities.Task;
import com.github.guillesup.interactors.util.GraphExporter;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.Collections;

public class BenchmarkReport {
    private final Benchmark benchmark;
    private final Graph<Task, DefaultWeightedEdge> directedWeightedGraph;

    private BenchmarkReport(Benchmark benchmark) {
        this.benchmark = benchmark;
        this.directedWeightedGraph = this.benchmark.getDirectedWeightedGraph();
    }

    public static BenchmarkReport getInstance(Benchmark benchmark) {
        return new BenchmarkReport(benchmark);
    }

    public String getReport() {
        return resume() + "\n" + criticalPathReport() + "\n" + endTimesReport();
    }

    private String resume() {
        var header = "Benchmark\n";
        var dashes = "------------------------------------\n";

        return dashes + header + dashes +
                "\t\tId: " + this.benchmark.getId() + "\n" +
                "\t  Jobs: " + this.benchmark.getTotalJobs() + "\n" +
                "  Machines: " + this.benchmark.getTotalMachines() + "\n" +
                "\t Tasks: " + this.benchmark.getTotalTasks() + "\n" +
                "Is acyclic: " + (this.benchmark.isGraphAcyclic() ? "Yes" : "No") + "\n";
    }

    private String criticalPathReport() {
        var cp = this.benchmark.getCriticalPath();

        var header = "Critical Path\n";
        var dashes = "------------------------------------\n";

        return dashes + header + dashes +
                "Weight: " + (int) cp.getWeight() + "\n" +
                "Length: " + cp.getLength() + "\n" +
                "  Path: " + this.benchmark.getMappedCriticalPath() + "\n";
    }

    private String endTimesReport() {
        var endTasksList = Graphs.predecessorListOf(this.directedWeightedGraph, Task.getFictiveEndTask());
        endTasksList.sort(Collections.reverseOrder());

        var output = new StringBuilder();
        var header = "| Task (m:j) | End time | Due date |\n";
        var dashes = "+------------+----------+----------+ \n";
        output.append(dashes).append(header).append(dashes);

        for (var task : endTasksList) {
            output.append(
                    String.format("| %10s | %-8d | %-8d |%n",
                            task.toJobShopNotation(),
                            task.getEndTime(),
                            task.getJob().getDueDate()));
        }

        output.append(dashes);

        return output.toString();
    }

    public String toDot() {
        return GraphExporter.getInstance(this.benchmark).toDot();
    }

    public void saveDotToDisk() {
        GraphExporter.getInstance(this.benchmark).saveDotToDisk();
    }
}
