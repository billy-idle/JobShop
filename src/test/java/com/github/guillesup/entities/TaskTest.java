package com.github.guillesup.entities;

import org.junit.Test;

import static org.junit.Assert.*;


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
        createDefaultTask().setStartTime(-1);
    }

    private Task createDefaultTask() {
        return new Task(1, new Machine(1), 1);
    }

    @Test(expected = TaskException.class)
    public void whenEndTimeIsNegative() {
        createDefaultTask().setEndTime(-1);
    }

    @Test(expected = TaskException.class)
    public void whenEndTimeIsLessThanStartTime() {
        Task task = createDefaultTask();
        task.setStartTime(50);
        task.setEndTime(49);
    }

    @Test(expected = TaskException.class)
    public void whenEndTimeEqualToStartTime() {
        Task task = createDefaultTask();
        task.setStartTime(37);
        task.setEndTime(37);
    }

    @Test(expected = TaskException.class)
    public void whenEndTimeIsEqualToStartTime() {
        Task task = createDefaultTask();
        task.setStartTime(50);
        task.setEndTime(50);
    }

    @Test
    public void taskEqualsToItself() {
        Task task = createDefaultTask();
        assertEquals(task, task);
    }

    @Test
    public void taskNotEqualsToNull() {
        assertNotEquals(createDefaultTask(), null);
    }

    @Test
    public void notInstanceOfTask() {
        assertNotEquals(createDefaultTask(), new Object());
    }

    @Test
    public void differentTasksButSameContent() {
        assertEquals(createDefaultTask(), createDefaultTask());
    }

    @Test
    public void tasksAreNotEquals() {
        assertNotEquals(createDefaultTask(),
                new Task(2, new Machine(1), 1));
    }

    @Test
    public void testPublicGetters() {
        Task task = createDefaultTask();
        assertTrue(task.getId() == 1 &&
                task.getTime() == 1 &&
                task.getStartTime() == 0 &&
                task.getEndTime() == 0 &&
                task.getMachine().equals(new Machine(1)) &&
                task.getMachineId() == 1);
    }

    @Test
    public void sameHashCode() {
        assertEquals(createDefaultTask().hashCode(),
                createDefaultTask().hashCode());
    }
}