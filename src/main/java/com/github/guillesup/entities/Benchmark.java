package com.github.guillesup.entities;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.*;

public class Benchmark {
    private final Set<Benchmark> benchmarkSet;
    private String id;
    private int totalJobs;
    private int totalMachines;
    private int totalTasks;
    private List<Task> taskList;
    private List<DefaultWeightedEdge> conjunctiveEdgesList;
    private List<DefaultWeightedEdge> disjunctiveEdgesList;
    private Graph<Task, DefaultWeightedEdge> directedWeightedGraph;

    {
        this.benchmarkSet = new HashSet<>();
    }

    private Benchmark(String id, List<Integer> problemSettings, List<Task> taskList) {

        this.id = Objects.requireNonNull(id, "Benchmark id must not be null!");
        Objects.requireNonNull(problemSettings, "Benchmark settings must not be null!");
        this.taskList = Objects.requireNonNull(taskList, "Benchmark task must not be null!");
        this.directedWeightedGraph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        this.disjunctiveEdgesList = new ArrayList<>();
        this.totalJobs = problemSettings.get(0);
        this.totalMachines = problemSettings.get(1);
        this.totalTasks = problemSettings.get(2);
        assessInput();
        populateDirectedWeightedGraph();
    }

    private void assessInput() {
        if (this.totalJobs < 0) {
            throw new ProblemException("Benchmark totalJobs must be greater than or equal to zero");
        } else if (this.totalMachines < 0) {
            throw new ProblemException("Benchmark totalMachines must be greater than or equal to zero");
        } else if (this.totalTasks < 0) {
            throw new ProblemException("Benchmark totalTasks must be greater than or equal to zero");
        }
    }

    private void populateDirectedWeightedGraph() {
        for (int i = 0; i < this.taskList.size() - 1; i++) {
            int index = i;
            Task sourceTask = this.taskList.get(index);
            Task targetTask = this.taskList.get(++index);

            if (sourceTask.getJob().equals(targetTask.getJob())) {
                Graphs.addEdgeWithVertices(directedWeightedGraph, sourceTask, targetTask, sourceTask.getTime());
            }
        }

        for (Task task : this.taskList) {
            if (Graphs.predecessorListOf(this.directedWeightedGraph, task).isEmpty()) {
                Graphs.addEdgeWithVertices(this.directedWeightedGraph, Task.getFictiveInitTask(), task,
                        Task.getFictiveInitTask().getTime());
            } else if (Graphs.successorListOf(this.directedWeightedGraph, task).isEmpty()) {
                Graphs.addEdgeWithVertices(this.directedWeightedGraph, task, Task.getFictiveEndTask(),
                        Task.getFictiveEndTask().getTime());
            }
        }

        this.conjunctiveEdgesList =
                new ArrayList<>(this.directedWeightedGraph.getAllEdges(Task.getFictiveInitTask(),
                        Task.getFictiveEndTask()));
    }

    private Benchmark() {

    }

    public static Benchmark getInstance() {
        return new Benchmark();
    }

    public List<DefaultWeightedEdge> getDisjunctiveEdgesList() {
        return this.disjunctiveEdgesList;
    }

    public void addDisjunctiveEdge(DefaultWeightedEdge disjunctiveEdge) {
        this.disjunctiveEdgesList.add(disjunctiveEdge);
    }

    public List<Benchmark> getBenchmarkSet() {
        return new ArrayList<>(benchmarkSet);
    }

    public void addBenchmark(String id, List<Integer> problemSettings, List<Task> taskList) {

        benchmarkSet.add(new Benchmark(id, problemSettings, taskList));
    }

    public Graph<Task, DefaultWeightedEdge> getDirectedWeightedGraph() {
        return this.directedWeightedGraph;
    }

    public String getId() {
        return this.id;
    }

    @Override
    public int hashCode() {
        return 117 +
                Objects.hashCode(this.id) +
                Objects.hashCode(this.totalJobs) +
                Objects.hashCode(this.totalMachines) +
                Objects.hashCode(this.totalTasks) +
                Objects.hashCode(this.directedWeightedGraph);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        if (o == this)
            return true;

        if (!(o instanceof Benchmark))
            return false;

        var otherBenchmark = (Benchmark) o;

        return this.id.equals(otherBenchmark.id);
    }

    public int getTotalJobs() {
        return this.totalJobs;
    }

    public int getTotalMachines() {
        return this.totalMachines;
    }

    public int getTotalTasks() {
        return this.totalTasks;
    }

    public List<Task> getTaskList() {
        return this.taskList;
    }

    public List<DefaultWeightedEdge> getConjunctiveEdgesList() {
        return this.conjunctiveEdgesList;
    }

}

class ProblemException extends RuntimeException {
    ProblemException(String message) {
        super(message);
    }
}