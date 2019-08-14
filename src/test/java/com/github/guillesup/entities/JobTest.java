package com.github.guillesup.entities;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class JobTest {
    private Job job;

    @Before
    public void setUp() {
        job = Job.getInstance();
    }

    @Test(expected = JobException.class)
    public void startTimeLessThanZero() {
        job.createJob(-1, 800, 1.0, 0);
    }

    @Test(expected = JobException.class)
    public void dueDateLessThanZero() {
        job.createJob(0, -1, 1.0, 0);
    }

    @Test(expected = JobException.class)
    public void weightLessThanZero() {
        job.createJob(0, 800, -1.0, 0);
    }

    @Test(expected = JobException.class)
    public void numberOfTasksLessThanZero() {
        job.createJob(0, 800, 1.0, -1);
    }

    @Test
    public void testPublicGetters() {
        assertTrue(job.getId() == 0 &&
                job.getNumberOfTasks() == 0 &&
                job.getDueDate() == 0 &&
                job.getStartTime() == 0 &&
                job.getWeight() == 0.0);
    }

    @Test
    public void differentJobsSameHash() {
        assertEquals(Job.getFictiveJob().hashCode(), Job.getInstance().hashCode());
    }

    @Test
    public void jobEqualsToItself() {
        assertEquals(job, job);
    }

    @Test
    public void jobNotEqualsToNull() {
        assertNotEquals(job, null);
    }

    @Test
    public void notInstanceOfJob() {
        assertNotEquals(job, new Object());
    }

    @Test
    public void differentJobsButSameContent() {
        assertEquals(Job.getFictiveJob(), Job.getInstance());
    }

    @Test
    public void jobsAreNotEqual() {
        Job jobOne = job.createJob(1, 1, 1.0, 1);
        Job jobTwo = job.getInstance();
        assertNotEquals(jobOne, jobTwo);
    }

    @Test
    public void testCompareTo() {
        int startTime = 0;
        int numberOfTasks = 10;

        Job jobOne = job.createJob(startTime, 950, 5.0, numberOfTasks);
        Job jobTwo = job.createJob(startTime, 800, 2.0, numberOfTasks);
        Job jobThree = job.createJob(startTime, 600, 4.0, numberOfTasks);
        Job jobFour = job.createJob(startTime, 300, 3.0, numberOfTasks);
        Job jobFive = job.createJob(startTime, 500, 3.0, numberOfTasks);
        Job jobSix = job.createJob(startTime, 400, 3.0, numberOfTasks);
        Job jobSeven = job.createJob(startTime, 600, 4.0, numberOfTasks);

        List<Job> jobs = job.getSortedJobList();

        List<Job> expectedList;

        expectedList =
                Arrays.asList(
                        jobOne,
                        jobThree,
                        jobSeven,
                        jobFour,
                        jobSix,
                        jobFive,
                        jobTwo);

        assertEquals(expectedList, jobs);
    }
}