package com.github.guillesup.entities;

import java.util.Objects;

public class Job implements Comparable<Job> {
    private final int id;
    private final int startTime;
    private final int dueDate;
    private final double weight;
    private final int numberOfTasks;

    private Job(int id, int startTime, int dueDate, double weight, int numberOfTasks) {
        this.id = id;
        this.startTime = startTime;
        this.dueDate = dueDate;
        this.weight = weight;
        this.numberOfTasks = numberOfTasks;
        assesInput();
    }

    private void assesInput() {
        if (this.id < 0) {
            throw new JobException("Job Id must be greater or equal than zero");
        } else if (this.startTime < 0) {
            throw new JobException("Job startTime must be greater than or equal to zero");
        } else if (this.dueDate < 0) {
            throw new JobException("Job dueDate must be greater than zero");
        } else if (Double.compare(this.weight, 0.0) < 0) {
            throw new JobException("Job weight must be greater than or equal to zero");
        } else if (this.numberOfTasks < 0) {
            throw new JobException("Job number of tasks must be greater than or equal to zero");
        }
    }

    public static Job createDummyJob() {
        return new Job(0, 0, 0, 0.0, 0);
    }

    public static Job createJob(int id, int startTime, int dueDate, double weight, int numberOfTasks) {
        return new Job(id, startTime, dueDate, weight, numberOfTasks);
    }

    public int getNumberOfTasks() {
        return numberOfTasks;
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

    @Override
    public int compareTo(Job otherJob) {
        if (this.weight > otherJob.weight) {
            return 1;
        } else if (this.weight < otherJob.weight) {
            return -1;
        } else if (this.weight == otherJob.weight && this.dueDate < otherJob.dueDate) {
            return 1;
        } else if (this.weight == otherJob.weight && this.dueDate > otherJob.dueDate) {
            return -1;
        }

        return 0;
    }

    @Override
    public int hashCode() {
        return 31 *
                Objects.hashCode(this.id) +
                Objects.hashCode(this.startTime) +
                Objects.hashCode(this.dueDate) +
                Objects.hashCode(this.weight) +
                Objects.hashCode(this.numberOfTasks);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        if (o == this)
            return true;

        if (!(o instanceof Job))
            return false;

        var otherJob = (Job) o;

        return (this.id == otherJob.id &&
                this.startTime == otherJob.startTime &&
                this.dueDate == otherJob.dueDate &&
                this.weight == otherJob.weight &&
                this.numberOfTasks == otherJob.numberOfTasks);
    }

    public int getId() {
        return this.id;
    }
}

class JobException extends RuntimeException {
    JobException(String message) {
        super(message);
    }
}