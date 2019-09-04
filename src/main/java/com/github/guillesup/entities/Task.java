package com.github.guillesup.entities;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class represent a task in the Job-Shop problem.
 *
 * @author m.mcfly
 */
public class Task implements Comparable<Task>, Subject, Observer {
    private final int id;
    private final Machine machine;
    private final Job job;
    private int startTime;
    private int endTime;
    private final int time;
    private final List<Observer> observerList;

    private Task(int id, Job job, Machine machine, int time) {
        this.id = id;
        this.machine = Objects.requireNonNull(machine, "Machine must not be null!");
        this.time = time;
        this.job = Objects.requireNonNull(job, "Job must not be null!");
        this.observerList = new ArrayList<>();
        assessInput();
    }

    private void assessInput() {
        if (this.time < 0) {
            throw new TaskException("Task time must be greater than or equal to zero");
        } else if (this.id < -1) {
            throw new TaskException("Task Id must be greater than or equal to zero");
        }
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
        return new Task(0, Job.getFictiveJob(), Machine.getFictiveMachine(), 0);
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
        return new Task(-1, Job.getFictiveJob(), Machine.getFictiveMachine(), 0);
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
        return String.valueOf(this.id);
    }

    public String toJobShopNotation() {
        return this.machine.getId() + ":" + this.job.getId();
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

    public int getStartTime() {
        return this.startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
        assessStartTime();
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

    @Override
    public void registerObserver(Observer o) {
        this.observerList.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        this.observerList.remove(o);
    }

    @Override
    public void notifyObservers(Graph<Task, DefaultWeightedEdge> directedWeightedGraph) {
        for (var o : this.observerList) {
            o.update(directedWeightedGraph);
        }
    }

    @Override
    public void update(Graph<Task, DefaultWeightedEdge> directedWeightedGraph) {
        var optionalInteger =
                Graphs.predecessorListOf(directedWeightedGraph, this).
                        stream().
                        map(Task::getEndTime).
                        max(Integer::compareTo);

        int startTime = 0;

        if (optionalInteger.isPresent()) {
            startTime = optionalInteger.get();
        }

        this.setEndTime(startTime + this.getTime());
        this.setStartTime(startTime);

        this.notifyObservers(directedWeightedGraph);
    }

    public int getEndTime() {
        return this.endTime;
    }

    public int getTime() {
        return this.time;
    }

    private void assessEndTime() {
        if (this.endTime < 0) {
            throw new TaskException("End time must be greater than or equal to zero");
        } else if (this.endTime < this.startTime) {
            throw new TaskException("End time must be greater than or equal to start time");
        }
    }

    private void assessStartTime() {
        if (this.startTime < 0) {
            throw new TaskException("Start time must be greater than or equal to zero");
        }
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
        assessEndTime();
    }
}

class TaskException extends RuntimeException {
    TaskException(String message) {
        super(message);
    }
}
