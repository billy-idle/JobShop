package com.github.guillesup.entities;

import java.util.Objects;

/**
 * This class represent a job in the Job-Shop problem.
 *
 * @author m.mcfly
 */
public class Job implements Comparable<Job> {
    private static final Job FICTIVE_JOB;

    static {
        FICTIVE_JOB = new Job(0, 0, 0, 0, 0);
    }

    private int id;
    private int startTime;
    private int dueDate;
    private double weight;
    private int numberOfTasks;

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

    /**
     * {@link #id} int = 0
     * {@link #startTime} int = 0
     * {@link #dueDate} int = 0
     * {@link #weight} int = 0
     * {@link #numberOfTasks} int = 0
     *
     * @return A FICTIVE_JOB only used by Task.FICTIVE_INIT_TASK and Task.FICTIVE_END_TASK.
     */
    public static Job getFictiveJob() {
        return FICTIVE_JOB;
    }

    /**
     * @param id            int >= 0
     * @param startTime     int >= 0
     * @param dueDate       int >= 0
     * @param weight        int >= 0
     * @param numberOfTasks int >= 0
     * @return A new instance of Job.
     */
    public static Job createJob(int id, int startTime, int dueDate, double weight, int numberOfTasks) {
        return new Job(id, startTime, dueDate, weight, numberOfTasks);
    }

    /**
     * @return An integer value that represents the start time.
     */
    public int getStartTime() {
        return this.startTime;
    }

    /**
     * @return An integer value that represents the due date.
     */
    public int getDueDate() {
        return this.dueDate;
    }

    /**
     * @return An integer value that represents the weight.
     */
    public double getWeight() {
        return this.weight;
    }

    /**
     * Sort by weight and due date.
     *
     * @param otherJob Job to compare.
     * @return (1) if "this.job" is weightier than the "otherJob", (-1) if is the other way,
     * (1) if both have the same weight but if "this.job" dueDate is less than the "otherJob",
     * (-1) if is the other way, (0) if they have the same weight and dueDate.
     */
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
        return String.valueOf(this.id);
    }

    /**
     * @return An integer value that represents the number of tasks.
     */
    public int getNumberOfTasks() {
        return this.numberOfTasks;
    }

    /**
     * @return An integer value that represents the job's Id.
     */
    public int getId() {
        return this.id;
    }

    /**
     * @param id Must be an integer greater than or equal to zero.
     */
    public void setId(int id) {
        this.id = id;
    }
}

/**
 * Use to throw exceptions related only with Job class.
 */
class JobException extends RuntimeException {
    JobException(String message) {
        super(message);
    }
}