package com.github.guillesup.interactors;

import com.github.guillesup.entities.Benchmark;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class FileParserTest {
    private FileParser fileParser;
    private Benchmark benchmark;

    @Before
    public void setUp() throws Exception {
        setPath("benchmark-set\\Only for Unit Tests");
        this.fileParser = FileParser.getInstance();
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
    public void getBenchmarkSet() {
        benchmark = this.fileParser.getBenchmark();
    }
}