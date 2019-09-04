package com.github.guillesup.entities;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JobTest {

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
        Job job = Job.getFictiveJob();

        assertTrue(job.getId() == 0 &&
                job.getNumberOfTasks() == 0 &&
                job.getDueDate() == 0 &&
                job.getStartTime() == 0 &&
                job.getWeight() == 0.0);
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

        List<Job> jobList = new ArrayList<>();

        jobList.add(jobOne);
        jobList.add(jobTwo);
        jobList.add(jobThree);
        jobList.add(jobFour);
        jobList.add(jobFive);
        jobList.add(jobSix);
        jobList.add(jobSeven);

        jobList.sort(Collections.reverseOrder());

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

        assertEquals(expectedList, jobList);
    }
}