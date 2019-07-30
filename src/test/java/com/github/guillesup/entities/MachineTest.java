package com.github.guillesup.entities;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MachineTest {

    @Test(expected = MachineException.class)
    public void whenMachineIdLessThanZero() {
        new Machine(-1);
    }

    @Test
    public void machineEqualsToItself() {
        Machine machine = new Machine(1);
        assertEquals(machine, machine);
    }

    @Test
    public void machineNotEqualsToNull() {
        Machine machine = new Machine(1);
        assertNotEquals(machine, null);
    }

    @Test
    public void objectIsInstanceOfMachineButNotEquals() {
        Machine machineOne = new Machine(1);
        Machine machineTwo = new Machine(2);
        assertNotEquals(machineOne, (Object) machineTwo);
    }

    @Test
    public void machineOneEqualToMachineTwo() {
        Machine machineOne = new Machine(1);
        Machine machineTwo = new Machine(1);
        assertEquals(machineOne, machineTwo);
    }

    @Test
    public void sameHashCode() {
        Machine machineOne = new Machine(1);
        Machine machineTwo = new Machine(1);
        assertEquals(machineOne.hashCode(), machineTwo.hashCode());
    }
}