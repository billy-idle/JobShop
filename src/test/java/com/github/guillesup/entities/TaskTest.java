package com.github.guillesup.entities;

import org.junit.Test;

import static org.junit.Assert.*;

public class TaskTest {

    @Test(expected = TaskException.class)
    public void whenStartTimeIsNegative() {
        Task.createTask(1, Job.getFictiveJob(), Machine.getFictiveMachine(), 1).setStartTime(-1);
    }

    @Test(expected = TaskException.class)
    public void whenEndTimeIsNegative() {
        Task.createTask(1, Job.getFictiveJob(), Machine.getFictiveMachine(), 1).setEndTime(-1);
    }

    @Test(expected = TaskException.class)
    public void whenEndTimeIsLessThanStartTime() {
        Task task = Task.createTask(1, Job.getFictiveJob(), Machine.getFictiveMachine(), 1);
        task.setStartTime(50);
        task.setEndTime(49);
    }

    @Test
    public void taskEqualsToItself() {
        Task task = Task.createTask(1, Job.getFictiveJob(), Machine.getFictiveMachine(), 1);
        assertEquals(task, task);
    }

    @Test
    public void taskNotEqualsToNull() {
        Task task = Task.createTask(1, Job.getFictiveJob(), Machine.getFictiveMachine(), 1);
        assertNotEquals(task, null);
    }

    @Test
    public void notInstanceOfTask() {
        Task task = Task.createTask(1, Job.getFictiveJob(), Machine.getFictiveMachine(), 1);
        assertNotEquals(task, new Object());
    }

    @Test
    public void tasksAreNotEquals() {
        Task t = Task.createTask(1, Job.getFictiveJob(), Machine.getFictiveMachine(), 1);
        Task t1 = Task.createTask(2, Job.getFictiveJob(), Machine.getFictiveMachine(), 1);
        assertNotEquals(t1, t);
    }

    @Test
    public void testPublicGetters() {
        Task task = Task.createTask(1, Job.getFictiveJob(), Machine.getFictiveMachine(), 0);
        assertTrue(task.getId() == 1 &&
                task.getTime() == 0 &&
                task.getStartTime() == 0 &&
                task.getEndTime() == 0 &&
                task.getMachine().equals(Machine.getFictiveMachine()) &&
                task.getJob().equals(Job.getFictiveJob()));
    }

    @Test
    public void sameHashCode() {
        Task task = Task.createTask(1, Job.getFictiveJob(), Machine.getFictiveMachine(), 1);
        assertEquals(task.hashCode(), task.hashCode());
    }
}