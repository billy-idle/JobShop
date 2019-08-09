package com.github.guillesup.entities;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.List;
import java.util.Objects;

public class Problem {
    private final String id;
    private final Graph<Task, DefaultEdge> edgeGraph;
    private final int totalJobs;
    private final int totalMachines;
    private final int totalTasks;

    private Problem(String id, List<Integer> problemSettings,
                    Graph<Task, DefaultEdge> edgeGraph) {

        this.id = Objects.requireNonNull(id, "Problem id must not be null!");
        this.edgeGraph = Objects.requireNonNull(edgeGraph, "Problem graph must not be null!");
        Objects.requireNonNull(problemSettings, "Problem settings must not be null!");
        this.totalJobs = problemSettings.get(0);
        this.totalMachines = problemSettings.get(1);
        this.totalTasks = problemSettings.get(2);
        assesInput();
    }

    private void assesInput() {
        if (this.totalJobs < 0) {
            throw new ProblemException("Problem totalJobs must be greater than or equal to zero");
        } else if (this.totalMachines < 0) {
            throw new ProblemException("Problem totalMachines must be greater than or equal to zero");
        } else if (this.totalTasks < 0) {
            throw new ProblemException("Problem totalTasks must be greater than or equal to zero");
        }
    }

    public static Problem createProblem(String id, List<Integer> problemSettings,
                                        Graph<Task, DefaultEdge> edgeGraph) {

        return new Problem(id, problemSettings, edgeGraph);
    }

    public String getId() {
        return this.id;
    }

    public Graph<Task, DefaultEdge> getEdgeGraph() {
        return this.edgeGraph;
    }

    @Override
    public int hashCode() {
        return 31 *
                Objects.hashCode(this.id) +
                Objects.hashCode(this.totalJobs) +
                Objects.hashCode(this.totalMachines) +
                Objects.hashCode(this.totalTasks) +
                Objects.hashCode(this.edgeGraph);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        if (o == this)
            return true;

        if (!(o instanceof Problem))
            return false;

        var otherProblem = (Problem) o;

        return (this.id.equals(otherProblem.id) &&
                this.totalJobs == otherProblem.totalJobs &&
                this.totalMachines == otherProblem.totalMachines &&
                this.totalTasks == otherProblem.totalTasks);
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
}

class ProblemException extends RuntimeException {
    ProblemException(String message) {
        super(message);
    }
}