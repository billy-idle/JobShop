package com.github.guillesup.interactors;

import com.github.guillesup.entities.DummyTask;
import com.github.guillesup.entities.Job;
import com.github.guillesup.entities.Problem;
import com.github.guillesup.entities.Task;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

class FileParser {
    private final List<Problem> problems;
    private final List<Path> pathList;
    private Path path;

    private FileParser() {
        this.problems = new ArrayList<>();
        this.pathList = new ArrayList<>();

        setPath();
        seekFiles();
        filesToProblems();
    }

    private void setPath() {
        final var props = new Properties();

        try (var in = new FileInputStream("config.properties")) {
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.path = Paths.get(props.getProperty("path"));
    }

    private void seekFiles() {
        try (var pathStream = Files.walk(this.path).
                filter(p -> p.getFileName().toString().endsWith(".txt"))) {

            pathStream.forEach(this.pathList::add);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void filesToProblems() {
        for (var p : this.pathList) {
            this.problems.add(parseFile(p));
        }
    }

    private Problem parseFile(Path path) {
        String problemId = null;
        List<Integer> problemSettings = new ArrayList<>();
        Graph<Task, DefaultEdge> edgeGraph = new DefaultDirectedGraph<>(DefaultEdge.class);

        try {
            final var allLines = Files.readAllLines(path);

            problemId = path.toString();
            problemSettings = getProblemSettings(allLines);
            edgeGraph = createDirectedGraph(allLines);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Problem.createProblem(problemId, problemSettings, edgeGraph);
    }

    private List<Integer> getProblemSettings(List<String> allLines) {
        try (var scanner = new Scanner(allLines.get(0))) {

            List<Integer> problemSettings = new ArrayList<>();

            while (scanner.hasNext()) {
                problemSettings.add(Integer.parseInt(scanner.next())); // #Jobs
                problemSettings.add(Integer.parseInt(scanner.next())); // #Machines
                problemSettings.add(Integer.parseInt(scanner.next())); // #Tasks
            }

            return problemSettings;
        }
    }

    private Graph<Task, DefaultEdge> createDirectedGraph(List<String> allLines) {

        Graph<Task, DefaultEdge> edgeGraph = new DefaultDirectedGraph<>(DefaultEdge.class);

        var initTask = DummyTask.getInitDummyTask();
        var endTask = DummyTask.getEndDummyTask();

        int jobId = 1;

        for (int i = 1; i < allLines.size(); i++) {
            String line = allLines.get(i);
            addTasksToGraph(jobId++, line, edgeGraph, initTask, endTask);
        }

        return edgeGraph;
    }

    private void addTasksToGraph(int jobId, String line,
                                 Graph<Task, DefaultEdge> edgeGraph, Task initTask,
                                 Task endTask) {

        List<Task> tasks = new ArrayList<>();
        int taskCounter = 0;
        String taskId;

        tasks.add(initTask);

        try (var scanner = new Scanner(line)) {
            while (scanner.hasNext()) {
                int startTime = Integer.parseInt(scanner.next());
                int dueDate = Integer.parseInt(scanner.next());
                double weight = Double.parseDouble(scanner.next());
                int numberOfTasks = Integer.parseInt(scanner.next());

                Job job = Job.createJob(jobId, startTime, dueDate, weight, numberOfTasks);

                while (scanner.hasNext()) {
                    int machineId = createMachine(Integer.parseInt(scanner.next()), tasks);
                    int time = Integer.parseInt(scanner.next());
                    ++taskCounter;
                    taskId = "" + jobId + taskCounter;
                    tasks.add(Task.createTask(Integer.parseInt(taskId), machineId, time, job));
                }
            }
        }

        tasks.add(endTask);
        int tasksIndex;

        for (int i = 0; i < tasks.size() - 1; i++) {
            tasksIndex = i;
            Graphs.addEdgeWithVertices(edgeGraph, tasks.get(tasksIndex), tasks.get(++tasksIndex));
        }
    }

    private int createMachine(int machine, List<Task> tasks) {
        if (!tasks.isEmpty()) {
            for (var task : tasks) {
                if (machine == task.getMachine()) {
                    return task.getMachine();
                }
            }
        }

        return machine;
    }

    public static FileParser getInstance() {
        return new FileParser();
    }

    public List<Problem> getProblems() {
        return this.problems;
    }
}
