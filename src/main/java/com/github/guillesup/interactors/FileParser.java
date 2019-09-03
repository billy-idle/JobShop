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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This class is used to parse the benchmark-set files.
 * It depends on "config.properties"
 *
 * @author m.mcfly
 */
public class FileParser {
    private final List<Benchmark> benchmarkList;
    private Path path;

    private FileParser() {
        this.benchmarkList = new ArrayList<>();
        readPathConfigFile();
        var pathList = getFilesPath();
        txtToBenchmark(pathList);
    }

    private void readPathConfigFile() {
        final var props = new Properties();

        try (var in = new FileInputStream("config.properties")) {
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.path = Paths.get(props.getProperty("path"));
    }

    private List<Path> getFilesPath() {
        List<Path> pathList = new ArrayList<>();

        try (var pathStream =
                     Files.walk(this.path).
                             filter(p -> p.getFileName().
                                     toString().
                                     endsWith(".txt"))) {

            pathStream.forEach(pathList::add);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return pathList;
    }

    private void txtToBenchmark(List<Path> pathList) {
        for (var p : pathList) {
            this.benchmarkList.add(processLines(p));
        }
    }

    private Benchmark processLines(Path p) {
        int totalJobs = 0;
        int totalMachines = 0;
        int totalTasks = 0;

        List<Task> taskList = new ArrayList<>();
        final List<String> allLines;

        String benchmarkId = p.getFileName().toString();

        try {
            allLines = Files.readAllLines(p);

            try (var scanner = new Scanner(allLines.get(0))) {
                while (scanner.hasNext()) {
                    totalJobs = Integer.parseInt(scanner.next()); // #Jobs
                    totalMachines = Integer.parseInt(scanner.next()); // #Machines
                    totalTasks = Integer.parseInt(scanner.next()); // #Tasks
                }
            }

            int jobId = 1;
            int taskId = 1;

            for (int i = 1; i < allLines.size(); i++) {
                try (var scanner = new Scanner(allLines.get(i))) {
                    while (scanner.hasNext()) {
                        int startTime = Integer.parseInt(scanner.next());
                        int dueDate = Integer.parseInt(scanner.next());
                        double weight = Double.parseDouble(scanner.next());
                        int numberOfTasks = Integer.parseInt(scanner.next());

                        Job job = Job.createJob(jobId++, startTime, dueDate, weight, numberOfTasks);

                        while (scanner.hasNext()) {
                            int machineId = Integer.parseInt(scanner.next());
                            Machine machine = Machine.createMachine(machineId);
                            int time = Integer.parseInt(scanner.next());
                            taskList.add(Task.createTask(taskId++, job, machine, time));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        sortThenRenameJobs(taskList);
        return Benchmark.createBenchmark(benchmarkId, totalJobs, totalMachines, totalTasks, taskList);
    }

    private void sortThenRenameJobs(List<Task> taskList) {
        var jobList =
                taskList.
                        stream().
                        map(Task::getJob).
                        distinct().
                        sorted(Collections.reverseOrder()).
                        collect(Collectors.toCollection(ArrayList::new));

        IntStream.range(0, jobList.size()).
                forEach(i -> {
                    int id = i + 1;
                    jobList.get(i).setId(id);
                });
    }

    /**
     * FileParser factory method.
     *
     * @return A new FileParser instance.
     */
    public static FileParser getInstance() {
        return new FileParser();
    }

    /**
     * @return An ArrayList of benchmarks.
     */
    public List<Benchmark> getBenchmarkList() {
        return this.benchmarkList;
    }

    public Benchmark getBenchmark(String filename) {
        return this.benchmarkList.
                stream().
                filter(t -> t.getId().contains(filename)).
                findFirst().orElseThrow();
    }
}
