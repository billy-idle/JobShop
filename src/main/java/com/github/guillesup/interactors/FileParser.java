package com.github.guillesup.interactors;

import com.github.guillesup.entities.Benchmark;
import com.github.guillesup.entities.Job;
import com.github.guillesup.entities.Machine;
import com.github.guillesup.entities.Task;

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
    private final List<Path> pathList;
    private Path path;
    private final Benchmark benchmark;

    {
        this.pathList = new ArrayList<>();
        this.benchmark = Benchmark.getInstance();
    }

    private FileParser() {
        setPath();
        seekFiles();
        txtToBenchmark();
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

    private void txtToBenchmark() {
        for (var path : this.pathList) {
            parseFile(path);
        }
    }

    private void parseFile(Path path) {
        String problemId = null;
        List<Integer> problemSettings = new ArrayList<>();
        List<Task> taskList = new ArrayList<>();

        try {
            final var allLines = Files.readAllLines(path);

            problemId = path.toString();
            problemSettings = getProblemSettings(allLines);
            taskList = parseTasks(allLines);

        } catch (IOException e) {
            e.printStackTrace();
        }

        benchmark.addBenchmark(problemId, problemSettings, taskList);
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

    private List<Task> parseTasks(List<String> allLines) {
        Task task = Task.getInstance();
        Machine machine = Machine.getInstance();
        Job job = Job.getInstance();

        for (int i = 1; i < allLines.size(); i++) {

            try (var scanner = new Scanner(allLines.get(i))) {

                while (scanner.hasNext()) {
                    int startTime = Integer.parseInt(scanner.next());
                    int dueDate = Integer.parseInt(scanner.next());
                    double weight = Double.parseDouble(scanner.next());
                    int numberOfTasks = Integer.parseInt(scanner.next());

                    Job j = job.createJob(startTime, dueDate, weight, numberOfTasks);

                    while (scanner.hasNext()) {
                        int machineId = Integer.parseInt(scanner.next());
                        Machine m = machine.createMachine(machineId);
                        int time = Integer.parseInt(scanner.next());
                        task.createTask(j, m, time);
                    }
                }
            }
        }

        return task.getTaskList();
    }

    public static FileParser getInstance() {
        return new FileParser();
    }

    public Benchmark getBenchmark() {
        return this.benchmark;
    }
}
