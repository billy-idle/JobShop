package com.github.guillesup.interactors;

import com.github.guillesup.entities.DummyTask;
import com.github.guillesup.entities.Problem;
import com.github.guillesup.entities.Task;
import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.io.ComponentNameProvider;
import org.jgrapht.io.DOTExporter;
import org.jgrapht.io.ExportException;
import org.jgrapht.io.GraphExporter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.*;

public class FlexibleSolution {
    private final List<Problem> problems;

    private FlexibleSolution(List<Problem> problems) {
        this.problems = problems;
    }

    public static void main(String[] args) throws IOException {
        setPath("problems\\Small & Testable");

        var fs = getInstance();
        fs.solve();

        setPath("problems");
    }

    private static void setPath(String path) throws IOException {
        final var props = new Properties();

        try (var in = new FileInputStream("config.properties")) {
            props.load(in);
        }

        props.setProperty("path", path);

        try (var out = new FileOutputStream("config.properties")) {
            props.store(out, "JobShop Properties");
        }
    }

    public static FlexibleSolution getInstance() {
        return new FlexibleSolution(ProblemLoader.getProblems());
    }

    public void solve() {
        for (var p : problems) {
            var edgeGraph = p.getEdgeGraph();
            solver(edgeGraph);
            System.out.println(p.getId());
            printPredecessorsOfEND(edgeGraph);
            System.out.println();
        }
    }

    private void solver(Graph<Task, DefaultEdge> edgeGraph) {
        var taskList = Graphs.successorListOf(edgeGraph, DummyTask.getInitDummyTask());
        taskList.forEach(t -> t.setEndTime(t.getTime()));
        taskList.sort(Collections.reverseOrder());

        traverseGraph(edgeGraph, taskList);

//        setEdgeToParallelPredecessorsByMachine(edgeGraph, taskList);
//        computeTimes(edgeGraph, taskList);
//        printGraph(edgeGraph);
//        printPredecessorsOfEND(edgeGraph);

    }

    private void setEdgeToParallelPredecessorsByMachine(Graph<Task, DefaultEdge> edgeGraph, List<Task> taskList) {

        for (var task : taskList) {
            taskList.forEach(
                    t -> {
                        if (!t.equals(task) && t.getMachine() == task.getMachine() && task.compareTo(t) > 0) {
                            setSuccessorTaskTimes(t, task);
                            edgeGraph.addEdge(task, t);
                        }
                    });
        }

        var successorTaskList = getSuccessorTaskList(edgeGraph, taskList);

        if (!successorTaskList.contains(DummyTask.getEndDummyTask())) {
            setEdgeToParallelPredecessorsByMachine(edgeGraph, successorTaskList);
        }
    }

    private void computeTimes(Graph<Task, DefaultEdge> edgeGraph, List<Task> taskList) {
        var successorTaskList = getSuccessorTaskList(edgeGraph, taskList);

        if (!successorTaskList.contains(DummyTask.getEndDummyTask())) {
            for (int i = 0; i < successorTaskList.size(); i++) {
                Task successorTask = successorTaskList.get(i);
                Task predecessorTask = taskList.get(i);

                Optional<Task> optionalParallelPredecessorTaskByMachine =
                        successorTaskList.
                                stream().
                                filter(t -> !t.equals(successorTask) &&
                                        t.getMachine() == successorTask.getMachine() &&
                                        successorTask.compareTo(t) < 0).
                                reduce((first, second) -> second);

                if (optionalParallelPredecessorTaskByMachine.isPresent()) {
                    Task parallelPredecessorTaskByMachine = optionalParallelPredecessorTaskByMachine.get();
                    setSuccessorTaskTimes(successorTask, parallelPredecessorTaskByMachine);
                }

                Task predecessorTaskByMachine =
                        taskList.
                                stream().
                                filter(t -> t.getMachine() == successorTask.getMachine()).
                                reduce((first, second) -> second).
                                orElse(predecessorTask);

                setSuccessorTaskTimes(successorTask, predecessorTaskByMachine);

                edgeGraph.addEdge(predecessorTask, predecessorTaskByMachine);
            }

            computeTimes(edgeGraph, successorTaskList);
        }
    }

    void setSuccessorTaskTimes(Task successorTask, Task predecessorTask) {
        int startTime = successorTask.getStartTime();
        int time = successorTask.getTime();

        successorTask.setStartTime(startTime + predecessorTask.getEndTime());
        successorTask.setEndTime(successorTask.getStartTime() + time);
    }

