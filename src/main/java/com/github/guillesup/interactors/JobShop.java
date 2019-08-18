package com.github.guillesup.interactors;

import com.github.guillesup.entities.Benchmark;
import com.github.guillesup.entities.Task;
import org.jgrapht.Graphs;

import java.util.*;

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

        scheduler(new ArrayList<>(), listOfTaskList.listIterator(), this.benchmark);
    }

    private void scheduler(List<Task> previousTaskList, Iterator<List<Task>> listIterator, Benchmark benchmark) {
        Optional<Task> optionalTask;
        var nextTaskList = listIterator.next();

        for (var nextTask : nextTaskList) {
            optionalTask = nextTaskList.
                    parallelStream().
                    filter(t -> t.getMachine().equals(nextTask.getMachine()) &&
                            t.getJob().compareTo(nextTask.getJob()) > 0).
                    reduce((first, second) -> second);

            if (optionalTask.isPresent()) {
                computeTime(optionalTask.get(), nextTask, benchmark);
            } else {
                optionalTask = previousTaskList.
                        parallelStream().
                        filter(t -> t.getMachine().equals(nextTask.getMachine())).
                        reduce((first, second) -> second);

                if (optionalTask.isPresent()) {
                    computeTime(optionalTask.get(), nextTask, benchmark);
                } else {
                    computeTime(nextTask, benchmark);
                }
            }
        }

        if (listIterator.hasNext()) {
            scheduler(nextTaskList, listIterator, benchmark);
        }
    }

    private void computeTime(Task previousTask, Task nextTask, Benchmark benchmark) {
        var directedWeightedGraph = benchmark.getDirectedWeightedGraph();

        var optionalTask =
                Graphs.predecessorListOf(directedWeightedGraph, nextTask).
                        parallelStream().
                        findFirst();

        if (optionalTask.isPresent()) {
            var predecessorTask = optionalTask.get();

            int startTime = (previousTask.getEndTime() > predecessorTask.getEndTime()) ?
                    previousTask.getEndTime() : predecessorTask.getEndTime();

            int endTime = startTime + nextTask.getTime();

            nextTask.setEndTime(endTime);
            nextTask.setStartTime(startTime);

            benchmark.addDisjunctiveEdge(
                    Graphs.addEdgeWithVertices(directedWeightedGraph, previousTask, nextTask,
                            previousTask.getTime()));
        }
    }

    private void computeTime(Task task, Benchmark benchmark) {
        var directedWeightedGraph = benchmark.getDirectedWeightedGraph();

        var optionalTask =
                Graphs.predecessorListOf(directedWeightedGraph, task).
                        parallelStream().
                        findFirst();

        if (optionalTask.isPresent()) {
            Task predecessorTask = optionalTask.get();
            int startTime = predecessorTask.getEndTime();
            int endTime = startTime + task.getTime();
            task.setEndTime(endTime);
            task.setStartTime(startTime);
        }
    }

    /**
     * @return benchmark object, with its disjunctive and directed edges.
     */
    public Benchmark getBenchmark() {
        return this.benchmark;
    }
}
