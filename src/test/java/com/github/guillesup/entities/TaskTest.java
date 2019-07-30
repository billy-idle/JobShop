package com.github.guillesup.entities;

import org.junit.Test;

public class TaskTest {

    @Test(expected = TaskException.class)
    public void whenTimeIsNegative() {
        new Task(1, new Machine(1), -1);
    }

    @Test(expected = NullPointerException.class)
    public void whenMachineIsNull() {
        new Task(1, null, 1);
    }

    @Test(expected = TaskException.class)
    public void whenIdIsLessThanOne() {
        new Task(0, new Machine(1), 1);
    }

    @Test(expected = TaskException.class)
    public void whenStartTimeIsNegative() {
        new Task(1, new Machine(1), 1).setStartTime(-1);
    }

    @Test(expected = TaskException.class)
    public void whenEndTimeIsNegative() {
        new Task(1, new Machine(1), 1).setEndTime(-1);
    }

    @Test(expected = TaskException.class)
    public void whenEndTimeIsLessThanStartTime() {
        Task task = new Task(1, new Machine(1), 1);
        task.setStartTime(50);
        task.setEndTime(49);
    }

    @Test(expected = TaskException.class)
    public void whenEndTimeIsEqualToStartTime() {
        Task task = new Task(1, new Machine(1), 1);
        task.setStartTime(50);
        task.setEndTime(50);
    }
}