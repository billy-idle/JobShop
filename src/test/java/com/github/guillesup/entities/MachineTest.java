package com.github.guillesup.entities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MachineTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test(expected = MachineException.class)
    public void whenMachineIdLessThanZero(){
        new Machine(-1);
    }

    @After
    public void tearDown() throws Exception {
    }
}