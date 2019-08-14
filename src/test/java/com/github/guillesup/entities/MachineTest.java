package com.github.guillesup.entities;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MachineTest {
    private Machine machine;

    @Before
    public void setUp() throws Exception {
        machine = Machine.getInstance();
    }

    @Test
    public void testMachineId() {
        assertEquals(0, machine.getId());
    }
}