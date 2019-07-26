package com.github.guillesup.entities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ProblemTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test(expected = ProblemException.class)
    public void whenJobsIsEmpty(){
        new Problem(new ArrayList<Job>());
    }

    @After
    public void tearDown() throws Exception {
    }
}