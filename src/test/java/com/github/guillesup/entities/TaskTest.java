package com.github.guillesup.entities;

import org.junit.Test;

import static org.junit.Assert.*;


public class TaskTest {

    @Test(expected = TaskException.class)
    public void whenTimeIsNegative() {
        Task.createTask(1, 1, -1, Job.createDummyJob());
    }

    @Test(expected = TaskException.class)
    public void whenMachineIsLessThanZero() {
        Task.createTask(1, -1, 1, Job.createDummyJob());
    }

    @Test(expected = TaskException.class)
    public void whenIdIsLessThanZero() {
        Task.createTask(-1, 1, 1, Job.createDummyJob());
    }

    @Test(expected = TaskException.class)
    public void whenStartTimeIsNegative() {
        DummyTask.createDummyTask().setStartTime(-1);
    }

    @Test(expected = TaskException.class)
    public void whenEndTimeIsNegative() {
        DummyTask.createDummyTask().setEndTime(-1);
    }

    @Test(expected = TaskException.class)
    public void whenEndTimeIsLessThanStartTime() {
        Task task = DummyTask.createDummyTask();
        task.setStartTime(50);
        task.setEndTime(49);
    }

    @Test(expected = TaskException.class)
    public void whenEndTimeEqualToStartTime() {
        Task task = DummyTask.createDummyTask();
        task.setStartTime(37);
        task.setEndTime(37);
    }

    @Test(expected = TaskException.class)
    public void whenEndTimeIsEqualToStartTime() {
        Task task = DummyTask.createDummyTask();
        task.setStartTime(50);
        task.setEndTime(50);
    }

    @Test
    public void taskEqualsToItself() {
        Task task = DummyTask.createDummyTask();
        assertEquals(task, task);
    }

    @Test
    public void taskNotEqualsToNull() {
        assertNotEquals(DummyTask.createDummyTask(), null);
    }

    @Test
    public void notInstanceOfTask() {
        assertNotEquals(DummyTask.createDummyTask(), new Object());
    }

    @Test
    public void differentTasksButSameContent() {
        assertEquals(DummyTask.createDummyTask(), DummyTask.createDummyTask());
    }

    @Test
    public void tasksAreNotEquals() {
        Task task = Task.createTask(1, 1, 1, Job.createDummyJob());
        task.setEndTime(10);
        assertNotEquals(DummyTask.createDummyTask(), task);
    }

    @Test
    public void testPublicGetters() {
        Job job = Job.createDummyJob();
        Task task = Task.createTask(1, 1, 1, job);
        task.setStartTime(0);
        task.setEndTime(1);

        assertTrue(task.getId() == 1 &&
                task.getTime() == 1 &&
                task.getStartTime() == 0 &&
                task.getEndTime() == 1 &&
                task.getMachine() == 1 &&
                task.getJob().equals(job));
    }

    @Test
    public void sameHashCode() {
        assertEquals(DummyTask.createDummyTask().hashCode(),
                DummyTask.createDummyTask().hashCode());
    }
}