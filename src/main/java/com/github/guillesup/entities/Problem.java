package com.github.guillesup.entities;

import java.util.List;
import java.util.Objects;

public class Problem {
    private String id;
    private List<Integer> settings;
    private List<Job> jobs;

    public Problem(String id, List<Integer> settings, List<Job> jobs) {
        this.id = Objects.requireNonNull(id, "Problem id required!");
        this.settings = Objects.requireNonNull(settings, "Problem settings required!");
        this.jobs = Objects.requireNonNull(jobs, "Problem jobs required!");
    }

    public String getId() {
        return this.id;
    }

    public List<Job> getJobs() {
        return this.jobs;
    }

    public List<Integer> getSettings() {
        return this.settings;
    }

    @Override
    public String toString() {
        String dashes = "-------------------------------------------------\n";
        String header = String.format(" %-11s%s%n %-10s\t%s%n %-10s%s%n %-10s\t%s%n%n",
                "Problem: ", this.id, "#Jobs: ", getNumberOfJobs(),
                "#Machines: ", getNumberOfMachines(), "#Tasks: ", getNumberOfTasks());

        StringBuilder body = new StringBuilder();

        for (Job job : this.jobs) {
            body.append(job.toString()).append("\n");
        }

        return dashes + header + dashes + body;
    }

    public int getNumberOfJobs() {
        return this.settings.get(0);
    }

    public int getNumberOfMachines() {
        return this.settings.get(1);
    }

    public int getNumberOfTasks() {
        return this.settings.get(2);
    }
}
