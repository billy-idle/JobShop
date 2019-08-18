package com.github.guillesup.entities;

import java.util.Objects;

/**
 * This class represent a task in the Job-Shop problem.
 *
 * @author m.mcfly
 */
public class Task implements Comparable<Task> {
    private static final Task FICTIVE_INIT_TASK;
    private static final Task FICTIVE_END_TASK;

    static {
        FICTIVE_INIT_TASK = new Task(0, Job.getFictiveJob(), Machine.getFictiveMachine(), 0);
        FICTIVE_END_TASK = new Task(-1, Job.getFictiveJob(), Machine.getFictiveMachine(), 0);
    }

    private int id;
    private Machine machine;
    private Job job;
    private int startTime;
    private int endTime;
    private int time;

    private Task(int id, Job job, Machine machine, int time) {
        this.id = id;
        this.machine = Objects.requireNonNull(machine, "Machine must not be null!");
        this.time = time;
        this.job = Objects.requireNonNull(job, "Job must not be null!");
        assessInput();
    }

    /**
     * {@link #id} int = 0
     * {@link #job} FICTIVE_JOB
     * {@link #machine} FICTIVE_MACHINE
     * {@link #time} int >= 0
     *
     * @return FICTIVE_INIT_TASK that represent the starting vertex.
     */
    public static Task getFictiveInitTask() {
        return FICTIVE_INIT_TASK;
    }

    /**
     * {@link #id} int = -1
     * {@link #job} FICTIVE_JOB
     * {@link #machine} FICTIVE_MACHINE
     * {@link #time} int >= 0
     *
     * @return FICTIVE_END_TASK that represent the ending vertex.
     */
    public static Task getFictiveEndTask() {
        return FICTIVE_END_TASK;
    }

    /**
     * Task factory method.
     *
     * @param id      int >= 0
     * @param job     Job instance
     * @param machine Machine instance
     * @param time    int >= 0
     * @return A new Task instance.
     */
    public static Task createTask(int id, Job job, Machine machine, int time) {
        return new Task(id, job, machine, time);
    }

    private void assessInput() {
        if (this.time < 0) {
            throw new TaskException("Task time must be greater than or equal to zero");
        } else if (this.id < -1) {
            throw new TaskException("Task Id must be greater than or equal to zero");
        }
    }

    @Override
    public int hashCode() {
        return 31 +
                Objects.hashCode(this.id) +
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
                this.job.equals(otherTask.job) &&
                this.machine == otherTask.machine &&
                this.time == otherTask.time);
    }

    @Override
    public String toString() {
        return ("" + this.machine.getId() + ":" + job.toString());
    }

    public int getId() {
        return this.id;
    }

    public Job getJob() {
        return this.job;
    }

    public Machine getMachine() {
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
        assessStartTime();
    }

    private void assessStartTime() {
        if (this.startTime < 0) {
            throw new TaskException("Start time must be greater than or equal to zero");
        }
    }

    public int getEndTime() {
        return this.endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
        assessEndTime();
    }

    private void assessEndTime() {
        if (this.endTime < 0) {
            throw new TaskException("End time must be greater than or equal to zero");
        } else if (this.endTime < this.startTime) {
            throw new TaskException("End time must be greater than or equal to start time");
        }
    }

    @Override
    public int compareTo(Task otherTask) {
        if (job.compareTo(otherTask.job) == 0 && this.id < otherTask.id) {
            return -1;
        } else if (job.compareTo(otherTask.job) == 0 && this.id > otherTask.id) {
            return 1;
        } else if (job.compareTo(otherTask.job) > 0) {
            return 1;
        } else if (job.compareTo(otherTask.job) < 0) {
            return -1;
        }

        return 0;
    }
}

class TaskException extends RuntimeException {
    TaskException(String message) {
        super(message);
    }
}
