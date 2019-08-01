package com.github.guillesup.interactors;

import com.github.guillesup.entities.Job;
import com.github.guillesup.entities.Machine;
import com.github.guillesup.entities.Problem;
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
import java.util.stream.Stream;

public class ProblemLoader {
    private final List<Problem> problems;
    private Path workPath;
    private List<String> fileContent;
    private final List<Path> filesPath;

    public ProblemLoader() {
        this.problems = new ArrayList<>();
        this.fileContent = new ArrayList<>();
        this.filesPath = new ArrayList<>();

        setWorkPath();
        seekFiles();
        filesToProblems();
    }

    public ProblemLoader(String workPath) {
        this.problems = new ArrayList<>();
        this.fileContent = new ArrayList<>();
        this.filesPath = new ArrayList<>();

        this.workPath = Paths.get(workPath);
        seekFiles();
        filesToProblems();
    }

    private void setWorkPath() {
        final var props = new Properties();

        try {
            props.load(new FileInputStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.workPath = Paths.get(props.getProperty("workPath"));
    }

    private void seekFiles() {
        try (Stream<Path> pathStream = Files.walk(this.workPath).
                filter(p -> p.getFileName().toString().endsWith(".txt"))) {

            pathStream.forEach(this.filesPath::add);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void filesToProblems() {
        for (Path path : this.filesPath) {
            this.problems.add(parseFile(path));
        }
    }

    private Problem parseFile(Path path) {
        try {
            this.fileContent = Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Problem(path.toString(), processSettings(), processJobs());
    }

    private List<Integer> processSettings() {
        try (Scanner headerScanner = new Scanner(fileContent.get(0))) {

            List<Integer> settings = new ArrayList<>();

            while (headerScanner.hasNext()) {
                settings.add(Integer.parseInt(headerScanner.next())); // #Jobs
                settings.add(Integer.parseInt(headerScanner.next())); // #Machines
                settings.add(Integer.parseInt(headerScanner.next())); // #Tasks
            }

            return settings;
        }
    }

    private List<Job> processJobs() {
        List<Job> jobs = new ArrayList<>();

        for (int i = 1; i < this.fileContent.size(); i++) {
            jobs.add(extractJob(i));
        }

        return jobs;
    }

    private Job extractJob(int line) {
        try (Scanner jobScanner = new Scanner(fileContent.get(line))) {
            int startTime = Integer.parseInt(jobScanner.next());
            int dueDate = Integer.parseInt(jobScanner.next());
            double weight = Double.parseDouble(jobScanner.next());
            int numberOfTaskPerJob = Integer.parseInt(jobScanner.next());

            int taskCounter = 1;
            List<Task> tasks = new ArrayList<>(numberOfTaskPerJob);

            while (jobScanner.hasNext()) {
                int machineId = Integer.parseInt(jobScanner.next());
                int time = Integer.parseInt(jobScanner.next());
                int taskId = Integer.parseInt(String.valueOf(line) + taskCounter++);
                tasks.add(new Task(taskId, createMachine(machineId, tasks), time));
            }

            return new Job(line, startTime, dueDate, weight, tasks);
        }
    }

    private Machine createMachine(int machineId, List<Task> tasks) {
        if (!tasks.isEmpty()) {
            for (Task task : tasks) {
                if (machineId == task.getMachineId()) {
                    return task.getMachine();
                }
            }
        }

        return new Machine(machineId);
    }

    public List<Problem> getProblems() {
        return this.problems;
    }
}
