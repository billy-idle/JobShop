package com.github.guillesup.entities;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class represents a benchmark.
 *
 * @author m.mcfly
 */
public class Benchmark {
    private final String id;
    private final int totalJobs;
    private final int totalMachines;
    private final int totalTasks;
    private final List<Task> taskList;
    private final List<DefaultWeightedEdge> conjunctiveEdgesList;
    private final List<DefaultWeightedEdge> disjunctiveEdgesList;
    private final Graph<Task, DefaultWeightedEdge> directedWeightedGraph;

    private Benchmark(String id, int totalJobs, int totalMachines, int totalTasks, List<Task> taskList) {
        this.id = Objects.requireNonNull(id, "Benchmark id must not be null!");
        this.totalJobs = totalJobs;
        this.totalMachines = totalMachines;
        this.totalTasks = totalTasks;
        this.taskList = Objects.requireNonNull(taskList, "Benchmark task-list must not be null!");
        this.conjunctiveEdgesList = new ArrayList<>();
        this.disjunctiveEdgesList = new ArrayList<>();
        this.directedWeightedGraph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        assessInput();
        populateDirectedWeightedGraph();
    }

    private void assessInput() {
        if (this.totalJobs < 0) {
            throw new BenchmarkException("Benchmark totalJobs must be greater than or equal to zero");
        } else if (this.totalMachines < 0) {
            throw new BenchmarkException("Benchmark totalMachines must be greater than or equal to zero");
        } else if (this.totalTasks < 0) {
            throw new BenchmarkException("Benchmark totalTasks must be greater than or equal to zero");
        }
    }

    private void populateDirectedWeightedGraph() {
        for (int i = 0; i < this.taskList.size() - 1; i++) {
            int index = i;
            Task sourceTask = this.taskList.get(index);
            Task targetTask = this.taskList.get(++index);

            if (sourceTask.getJob().equals(targetTask.getJob())) {
                this.conjunctiveEdgesList.add(
                        Graphs.addEdgeWithVertices(directedWeightedGraph, sourceTask, targetTask, sourceTask.getTime()));
            }
        }

        for (Task task : this.taskList) {
            if (Graphs.predecessorListOf(this.directedWeightedGraph, task).isEmpty()) {
                this.conjunctiveEdgesList.add(Graphs.addEdgeWithVertices(this.directedWeightedGraph,
                        Task.getFictiveInitTask(), task, Task.getFictiveInitTask().getTime()));
            } else if (Graphs.successorListOf(this.directedWeightedGraph, task).isEmpty()) {
                this.conjunctiveEdgesList.add(Graphs.addEdgeWithVertices(this.directedWeightedGraph, task,
                        Task.getFictiveEndTask(), task.getTime()));
            }
        }
    }

    /**
     * Benchmark factory method.
     *
     * @param id            A string, usually the relative path of the benchmark txt file.
     * @param totalJobs     int >= 0
     * @param totalMachines int >= 0
     * @param totalTasks    int >= 0
     * @param taskList      ArrayList of tasks (vertices).
     * @return a new Benchmark instance.
     */
    public static Benchmark createBenchmark(String id, int totalJobs, int totalMachines, int totalTasks, List<Task> taskList) {
        return new Benchmark(id, totalJobs, totalMachines, totalTasks, taskList);
    }

    /**
     * @return ArrayList of the disjunctive edges.
     */
    public List<DefaultWeightedEdge> getDisjunctiveEdgesList() {
        return this.disjunctiveEdgesList;
    }

    /**
     * @param disjunctiveEdge Disjunctive edge between tasks with the same machine.
     */
    public void addDisjunctiveEdge(DefaultWeightedEdge disjunctiveEdge) {
        this.disjunctiveEdgesList.add(disjunctiveEdge);
    }

    /**
     * @return The directed and weighted graph.
     */
    public Graph<Task, DefaultWeightedEdge> getDirectedWeightedGraph() {
        return this.directedWeightedGraph;
    }

    /**
     * @return Benchmark's id, usually the relative path of the txt file.
     */
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

    /**
     * @return Total number of jobs.
     */
    public int getTotalJobs() {
        return this.totalJobs;
    }

    /**
     * @return Total number of machines.
     */
    public int getTotalMachines() {
        return this.totalMachines;
    }

    /**
     * @return Total number of tasks.
     */
    public int getTotalTasks() {
        return this.totalTasks;
    }

    /**
     * @return ArrayList of tasks (vertices).
     */
    public List<Task> getTaskList() {
        return this.taskList;
    }

    /**
     * @return ArrayList of conjunctive edges.
     */
    public List<DefaultWeightedEdge> getConjunctiveEdgesList() {
        return this.conjunctiveEdgesList;
    }

    /**
     * @return GraphPath that describes the critical path.
     */
    public GraphPath<Task, DefaultWeightedEdge> getCriticalPath() {
        var allDirectedPaths = new AllDirectedPaths<>(this.directedWeightedGraph);

        List<GraphPath<Task, DefaultWeightedEdge>> graphPathList;
        graphPathList = allDirectedPaths.getAllPaths(Task.getFictiveInitTask(), Task.getFictiveEndTask(), true, null);

        var makespan = getMakespan();
        var optionalVertexList = graphPathList.parallelStream().filter(t -> t.getWeight() == makespan).findFirst();

        return optionalVertexList.orElseGet(optionalVertexList::get);
    }


    /**
     * @return The length of the critical path.
     */
    public double getMakespan() {
        double makespan = 0.0;
        var allDirectedPaths = new AllDirectedPaths<>(this.directedWeightedGraph);

        var graphPathList =
                allDirectedPaths.getAllPaths(Task.getFictiveInitTask(), Task.getFictiveEndTask(), true, null);

        var optionalMakespan = graphPathList.parallelStream().map(GraphPath::getWeight).max(Double::compareTo);

        if (optionalMakespan.isPresent()) {
            makespan = optionalMakespan.get();
        }

        return makespan;
    }

    /**
     * @return True is the directed and weighted graph is acyclic.
     */
    public boolean isGraphAcyclic() {
        var cycleDetector = new CycleDetector<>(this.directedWeightedGraph);
        return cycleDetector.detectCycles();
    }
}

class BenchmarkException extends RuntimeException {
    BenchmarkException(String message) {
        super(message);
    }
}