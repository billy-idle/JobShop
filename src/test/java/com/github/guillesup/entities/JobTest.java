package com.github.guillesup.entities;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class JobTest {

    @Test(expected = JobException.class)
    public void idLessThanZero() {
        Job.createJob(-1, 0, 800, 1.0, 0);
    }

    @Test(expected = JobException.class)
    public void startTimeLessThanZero() {
        Job.createJob(1, -1, 800, 1.0, 0);
    }

    @Test(expected = JobException.class)
    public void dueDateLessThanZero() {
        Job.createJob(1, 0, -1, 1.0, 0);
    }

    @Test(expected = JobException.class)
    public void weightLessThanZero() {
        Job.createJob(1, 0, 800, -1.0, 0);
    }

    @Test(expected = JobException.class)
    public void numberOfTasksLessThanZero() {
        Job.createJob(1, 0, 800, 1.0, -1);
    }

    @Test
    public void testPublicGetters() {
        Job job = Job.createDummyJob();

        assertTrue(job.getId() == 0 &&
                job.getNumberOfTasks() == 0 &&
                job.getDueDate() == 0 &&
                job.getStartTime() == 0 &&
                job.getWeight() == 0.0);
    }

    @Test
    public void differentJobsSameHash() {
        assertEquals(Job.createDummyJob().hashCode(), Job.createDummyJob().hashCode());
    }

    @Test
    public void jobEqualsToItself() {
        Job job = Job.createDummyJob();
        assertEquals(job, job);
    }

    @Test
    public void jobNotEqualsToNull() {
        assertNotEquals(Job.createDummyJob(), null);
    }

    @Test
    public void notInstanceOfJob() {
        assertNotEquals(Job.createDummyJob(), new Object());
    }

    @Test
    public void differentJobsButSameContent() {
        assertEquals(Job.createDummyJob(), Job.createDummyJob());
    }

    @Test
    public void jobsAreNotEqual() {
        Job jobOne = Job.createJob(1, 1, 1, 1.0, 1);
        Job jobTwo = Job.createDummyJob();
        assertNotEquals(jobOne, jobTwo);
    }

    @Test
    public void testCompareTo() {
        int startTime = 0;
        int numberOfTasks = 10;

        Job jobOne = Job.createJob(1, startTime, 950, 5.0, numberOfTasks);
        Job jobTwo = Job.createJob(2, startTime, 800, 2.0, numberOfTasks);
        Job jobThree = Job.createJob(3, startTime, 600, 4.0, numberOfTasks);
        Job jobFour = Job.createJob(4, startTime, 300, 3.0, numberOfTasks);
        Job jobFive = Job.createJob(5, startTime, 500, 3.0, numberOfTasks);
        Job jobSix = Job.createJob(6, startTime, 400, 3.0, numberOfTasks);
        Job jobSeven = Job.createJob(7, startTime, 600, 4.0, numberOfTasks);

        List<Job> jobs = new ArrayList<>();

        jobs.add(jobOne);
        jobs.add(jobTwo);
        jobs.add(jobThree);
        jobs.add(jobFour);
        jobs.add(jobFive);
        jobs.add(jobSix);
        jobs.add(jobSeven);

        jobs.sort(Collections.reverseOrder());

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