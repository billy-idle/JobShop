package com.github.guillesup.interactors;

import com.github.guillesup.entities.DummyTask;
import com.github.guillesup.entities.Job;
import com.github.guillesup.entities.Problem;
import com.github.guillesup.entities.Task;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class ProblemLoaderTest {
    private List<Problem> problems;

    @Before
    public void setUp() throws Exception {
        setPath("problems\\Small & Testable");
        problems = ProblemLoader.getProblems();
    }

    private void setPath(String path) throws IOException {
        final var props = new Properties();

        try (var in = new FileInputStream("config.properties")) {
            props.load(in);
        }

        props.setProperty("path", path);

        try (var out = new FileOutputStream("config.properties")) {
            props.store(out, "JobShop Properties");
        }
    }

    @After
    public void tearDown() throws Exception {
        setPath("problems");
    }

    @Test
    public void problemsAreEquals() throws Exception {
        var problemOne = createTestProblem();
        var problemTwo = problems.get(0);
        assertEquals(problemOne, problemTwo);
    }

    private Problem createTestProblem() {
        final String problemId = "problems\\Small & Testable\\problem.txt";
        final int totalJobs = 3;
        final int totalMachines = 3;
        final int totalTasks = 9;

        final var settings = new ArrayList<Integer>(3);
        settings.add(totalJobs);
        settings.add(totalMachines);
        settings.add(totalTasks);

        return Problem.createProblem(problemId, settings, createGraph());
    }

    private Graph<Task, DefaultEdge> createGraph() {
        var jobOne = Job.createJob(1, 0, 16, 1.0, 3);
        var jobTwo = Job.createJob(2, 0, 11, 2.0, 3);
        var jobThree = Job.createJob(3, 0, 10, 4.0, 3);

        var taskOne = Task.createTask(11, 3, 5, jobOne);
        var taskTwo = Task.createTask(12, 1, 4, jobOne);
        var taskThree = Task.createTask(13, 2, 5, jobOne);

        var taskFour = Task.createTask(21, 2, 4, jobTwo);
        var taskFive = Task.createTask(22, 3, 2, jobTwo);
        var taskSix = Task.createTask(23, 1, 3, jobTwo);

        var taskSeven = Task.createTask(31, 1, 2, jobThree);
        var taskEight = Task.createTask(32, 2, 4, jobThree);
        var taskNine = Task.createTask(33, 3, 5, jobThree);

        var initTask = DummyTask.getInitDummyTask();
        var endTask = DummyTask.getEndDummyTask();

        Graph<Task, DefaultEdge> edgeGraph = new DefaultDirectedGraph<>(DefaultEdge.class);

        edgeGraph.addVertex(initTask);
        edgeGraph.addVertex(endTask);
        edgeGraph.addVertex(taskOne);
        edgeGraph.addVertex(taskTwo);
        edgeGraph.addVertex(taskThree);
        edgeGraph.addVertex(taskFour);
        edgeGraph.addVertex(taskFive);
        edgeGraph.addVertex(taskSix);
        edgeGraph.addVertex(taskSeven);
        edgeGraph.addVertex(taskEight);
        edgeGraph.addVertex(taskNine);

        Graphs.addEdgeWithVertices(edgeGraph, initTask, taskOne);
        Graphs.addEdgeWithVertices(edgeGraph, taskOne, taskTwo);
        Graphs.addEdgeWithVertices(edgeGraph, taskTwo, taskThree);
        Graphs.addEdgeWithVertices(edgeGraph, taskThree, endTask);

        Graphs.addEdgeWithVertices(edgeGraph, initTask, taskFour);
        Graphs.addEdgeWithVertices(edgeGraph, taskFour, taskFive);
        Graphs.addEdgeWithVertices(edgeGraph, taskFive, taskSix);
        Graphs.addEdgeWithVertices(edgeGraph, taskSix, endTask);

        Graphs.addEdgeWithVertices(edgeGraph, initTask, taskSeven);
        Graphs.addEdgeWithVertices(edgeGraph, taskSeven, taskEight);
        Graphs.addEdgeWithVertices(edgeGraph, taskEight, taskNine);
        Graphs.addEdgeWithVertices(edgeGraph, taskNine, endTask);

        return edgeGraph;
    }
}