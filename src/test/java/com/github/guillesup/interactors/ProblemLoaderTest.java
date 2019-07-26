package com.github.guillesup.interactors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;

import static org.junit.Assert.*;

public class ProblemLoaderTest {
    ProblemLoader problemLoader;

    @Before
    public void setUp() throws Exception {
        this.problemLoader = new ProblemLoader();
        this.problemLoader.problemFactory();
    }


    @After
    public void tearDown() throws Exception {
    }
}