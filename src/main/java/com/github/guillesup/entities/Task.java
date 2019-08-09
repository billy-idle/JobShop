package com.github.guillesup.entities;

import java.util.Objects;

public class Task implements Comparable<Task> {
    private final int id;
    private final int machine;
    private final int time;
    private final Job job;
    private int startTime;
    private int endTime;

    Task(int id, int machine, int time, Job job) {
        this.id = id;
        this.machine = machine;
        this.time = time;
        this.job = Objects.requireNonNull(job, "Job must not be null!");
        assesInput();
    }

    private void assesInput() {
        if (this.time < 0) {
            throw new TaskException("Task time must be greater than or equal to zero");
        } else if (this.id < 0) {
            throw new TaskException("Task Id must be greater than or equal to zero");
        } else if (this.machine < 0) {
            throw new TaskException("Task machine must be greater than or equal to zero");
        }
    }

    public static Task createTask(int id, int machine, int time, Job job) {
        return new Task(id, machine, time, job);
    }

    public int getEndTime() {
        return this.endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
        assesEndTime();
    }

    private void assesEndTime() {
        if (this.endTime < 0) {
            throw new TaskException("End time must be greater than or equal to zero");
        } else if (this.endTime < this.startTime) {
            throw new TaskException("End time must be greater than or equal to start time");
        }
    }

    @Override
    public int hashCode() {
        return 31 *
                Objects.hashCode(this.id) *
                Objects.hashCode(this.machine) +
                Objects.hashCode(this.time) +
                Objects.hashCode(this.job);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        if (o == this)
            return true;

        if (!(o instanceof Task))
            return false;

        var otherTask = (Task) o;

        return (this.id == otherTask.id &&
                this.machine == otherTask.machine &&
                this.time == otherTask.time &&
                this.job.equals(otherTask.job));
    }

    @Override
    public String toString() {
        return ("" + this.id);
    }

    public int getId() {
        return id;
    }

    public int getMachine() {
        return this.machine;
    }

    public int getTime() {
        return this.time;
    }

    public int getStartTime() {
        return this.startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
        assesStartTime();
    }

    private void assesStartTime() {
        if (this.startTime < 0) {
            throw new TaskException("Start time must be greater than or equal to zero");
        }
    }

    public int getJobId() {
        return this.job.getId();
    }

    @Override
    public int compareTo(Task otherTask) {
        return this.job.compareTo(otherTask.getJob());
    }

    public Job getJob() {
        return this.job;
    }
}

class TaskException extends RuntimeException {
    TaskException(String message) {
        super(message);
    }
}
