package com.github.guillesup.utility;

import com.github.guillesup.entities.Job;
import com.github.guillesup.entities.Problem;
import com.github.guillesup.entities.Task;

import java.util.List;
import java.util.Objects;

public class ProblemConsolePrinter {
    private final List<Problem> problems;

    public ProblemConsolePrinter(List<Problem> problems) {
        this.problems = Objects.requireNonNull(problems,
                "ProblemConsolePrinter problems must not be null");
    }

    public void printSortedJobs() {
        for (Problem problem : this.problems) {
            problem.sort();
        }

        printJobs();
    }

    public void printJobs() {
        for (Problem problem : this.problems) {
            printProblemSettings(problem);
            String headerDashes = " -----------------------------------------------\n";
            String header = String.format("| %-6s| %-10s| %-5s | %-6s | %-6s |%n",
                    "JobId", "StartTime", "Weight", "DueDate", "#Tasks");

            System.out.print(headerDashes + header + headerDashes);

            for (Job job : problem.getJobs()) {

                String headerContent =
                        String.format("|  %-5d|    %-7d|   %-5.1f|  %-6d |   %-4d |%n",
                                job.getId(), job.getStartTime(), job.getWeight(),
                                job.getDueDate(), job.getNumberOfTasks());

                System.out.print(headerContent + headerDashes);

//                printTasks(job);
            }

        }

    }

    private void printProblemSettings(Problem problem) {
        String dashes = "\n -----------------------------------------------\n";
        String header = String.format(" %-11s%s%n %-10s\t%s%n %-10s%s%n %-10s\t%s%n",
                "Problem: ", problem.getId(), "#Jobs: ", problem.getNumberOfJobs(),
                "#Machines: ", problem.getNumberOfMachines(), "#Tasks: ", problem.getNumberOfTasks());

        System.out.print(dashes + header);
    }

    public void printTasks(Job job) {
        String rowHeader = String.format("| %-7s| %-10s| %-5s| %-10s| %-8s|%n",
                "TaskId", "MachineId", "Time", "StartTime", "EndTime");

        System.out.print(rowHeader);

        String rowContent;

        for (Task task : job.getTasks()) {

            rowContent = String.format("|  %-6d|    %-7s| %-5d|    %-7d|   %-6d|%n",
                    task.getId(), task.getMachineId(), task.getTime(),
                    task.getStartTime(), task.getEndTime());

            System.out.print(rowContent);
        }

    }
}
