package com.github.guillesup.entities;

import com.github.guillesup.interactors.FileParser;
import com.github.guillesup.interactors.JobShop;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class BenchmarkTest {

    private final List<Integer> expectedEndTimeList = new ArrayList<>();
    private Benchmark benchmark;
    private Graph<Task, DefaultWeightedEdge> directedWeightedGraph;
    private JobShop jobShop;

    {
        this.expectedEndTimeList.add(1238);
        this.expectedEndTimeList.add(1259);
        this.expectedEndTimeList.add(1436);
        this.expectedEndTimeList.add(1336);
        this.expectedEndTimeList.add(1255);
        this.expectedEndTimeList.add(1317);
        this.expectedEndTimeList.add(1388);
        this.expectedEndTimeList.add(1371);
        this.expectedEndTimeList.add(1431);
        this.expectedEndTimeList.add(1485);
    }

    @Before
    public void setUp() throws Exception {
        setPath("benchmark-set\\Only for Unit Tests");
        this.jobShop = JobShop.getInstance(FileParser.getInstance().getBenchmarkList().get(0));
        this.jobShop.schedule();
        this.benchmark = jobShop.getBenchmark();
        this.directedWeightedGraph = benchmark.getDirectedWeightedGraph();
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
        setPath("benchmark-set");
    }

    @Test
    public void testMakespan() {
        assertEquals(1485, this.benchmark.getMakespan(), 0.01);
    }

    @Test
    public void testEndTimeValues() {
        this.directedWeightedGraph = this.benchmark.getDirectedWeightedGraph();
        List<Task> endTaskList = Graphs.predecessorListOf(directedWeightedGraph, Task.getFictiveEndTask());
        List<Integer> endTimeList = endTaskList.stream().map(Task::getEndTime).collect(Collectors.toList());
        Collections.sort(this.expectedEndTimeList);
        Collections.sort(endTimeList);
        assertEquals(this.expectedEndTimeList, endTimeList);
    }

    @Test
    public void testCriticalPath() {
        assertEquals("[0:0, 5:2, 5:6, 7:6, 7:10, 7:1, 7:2, 6:2, 2:2, 3:2, 3:5, 8:5, 8:6, 8:2, " +
                "8:1, 10:1, 10:6, 10:7, 10:10, 0:0]", this.benchmark.getCriticalPath().getVertexList().toString());
    }

    @Test
    public void getId() {
        assertEquals("benchmark-set\\Only for Unit Tests\\abz5-1.3.txt", this.benchmark.getId());
    }

    @Test
    public void getTotalJobs() {
        assertEquals(10, this.benchmark.getTotalJobs());
    }

    @Test
    public void getTotalMachines() {
        assertEquals(10, this.benchmark.getTotalMachines());
    }

    @Test
    public void getTotalTasks() {
        assertEquals(100, this.benchmark.getTotalTasks());
    }

    @Test
    public void testIsGraphAcyclic() {
        assertFalse(this.benchmark.isGraphAcyclic());
    }
}