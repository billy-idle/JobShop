package com.github.guillesup.interactors;

import com.github.guillesup.entities.Benchmark;
import com.github.guillesup.entities.Task;
import org.jgrapht.Graphs;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class represents a way to solve the Job-Shop problem.
 * This technique was taught by Paula Criollo.
 *
 * @author m.mcfly
 */
public class JobShop {
    private final Benchmark benchmark;

    private JobShop(Benchmark benchmark) {
        this.benchmark = benchmark;
    }

    /**
     * JobShop factory method.
     *
     * @param benchmark A benchmark object.
     * @return A new JobShop instance.
     */
    public static JobShop getInstance(Benchmark benchmark) {
        return new JobShop(benchmark);
    }

    /**
     * Solution
     */
    public void schedule() {
        int totalTasks = this.benchmark.getTotalTasks();
        int sqrOfTotalTasks = (int) Math.sqrt(totalTasks);
        var taskList = this.benchmark.getTaskList();
        Collections.sort(taskList);

        List<Task> snakeOrderTaskList = new ArrayList<>();

        for (int i = 0; i < sqrOfTotalTasks; i++) {
            for (int j = 0; j < totalTasks; j += sqrOfTotalTasks) {
                snakeOrderTaskList.add(taskList.get(i + j));
            }
        }

        List<List<Task>> listOfTaskList = new ArrayList<>();

        for (int i = 0; i < taskList.size(); i += sqrOfTotalTasks) {
            listOfTaskList.add(snakeOrderTaskList.subList(i, sqrOfTotalTasks + i));
        }

        for (var tl : listOfTaskList) {
            tl.sort(Collections.reverseOrder());
        }

        registerObservers(listOfTaskList);

        scheduler(new ArrayList<>(), listOfTaskList.listIterator(), this.benchmark);

        updateValues();
    }

    private void registerObservers(List<List<Task>> listOfTaskList) {
        List<Task> taskList = listOfTaskList.stream().flatMap(List::stream).collect(Collectors.toList());

        for (int i = 0; i < taskList.size() - 1; i++) {
            taskList.get(i).registerObserver(taskList.get(i + 1));
        }
    }

    private void updateValues() {
        var optionalTask =
                this.benchmark.getDirectedWeightedGraph().vertexSet().
                        stream().
                        filter(task -> task.getId() == 0).
                        findFirst();

        optionalTask.ifPresent(task -> task.notifyObservers(this.benchmark.getDirectedWeightedGraph()));

    }

    private void scheduler(List<Task> previousTaskList, Iterator<List<Task>> listIterator, Benchmark benchmark) {
        Optional<Task> optionalTask;
        var nextTaskList = listIterator.next();

        for (var nextTask : nextTaskList) {
            optionalTask =
                    nextTaskList.
                            parallelStream().
                            filter(t -> t.getMachine().equals(nextTask.getMachine()) &&
                                    t.getJob().compareTo(nextTask.getJob()) > 0).
                            reduce((first, second) -> second);

            if (optionalTask.isPresent()) {
                addDisjunctiveEdge(optionalTask.get(), nextTask, this.benchmark);
            } else {
                optionalTask =
                        previousTaskList.
                                parallelStream().
                                filter(t -> t.getMachine().equals(nextTask.getMachine())).
                                reduce((first, second) -> second);

                optionalTask.ifPresent(task -> addDisjunctiveEdge(task, nextTask, this.benchmark));
            }
        }

        if (listIterator.hasNext()) {
            scheduler(nextTaskList, listIterator, benchmark);
        }
    }

    private void addDisjunctiveEdge(Task previousTask, Task nextTask, Benchmark benchmark) {
        var directedWeightedGraph = benchmark.getDirectedWeightedGraph();

        benchmark.addDisjunctiveEdge(
                Graphs.addEdgeWithVertices(directedWeightedGraph, previousTask, nextTask,
                        previousTask.getTime()));
    }

    /**
     * @return benchmark object, with its disjunctive and directed edges.
     */
    public Benchmark getBenchmark() {
        return this.benchmark;
    }
}
