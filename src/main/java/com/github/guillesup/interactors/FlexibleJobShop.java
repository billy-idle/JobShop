package com.github.guillesup.interactors;

import com.github.guillesup.entities.Benchmark;
import com.github.guillesup.entities.Task;
import org.jgrapht.Graphs;

import java.util.*;

public class FlexibleJobShop {
    private final Benchmark benchmark;

    private FlexibleJobShop(Benchmark benchmark) {
        this.benchmark = benchmark;
    }

    public static FlexibleJobShop getInstance(Benchmark benchmark) {
        return new FlexibleJobShop(benchmark);
    }

    public void schedule() {
        for (Benchmark bm : this.benchmark.getBenchmarkSet()) {

            int totalTasks = bm.getTotalTasks();
            int sqrOfTotalTasks = (int) Math.sqrt(totalTasks);
            var taskList = bm.getTaskList();
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

            scheduler(new ArrayList<>(), listOfTaskList.listIterator(), bm);

        }
    }

    private void scheduler(List<Task> previousTaskList, Iterator<List<Task>> listIterator, Benchmark benchmark) {
        Optional<Task> optionalTask;
        var nextTaskList = listIterator.next();

        for (var nextTask : nextTaskList) {
            optionalTask = nextTaskList.
                    stream().
                    filter(t -> t.getMachine().equals(nextTask.getMachine()) &&
                            t.getJob().compareTo(nextTask.getJob()) > 0).
                    reduce((first, second) -> second);

            if (optionalTask.isPresent()) {
                computeTime(optionalTask.get(), nextTask, benchmark);
            } else {
                optionalTask = previousTaskList.
                        stream().
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
                        stream().
                        findFirst();

        if (optionalTask.isPresent()) {
            var predecessorTask = optionalTask.get();

            int startTime = (previousTask.getEndTime() > predecessorTask.getEndTime()) ?
                    previousTask.getEndTime() : predecessorTask.getEndTime();

            int endTime = startTime + nextTask.getTime();

            nextTask.setEndTime(endTime);
            nextTask.setStartTime(startTime);

            benchmark.addDisjunctiveEdge(directedWeightedGraph.addEdge(previousTask, nextTask));
        }

    }

    private void computeTime(Task task, Benchmark benchmark) {
        var directedWeightedGraph = benchmark.getDirectedWeightedGraph();
        int startTime = 0;
        int endTime;

        var optionalTask =
                Graphs.predecessorListOf(directedWeightedGraph, task).
                        stream().
                        findFirst();

        if (optionalTask.isPresent()) {
            Task predecessor = optionalTask.get();
            startTime = predecessor.getEndTime();
            endTime = startTime + task.getTime();
            task.setEndTime(endTime);
            task.setStartTime(startTime);
        } else {
            endTime = startTime + task.getTime();
            task.setEndTime(endTime);
            task.setStartTime(startTime);
        }
    }

    public Benchmark getBenchmark() {
        return this.benchmark;
    }
}
