package com.github.guillesup.entities;

import java.util.List;
import java.util.Objects;

public class Job {
    private int id;
    private int startTime;
    private int dueDate;
    private double weight;
    private List<Task> tasks;

    public Job(int id, int startTime, int dueDate, double weight, List<Task> tasks) {
        this.id = id;
        this.startTime = startTime;
        this.dueDate = dueDate;
        this.weight = weight;
        this.tasks = Objects.requireNonNull(tasks, "Job tasks must not be null");
        assesInput();
    }

    private void assesInput() {
        if (this.id <= 0) {
            throw new JobException("Job Id must be greater than zero");
        } else if (this.startTime < 0) {
            throw new JobException("Job startTime must be greater than or equal to zero");
        } else if (this.dueDate < 0) {
            throw new JobException("Job dueDate must be greater than or equal to zero");
        } else if (Double.compare(this.weight, 0.0) < 0) {
            throw new JobException("Job weight must be greater than or equal to zero");
        } else if (this.tasks.isEmpty()) {
            throw new JobException("Job tasks cannot be empty");
        }
    }

    public int getId() {
        return this.id;
    }

    public int getStartTime() {
        return this.startTime;
    }

    public int getDueDate() {
        return this.dueDate;
    }

    public double getWeight() {
        return this.weight;
    }

    public List<Task> getTasks() {
        return this.tasks;
    }

    public int getNumberOfTasks() {
        return this.tasks.size();
    }

    @Override
    public String toString() {
        String headerDashes = " -----------------------------------------------\n";
        String rowDashes = " -------------------------------------------------\n";

        String header = String.format("| %-6s| %-10s| %-5s | %-6s | %-6s |%n",
                "JobId", "StartTime", "DueDate", "Weight", "#Tasks");
        String headerContent =
                String.format("|  %-5d|    %-7d|   %-6d|  %-5.1f |   %-4d |%n",
                        this.id, this.startTime, this.dueDate, this.weight,
                        this.tasks.size());
        String row = String.format("| %-7s| %-10s| %-5s| %-10s| %-8s|%n",
                "TaskId", "MachineId", "Time", "StartTime", "EndTime");
        StringBuilder rowContent = new StringBuilder();

        for (Task task : this.tasks) {
            rowContent.append(task.toString()).append(rowDashes);
        }


        return headerDashes + header + headerDashes + headerContent +
                headerDashes + "\n" + rowDashes + row + rowDashes + rowContent;
    }
}

class JobException extends RuntimeException {
    JobException(String message) {
        super(message);
    }
}