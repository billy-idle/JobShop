package com.github.guillesup.entities;

import org.junit.After;
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
    public void setUp() throws Exception {
        createThreeMachines();
        createEightTasks();
        //createThreeJobs();
    }

    private void createThreeMachines() {
        machines = new ArrayList<>(3);

        machines.add(new Machine(0));
        machines.add(new Machine(1));
        machines.add(new Machine(2));
    }

    private void createEightTasks() {
        tasks = new ArrayList<>(8);

        tasks.add(new Task(machines.get(0), 3));
        tasks.add(new Task(machines.get(1), 2));
        tasks.add(new Task(machines.get(2), 2));

        tasks.add(new Task(machines.get(0), 2));
        tasks.add(new Task(machines.get(2), 1));
        tasks.add(new Task(machines.get(1), 4));

        tasks.add(new Task(machines.get(1), 4));
        tasks.add(new Task(machines.get(2), 3));
    }

    /*private void createThreeJobs() {
        jobs = new ArrayList<>(3);

        jobs.add(new Job(0, tasks.subList(0, 3)));
        jobs.add(new Job(1, tasks.subList(3, 6)));
        jobs.add(new Job(2, tasks.subList(6, 8)));
    }*/

    @Test
    @Ignore
    public void printJobs() {
        StringBuilder actual = new StringBuilder();
        String expected =
                "Job 0 = [(0, 3), (1, 2), (2, 2)]\n" +
                "Job 1 = [(0, 2), (2, 1), (1, 4)]\n" +
                "Job 2 = [(1, 4), (2, 3)]\n";

        for (Job job : jobs) {
            actual.append(job.toString() + "\n");
        }

        assertEquals(expected, actual.toString());
    }

    @Test(expected = JobException.class)
    public void whenIdIsLessThanZero(){
        new Job(-1, 0,0, 0.0, this.tasks);
    }

    @Test(expected = JobException.class)
    public void whenStartTimeIsLessThanZero(){
        new Job(0, -1,0, 0.0, this.tasks);
    }

    @Test(expected = JobException.class)
    public void whenDueDateIsLessThanZero(){
        new Job(0, 0,-1, 0.0, this.tasks);
    }

    @Test(expected = JobException.class)
    public void whenIdWeightLessThanZero(){
        new Job(0, 0,0, -1.0, this.tasks);
    }

    @Test(expected = JobException.class)
    public void whenTasksIsEmpty(){
        new Job(0, 0,0, 0.0, new ArrayList<Task>());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getTasks() {
    }
}