package com.github.guillesup.entities;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class JobTest {
    private List<Job> jobs;
    private List<Machine> machines;
    private List<Task> tasks;

    @Before
    public void setUp() {
        createThreeMachines();
        createEightTasks();
        createThreeJobs();
    }

    private void createThreeMachines() {
        machines = new ArrayList<>(3);

        machines.add(new Machine(1));
        machines.add(new Machine(2));
        machines.add(new Machine(3));
    }

    private void createEightTasks() {
        tasks = new ArrayList<>(8);

        tasks.add(new Task(1, machines.get(0), 3));
        tasks.add(new Task(2, machines.get(1), 2));
        tasks.add(new Task(3, machines.get(2), 2));

        tasks.add(new Task(4, machines.get(0), 2));
        tasks.add(new Task(5, machines.get(2), 1));
        tasks.add(new Task(6, machines.get(1), 4));

        tasks.add(new Task(7, machines.get(1), 4));
        tasks.add(new Task(8, machines.get(2), 3));
    }

    private void createThreeJobs() {
        jobs = new ArrayList<>(3);

        jobs.add(new Job(1, 0, 800, 1.0, tasks.subList(0, 3)));
        jobs.add(new Job(2, 0, 810, 2.0, tasks.subList(3, 6)));
        jobs.add(new Job(3, 0, 820, 3.0, tasks.subList(6, 8)));
    }

    @Test
    @Ignore
    public void printJobs() {
        StringBuilder actual = new StringBuilder();
        String expected =
                "Job 0 = [(0, 3), (1, 2), (2, 2)]\n" +
                        "Job 1 = [(0, 2), (2, 1), (1, 4)]\n" +
                        "Job 2 = [(1, 4), (2, 3)]\n";

        for (Job job : jobs) {
            actual.append(job.toString()).append("\n");
        }

        assertEquals(expected, actual.toString());
    }

    @Test(expected = JobException.class)
    public void whenIdIsLessThanZero() {
        new Job(-1, 0, 0, 0.0, this.tasks);
    }

    @Test(expected = JobException.class)
    public void whenStartTimeIsLessThanZero() {
        new Job(0, -1, 0, 0.0, this.tasks);
    }

    @Test(expected = JobException.class)
    public void whenDueDateIsLessThanZero() {
        new Job(0, 0, -1, 0.0, this.tasks);
    }

    @Test(expected = JobException.class)
    public void whenIdWeightLessThanZero() {
        new Job(0, 0, 0, -1.0, this.tasks);
    }

    @Test(expected = JobException.class)
    public void whenTasksIsEmpty() {
        new Job(0, 0, 0, 0.0, new ArrayList<>());
    }

    @Test(expected = NullPointerException.class)
    public void whenTasksIsNull() {
        new Job(0, 0, 0, 0.0, null);
    }
}