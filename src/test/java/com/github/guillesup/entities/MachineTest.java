package com.github.guillesup.entities;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MachineTest {

    @Test
    public void testMachineIdEquals() {
        assertEquals(0, Machine.createMachine(0).getId());
    }

    @Test(expected = MachineException.class)
    public void testMachineIdLessThanZero() {
        Machine.createMachine(-1);
    }
}