package com.github.guillesup.entities;

import java.util.Objects;

public class Task {
    private final int id;
    private final Machine machine;
    private final int time;
    private int startTime;
    private int endTime;

    public Task(int id, Machine machine, int time) {
        this.id = id;
        this.machine = Objects.requireNonNull(machine, "Task machine must not be null");
        this.time = time;
        assesInput();
    }

    private void assesInput() {
        if (this.time < 0) {
            throw new TaskException("Time must be greater than or equal to zero");
        } else if (this.id <= 0) {
            throw new TaskException("Task Id must be greater than zero");
        }
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
        }

        if (this.endTime <= this.startTime) {
            throw new TaskException("End time must be greater than start time");
        }
    }

    @Override
    public int hashCode() {
        return 31 *
                Objects.hashCode(this.id) *
                Objects.hashCode(this.machine) +
                Objects.hashCode(this.time) +
                Objects.hashCode(this.startTime) +
                Objects.hashCode(this.endTime);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        if (o == this)
            return true;

        if (!(o instanceof Task))
            return false;

        Task otherTask = (Task) o;

        return (this.id == otherTask.id &&
                this.machine.equals(otherTask.machine) &&
                this.time == otherTask.time &&
                this.startTime == otherTask.startTime &&
                this.endTime == otherTask.endTime);
    }

    public int getId() {
        return id;
    }

    public Machine getMachine() {
        return this.machine;
    }

    public int getMachineId() {
        return this.machine.getId();
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
}

class TaskException extends RuntimeException {
    TaskException(String message) {
        super(message);
    }
}
