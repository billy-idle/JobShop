package com.github.guillesup.entities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TaskTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test(expected = TaskException.class)
    public void whenTimeIsNegative() {
        new Task(new Machine(1), -1);
    }

    @Test(expected = TaskException.class)
    public void whenMachineIsNull() {
        new Task(null, 1);
    }

    @After
    public void tearDown() throws Exception {
    }
}