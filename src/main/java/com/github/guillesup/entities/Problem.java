package com.github.guillesup.entities;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Problem {
    private final String id;
    private final List<Integer> settings;
    private final List<Job> jobs;

    public Problem(String id, List<Integer> settings, List<Job> jobs) {
        this.id = Objects.requireNonNull(id, "Problem id required!");
        this.settings = Objects.requireNonNull(settings, "Problem settings required!");
        this.jobs = Objects.requireNonNull(jobs, "Problem jobs required!");
    }

    public String getId() {
        return this.id;
    }

    public List<Integer> getSettings() {
        return this.settings;
    }

    public List<Job> getJobs() {
        return this.jobs;
    }

    public void sort() {
        this.jobs.sort(Collections.reverseOrder());
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

    @Override
    public int hashCode() {
        return 31 *
                Objects.hashCode(this.id) +
                Objects.hashCode(this.settings) +
                Objects.hashCode(this.jobs);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        if (o == this)
            return true;

        if (!(o instanceof Problem))
            return false;

        Problem otherProblem = (Problem) o;

        return (this.jobs.equals(otherProblem.jobs));
    }
}
