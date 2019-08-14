package com.github.guillesup.entities;

import java.util.*;

public class Job implements Comparable<Job> {
    private static final Job FICTIVE_JOB;

    static {
        FICTIVE_JOB = new Job(0, 0, 0, 0, 0);
    }

    private final Map<Integer, Job> jobMap;
    private int id;
    private int startTime;
    private int dueDate;
    private double weight;
    private int numberOfTasks;
    private int sequence;

    {
        this.jobMap = new HashMap<>();
    }

    private Job(int id, int startTime, int dueDate, double weight, int numberOfTasks) {
        this.id = id;
        this.startTime = startTime;
        this.dueDate = dueDate;
        this.weight = weight;
        this.numberOfTasks = numberOfTasks;
        assessInput();
    }

    private void assessInput() {
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

    private Job() {

    }

    public static Job getInstance() {
        return new Job();
    }

    public static Job getFictiveJob() {
        return FICTIVE_JOB;
    }

    public Job createJob(int startTime, int dueDate, double weight, int numberOfTasks) {
        int jodId = nextInteger();
        this.jobMap.put(jodId, new Job(jodId, startTime, dueDate, weight, numberOfTasks));
        return this.jobMap.get(jodId);
    }

    private int nextInteger() {
        return this.sequence++;
    }

    public List<Job> getSortedJobList() {
        Collection<Job> values = this.jobMap.values();
        List<Job> jobList = new ArrayList<>(values);
        jobList.sort(Collections.reverseOrder());
        return jobList;
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
        return 167 +
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

    @Override
    public String toString() {
        return "j" + this.id;
    }

    public int getNumberOfTasks() {
        return this.numberOfTasks;
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