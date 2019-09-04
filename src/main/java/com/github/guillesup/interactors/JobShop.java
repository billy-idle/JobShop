package com.github.guillesup.interactors;

import com.github.guillesup.entities.Benchmark;
import com.github.guillesup.entities.Task;
import com.github.guillesup.interactors.util.FileParser;
import org.jgrapht.Graphs;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This class represents a way to solve the Job-Shop problem.
 *
 * @author m.mcfly
 */
public class JobShop {
    private final Benchmark benchmark;

    private JobShop(Benchmark benchmark) {
        this.benchmark = benchmark;
    }

    public static Benchmark schedule(String filename) {
        var benchmark = FileParser.getInstance().getBenchmark(filename);
        new JobShop(benchmark).schedule();
        return benchmark;
    }

    /**
     * Solution
     */
    private void schedule() {
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

        listOfTaskList.forEach(t -> t.sort(Collections.reverseOrder()));

        taskList = listOfTaskList.stream().flatMap(List::stream).collect(Collectors.toList());

        registerObservers(taskList);

        scheduler(new ArrayList<>(), listOfTaskList.listIterator(), this.benchmark);

        updateValues(taskList.get(0));
    }

    private void registerObservers(List<Task> taskList) {
        IntStream.range(0, taskList.size() - 1).
                forEachOrdered(i -> taskList.get(i).registerObserver(taskList.get(i + 1)));
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

    private void updateValues(Task listFirstTask) {
        listFirstTask.update(this.benchmark.getDirectedWeightedGraph());
    }

}
