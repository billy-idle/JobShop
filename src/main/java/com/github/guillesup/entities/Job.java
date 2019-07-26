package com.github.guillesup.entities;

import java.util.List;

public class Job {
    private int id;
    private int startTime;
    private int dueDate;
    private double weight;
    private List<Task> tasks;

    public Job(int id, int startTime, int dueDate, double weight, List<Task> tasks) {
        this.id = id;
        this.startTime = startTime;
        this.dueDate = dueDate;
        this.weight = weight;
        this.tasks = tasks;
        assesInput();
    }

    private void assesInput() {
        if (this.id < 0 || this.startTime < 0 || this.dueDate < 0
                || Double.compare(this.weight, 0.0) < 0 || tasks.isEmpty()) {
            throw new JobException("JobException, please check the parameters");
        }
    }

    public int getId() {
        return id;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getDueDate() {
        return dueDate;
    }

    public double getWeight() {
        return weight;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    @Override
    public String toString() {
        String leftSideOfEqual = "Job " + this.id + " = ";
        StringBuilder rightSideOfEqual = new StringBuilder("[");

        for (int i = 0; i < this.tasks.size(); i++) {
            Task task = this.tasks.get(i);
            rightSideOfEqual.append(task.toString());

            if (i == this.tasks.size() - 1) {
                break;
            } else {
                rightSideOfEqual.append(", ");
            }
        }

        rightSideOfEqual.append("]");

        return leftSideOfEqual + rightSideOfEqual;
    }

}

class JobException extends RuntimeException {
    JobException(String message) {
        super(message);
    }
}