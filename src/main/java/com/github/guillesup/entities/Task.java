package com.github.guillesup.entities;

import java.util.Objects;

public class Task {
    private int id;
    private Machine machine;
    private int time;
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
        } else if (this.endTime <= this.startTime) {
            throw new TaskException("End time must be greater than start time");
        }
    }

    public int getId() {
        return id;
    }

    public Machine getMachine() {
        return this.machine;
    }

    public int getTime() {
        return this.time;
    }

    public int getMachineId() {
        return this.machine.getId();
    }

    public void addMachine(Machine machine) {
        this.machine = Objects.requireNonNull(machine);
    }

    @Override
    public String toString() {
        return String.format("|  %-6d|    %-7s| %-5d|    %-7d|   %-6d|%n",
                this.id, this.machine.toString(), this.time,
                this.startTime, this.endTime);
    }

}

class TaskException extends RuntimeException {
    TaskException(String message) {
        super(message);
    }
}
