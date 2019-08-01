package com.github.guillesup.entities;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class JobTest {
    private List<Task> tasks;

    @Before
    public void setUp() {
        createEightTasks();
    }

    private void createEightTasks() {
        List<Machine> machines = new ArrayList<>(3);

        machines.add(new Machine(1));
        machines.add(new Machine(2));
        machines.add(new Machine(3));

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

    @Test(expected = JobException.class)
    public void idLessThanZero() {
        new Job(-1, 0, 800, 1.0, this.tasks);
    }

    @Test(expected = JobException.class)
    public void idEqualToZero() {
        new Job(0, 0, 800, 1.0, this.tasks);
    }

    @Test(expected = JobException.class)
    public void startTimeLessThanZero() {
        new Job(1, -1, 800, 1.0, this.tasks);
    }

    @Test(expected = JobException.class)
    public void dueDateLessThanZero() {
        new Job(1, 0, -1, 1.0, this.tasks);
    }

    @Test(expected = JobException.class)
    public void weightLessThanZero() {
        new Job(1, 0, 800, -1.0, this.tasks);
    }

    @Test(expected = JobException.class)
    public void weightEqualToZero() {
        new Job(1, 0, 800, 0.0, this.tasks);
    }

    @Test(expected = JobException.class)
    public void whenTasksIsEmpty() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(1, new Machine(1), 1));
        tasks.clear();

        new Job(1, 0, 800, 1.0, tasks);
    }

    @Test(expected = NullPointerException.class)
    public void tasksIsNull() {
        new Job(0, 0, 0, 0.0, null);
    }

    @Test
    public void differentJobsSameHash() {
        assertEquals(createDefaultJob().hashCode(), createDefaultJob().hashCode());
    }

    private Job createDefaultJob() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(1, new Machine(1), 1));

        return new Job(1, 0, 800, 1.0, tasks);
    }

    @Test
    public void testGetters() {
        Job job = createDefaultJob();
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(1, new Machine(1), 1));

        assertTrue(job.getId() == 1 &&
                job.getStartTime() == 0 &&
                job.getDueDate() == 800 &&
                Double.compare(job.getWeight(), 1.0) == 0 &&
                job.getTasks().equals(tasks) &&
                job.getNumberOfTasks() == 1);
    }

    @Test
    public void jobEqualsToItself() {
        Job job = createDefaultJob();
        assertEquals(job, job);
    }

    @Test
    public void jobNotEqualsToNull() {
        assertNotEquals(createDefaultJob(), null);
    }

    @Test
    public void notInstanceOfJob() {
        assertNotEquals(createDefaultJob(), new Object());
    }

    @Test
    public void differentJobsButSameContent() {
        assertEquals(createDefaultJob(), createDefaultJob());
    }

    @Test
    public void jobsAreNotEqual() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(2, new Machine(1), 1));
        Job job = new Job(1, 0, 800, 1.0, tasks);

        assertNotEquals(createDefaultJob(), job);
    }

    @Test
    public void testCompareTo() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(2, new Machine(1), 1));

        Job jobOne = new Job(1, 0, 950, 5.0, tasks);
        Job jobTwo = new Job(2, 0, 800, 2.0, tasks);
        Job jobThree = new Job(3, 0, 600, 4.0, tasks);
        Job jobFour = new Job(4, 0, 300, 3.0, tasks);
        Job jobFive = new Job(5, 0, 500, 3.0, tasks);
        Job jobSix = new Job(6, 0, 400, 3.0, tasks);
        Job jobSeven = new Job(7, 0, 600, 4.0, tasks);

        List<Job> jobs = new ArrayList<>();

        jobs.add(jobOne);
        jobs.add(jobTwo);
        jobs.add(jobThree);
        jobs.add(jobFour);
        jobs.add(jobFive);
        jobs.add(jobSix);
        jobs.add(jobSeven);

        List<Integer> expectedList;
        expectedList = Arrays.asList(1, 3, 7, 4, 6, 5, 2);

        jobs.sort(Collections.reverseOrder());

        List<Integer> orderedList = new ArrayList<>();

        for (Job job : jobs) {
            orderedList.add(job.getId());
        }

        assertEquals(expectedList, orderedList);
    }
}