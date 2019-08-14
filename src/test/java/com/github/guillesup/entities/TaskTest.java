package com.github.guillesup.entities;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TaskTest {
    private Task task;

    @Before
    public void setUp() throws Exception {
        task = Task.getInstance();
    }

    @Test(expected = TaskException.class)
    public void whenStartTimeIsNegative() {
        task.createTask(Job.getFictiveJob(), Machine.getFictiveMachine(), 1).setStartTime(-1);
    }

    @Test(expected = TaskException.class)
    public void whenEndTimeIsNegative() {
        task.createTask(Job.getFictiveJob(), Machine.getFictiveMachine(), 1).setEndTime(-1);
    }

    @Test(expected = TaskException.class)
    public void whenEndTimeIsLessThanStartTime() {
        Task t = task.createTask(Job.getFictiveJob(), Machine.getFictiveMachine(), 1);
        t.setStartTime(50);
        t.setEndTime(49);
    }

    @Test
    public void taskEqualsToItself() {
        Task t = task.createTask(Job.getFictiveJob(), Machine.getFictiveMachine(), 1);
        assertEquals(t, t);
    }

    @Test
    public void taskNotEqualsToNull() {
        Task t = task.createTask(Job.getFictiveJob(), Machine.getFictiveMachine(), 1);
        assertNotEquals(t, null);
    }

    @Test
    public void notInstanceOfTask() {
        Task t = task.createTask(Job.getFictiveJob(), Machine.getFictiveMachine(), 1);
        assertNotEquals(t, new Object());
    }

    @Test
    public void tasksAreNotEquals() {
        Task t = task.createTask(Job.getFictiveJob(), Machine.getFictiveMachine(), 1);
        Task t1 = task.createTask(Job.getFictiveJob(), Machine.getFictiveMachine(), 1);
        assertNotEquals(t1, t);
    }

    @Test
    public void testPublicGetters() {
        Task t = task.createTask(Job.getFictiveJob(), Machine.getFictiveMachine(), 0);
        assertTrue(t.getId() == 0 &&
                t.getTime() == 0 &&
                t.getStartTime() == 0 &&
                t.getEndTime() == 0 &&
                t.getMachine().equals(Machine.getFictiveMachine()) &&
                t.getJob().equals(Job.getFictiveJob()));
    }

    @Test
    public void sameHashCode() {
        Task t = task.createTask(Job.getFictiveJob(), Machine.getFictiveMachine(), 1);
        assertEquals(t.hashCode(), t.hashCode());
    }
}