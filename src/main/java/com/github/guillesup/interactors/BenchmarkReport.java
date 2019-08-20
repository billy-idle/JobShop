package com.github.guillesup.interactors;

import com.github.guillesup.entities.Benchmark;
import com.github.guillesup.entities.Task;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.Collections;

/**
 *
 */
public class BenchmarkReport {
    private final Benchmark benchmark;
    private final Graph<Task, DefaultWeightedEdge> directedWeightedGraph;

    private BenchmarkReport(Benchmark benchmark) {
        this.benchmark = benchmark;
        this.directedWeightedGraph = this.benchmark.getDirectedWeightedGraph();
    }

    public static BenchmarkReport createReport(Benchmark benchmark) {
        return new BenchmarkReport(benchmark);
    }

    public String getReport() {
        return resume() + "\n" + criticalPathReport() + "\n" + endTimesReport();
    }

    private String resume() {
        var header = "Benchmark\n";
        var dashes = "---------\n";

        return dashes + header + dashes +
                String.format(" Id: %1s%n #Jobs: %2d%n #Machines: %3d%n #Tasks: %4d%n Is the graph acyclic: %5s%n",
                        this.benchmark.getId(), this.benchmark.getTotalJobs(),
                        this.benchmark.getTotalMachines(), this.benchmark.getTotalTasks(),
                        this.benchmark.isGraphAcyclic() ? "No" : "Yes");
    }

    private String criticalPathReport() {
        var output = new StringBuilder();
        var criticalPath = this.benchmark.getCriticalPath();
        var weight = criticalPath.getWeight();
        var length = criticalPath.getLength();
        var vertexList = criticalPath.getVertexList();

        var header = "Critical Path\n";
        var dashes = "-------------\n";
        output.append(dashes).append(header).append(dashes).
                append(String.format(" Weight: %1$d%n Length: %2$d%n Path: %3$s%n",
                        (int) weight, length, vertexList));

        return output.toString();
    }

    private String endTimesReport() {
        var endTasksList = Graphs.predecessorListOf(directedWeightedGraph, Task.getFictiveEndTask());
        endTasksList.sort(Collections.reverseOrder());

        var output = new StringBuilder();
        var header = "| Task (m:j) | End time | Due date |\n";
        var dashes = "+------------+----------+----------+ \n";
        output.append(dashes).append(header).append(dashes);

        for (var task : endTasksList) {
            output.append(
                    String.format("| %10s | %-8d | %-8d |%n", task.toString(), task.getEndTime(),
                            task.getJob().getDueDate()));
        }

        output.append(dashes);

        return output.toString();
    }
}
