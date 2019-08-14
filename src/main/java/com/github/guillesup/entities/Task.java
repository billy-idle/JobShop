package com.github.guillesup.entities;

import java.util.*;

public class Task implements Comparable<Task> {
    private static final Task FICTIVE_INIT_TASK;
    private static final Task FICTIVE_END_TASK;

    static {
        FICTIVE_INIT_TASK = new Task(0, Job.getFictiveJob(), Machine.getFictiveMachine(), 0);
        FICTIVE_END_TASK = new Task(Integer.MAX_VALUE, Job.getFictiveJob(), Machine.getFictiveMachine(), 0);
    }

    private final Map<Integer, Task> taskMap;
    private final List<Task> taskList;
    private int id;
    private Machine machine;
    private int time;
    private Job job;
    private int startTime;
    private int endTime;
    private int sequenceId;

    {
        this.taskMap = new HashMap<>();
        this.taskList = new ArrayList<>();
    }

    private Task(int id, Job job, Machine machine, int time) {
        this.id = id;
        this.machine = Objects.requireNonNull(machine, "Machine must not be null!");
        this.time = time;
        this.job = Objects.requireNonNull(job, "Job must not be null!");
        assessInput();
    }

    private void assessInput() {
        if (this.time < 0) {
            throw new TaskException("Task time must be greater than or equal to zero");
        } else if (this.id < 0) {
            throw new TaskException("Task Id must be greater than or equal to zero");
        }
    }

    public static Task getInstance() {
        return new Task(0, Job.getInstance(), Machine.getInstance(), 0);
    }

    public static Task getFictiveInitTask() {
        return FICTIVE_INIT_TASK;
    }

    public static Task getFictiveEndTask() {
        return FICTIVE_END_TASK;
    }

    public List<Task> getTaskList() {
        return this.taskList;
    }

    public Task createTask(Job job, Machine machine, int time) {
        int taskId = nextId();
        Task task = new Task(taskId, job, machine, time);
        this.taskList.add(task);
        this.taskMap.put(taskId, task);
        return this.taskMap.get(taskId);
    }

    private int nextId() {
        return this.sequenceId++;
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
        return ("" + this.machine.getId() + this.job.getId());
    }

    public Job getJob() {
        return this.job;
    }

    public int getId() {
        return this.id;
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
        assesStartTime();
    }

    private void assesStartTime() {
        if (this.startTime < 0) {
            throw new TaskException("Start time must be greater than or equal to zero");
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