    private List<Task> getSuccessorTaskList(Graph<Task, DefaultEdge> edgeGraph, List<Task> taskList) {
        List<Task> successorTaskList = new ArrayList<>();

        for (var task : taskList) {
            Optional<Task> optionalTask =
                    Graphs.successorListOf(edgeGraph, task).
                            stream().
                            filter(t -> t.getMachine() != task.getMachine()).
                            findFirst();

            if (optionalTask.isPresent()) {
                Task successorTask = optionalTask.get();
                successorTaskList.add(successorTask);
            }
        }

        return successorTaskList;
    }

    private void exportToGraphviz(Graph<Task, DefaultEdge> edgeGraph) {

        // use helper classes to define how vertices should be rendered,
        // adhering to the DOT language restrictions
        ComponentNameProvider<Task> vertexIdProvider = task -> "" + task.getId();

        ComponentNameProvider<Task> vertexLabelProvider = Task::toString;

        GraphExporter<Task, DefaultEdge> exporter =
                new DOTExporter<>(vertexIdProvider, null, null);

        Writer writer = new StringWriter();

        try {
            exporter.exportGraph(edgeGraph, writer);
        } catch (ExportException e) {
            e.printStackTrace();
        }

        System.out.println(writer.toString());

    }

    private void printPredecessorsOfEND(Graph<Task, DefaultEdge> edgeGraph) {
        var taskList = Graphs.predecessorListOf(edgeGraph, DummyTask.getEndDummyTask());

        for (var t : taskList) {
            System.out.println("JobId: " + t.getJobId() + "\tStart: " + t.getStartTime()
                    + "\tEnd: " + t.getEndTime() + "\tTaskId: " + t.toString());
        }
    }

    private void drawGraph(Graph<Task, DefaultEdge> edgeGraph) {
        JGraphXAdapter<Task, DefaultEdge> graphAdapter = new JGraphXAdapter<>(edgeGraph);
        mxIGraphLayout layout = new mxCompactTreeLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);

        File imgFile = new File("graph.png");
        try {
            ImageIO.write(image, "PNG", imgFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void printGraph(Graph<Task, DefaultEdge> edgeGraph) {
        System.out.println(edgeGraph);

        var taskSet = edgeGraph.vertexSet();

        for (Task t : taskSet) {
            System.out.println("Id: " + t.toString() + " Start: " + t.getStartTime() + " End: " + t.getEndTime());
        }
    }

    private void traverseGraph(Graph<Task, DefaultEdge> edgeGraph, List<Task> taskList) {
        // In search for machine dependencies, runs once
        for (var formerTask : taskList) {
            if (edgeGraph.containsEdge(DummyTask.getInitDummyTask(), formerTask)) {
                for (var nextTask : taskList) {
                    if (!formerTask.equals(nextTask) &&
                            formerTask.getMachine() == nextTask.getMachine()) {

                        if (formerTask.compareTo(nextTask) > 0) {
                            nextTask.setStartTime(formerTask.getEndTime());
                            nextTask.setEndTime(nextTask.getStartTime() + nextTask.getTime());
                            edgeGraph.addEdge(formerTask, nextTask);
                        } else if (formerTask.compareTo(nextTask) == 0) {

                            if (nextTask.getStartTime() == 0) {
                                nextTask.setStartTime(formerTask.getEndTime());
                                nextTask.setEndTime(nextTask.getStartTime() + nextTask.getTime());
                                edgeGraph.addEdge(formerTask, nextTask);
                            }
                        }
                    }
                }
            }
        }

        List<Task> successorTaskList = new ArrayList<>();

        for (var task : taskList) {

            var successorOptionalTask = Graphs.successorListOf(edgeGraph, task).
                    stream().filter(t -> t.getMachine() != task.getMachine()).findFirst();

            if (successorOptionalTask.isPresent()) {
                var successorTask = successorOptionalTask.get();

                if (!successorTask.equals(DummyTask.getEndDummyTask())) {
                    successorTaskList.add(successorTask);

                    Task predecessorTaskByMachine =
                            successorTaskList.
                                    stream().
                                    filter(t -> !t.equals(successorTask) &&
                                            t.getMachine() == successorTask.getMachine()).
                                    reduce((first, second) -> second).
                                    orElse(taskList.
                                            stream().
                                            filter(t -> t.getMachine() == successorTask.getMachine()).
                                            reduce((first, second) -> second).
                                            orElse(task));

                    if (task.getEndTime() > predecessorTaskByMachine.getEndTime()) {
                        successorTask.setStartTime(task.getEndTime());
                    } else {
                        successorTask.setStartTime(predecessorTaskByMachine.getEndTime());
                    }

                    successorTask.setEndTime(successorTask.getTime() + successorTask.getStartTime());
                    edgeGraph.addEdge(predecessorTaskByMachine, successorTask);

                    if (successorTaskList.size() == taskList.size()) {
                        traverseGraph(edgeGraph, successorTaskList);
                    }
                } else {
                    break;
                }
            }
        }
    }
}
