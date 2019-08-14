package com.github.guillesup.interactors;

import com.github.guillesup.entities.Benchmark;
import com.github.guillesup.entities.Task;
import org.jgrapht.Graphs;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Properties;

public class FlexibleJobShopTest {
    private FlexibleJobShop flexibleJobShop;

    @Before
    public void setUp() throws Exception {
        setPath("benchmark-set\\Only for Unit Tests");
        FileParser fileParser = FileParser.getInstance();
        Benchmark benchmark = fileParser.getBenchmark();
        this.flexibleJobShop = FlexibleJobShop.getInstance(benchmark);
        this.flexibleJobShop.schedule();
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
    public void schedule() {
    }

    @Test
    public void getGraph() {
        Benchmark benchmark = this.flexibleJobShop.getBenchmark();

        for (Benchmark bm : benchmark.getBenchmarkSet()) {

            var directedWeightedGraph = bm.getDirectedWeightedGraph();
//            System.out.println(directedWeightedGraph);

            var endTasks = Graphs.predecessorListOf(directedWeightedGraph, Task.getFictiveEndTask());
            endTasks.sort(Collections.reverseOrder());

            System.out.println(bm.getId());
            for (var task : endTasks) {
                System.out.println("Task: " + task + " End-Time: " + task.getEndTime());
            }
        }
    }
}