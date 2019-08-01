package com.github.guillesup.interactors;

import com.github.guillesup.entities.Job;
import com.github.guillesup.entities.Machine;
import com.github.guillesup.entities.Problem;
import com.github.guillesup.entities.Task;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProblemLoaderTest {
    private ProblemLoader problemLoader;
    private Problem testProblem;

    @Before
    public void setUp() {
        String workPath = "testWorkPath";
        problemLoader = new ProblemLoader(workPath);
        manuallyCreateTestProblem();
    }

    private void manuallyCreateTestProblem() {
        Machine machine1 = new Machine(1);
        Machine machine2 = new Machine(2);
        Machine machine3 = new Machine(3);

        Task task11 = new Task(11, machine3, 5);
        Task task12 = new Task(12, machine1, 4);
        Task task13 = new Task(13, machine2, 5);

        List<Task> tasksJob1 = new ArrayList<>(3);
        tasksJob1.add(task11);
        tasksJob1.add(task12);
        tasksJob1.add(task13);

        Job job1 = new Job(1, 0, 16, 1.0, tasksJob1);

        Task task21 = new Task(21, machine2, 4);
        Task task22 = new Task(22, machine3, 2);
        Task task23 = new Task(23, machine1, 3);

        List<Task> tasksJob2 = new ArrayList<>();
        tasksJob2.add(task21);
        tasksJob2.add(task22);
        tasksJob2.add(task23);

        Job job2 = new Job(2, 0, 11, 2.0, tasksJob2);

        Task task31 = new Task(31, machine1, 2);
        Task task32 = new Task(32, machine2, 4);
        Task task33 = new Task(33, machine3, 5);

        List<Task> tasksJob3 = new ArrayList<>(3);
        tasksJob3.add(task31);
        tasksJob3.add(task32);
        tasksJob3.add(task33);

        Job job3 = new Job(3, 0, 10, 4.0, tasksJob3);

        List<Integer> settings = new ArrayList<>(3);
        settings.add(3); // #Jobs
        settings.add(3); // #Machines
        settings.add(9); // #Tasks

        List<Job> jobs = new ArrayList<>(3);
        jobs.add(job1);
        jobs.add(job2);
        jobs.add(job3);

        testProblem = new Problem("testWorkPath\\testProblem.txt", settings, jobs);
    }

    @Test
    public void problemsAreEquals() {
        assertEquals(problemLoader.getProblems().get(0), testProblem);
    }

    @Test
    public void sameHashCode() {
        assertEquals(problemLoader.getProblems().get(0).hashCode(), testProblem.hashCode());
    }
}