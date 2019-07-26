package com.github.guillesup.entities;

public class Task {

    private Machine machine;
    private int time;

    public Task(Machine machine, int time) {
        this.machine = machine;
        this.time = time;
        assesInput();
    }

    private void assesInput() {
        if (this.machine == null) {
            throw new TaskException("Machine cannot be null");
        }else if (this.time < 0) {
            throw new TaskException("Time must be greater than or equal to zero");
        }
    }

    public Machine getMachine() {
        return this.machine;
    }

    public int getTime() {
        return this.time;
    }

    @Override
    public String toString() {
        return "(" + this.machine.getId() + ", " + this.time + ")";
    }

}

class TaskException extends RuntimeException {
    TaskException(String message) {
        super(message);
    }
}
