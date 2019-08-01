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
        Machine machine = createDefaultMachine();
        assertEquals(machine, machine);
    }

    private Machine createDefaultMachine() {
        return new Machine(1);
    }

    @Test
    public void machineNotEqualsToNull() {
        assertNotEquals(createDefaultMachine(), null);
    }

    @Test
    public void notInstanceOfMachine() {
        Machine machineOne = createDefaultMachine();
        assertNotEquals(machineOne, new Object());
    }

    @Test
    public void differentMachinesButSameContent() {
        assertEquals(createDefaultMachine(), createDefaultMachine());
    }

    @Test
    public void machineOneNotEqualToMachineTwo() {
        assertNotEquals(new Machine(1), new Machine(2));
    }

    @Test
    public void sameHashCode() {
        assertEquals(createDefaultMachine().hashCode(),
                createDefaultMachine().hashCode());
    }

    @Test
    public void canGetId() {
        int id = 5;
        Machine machine = new Machine(id);
        assertEquals(machine.getId(), id);
    }
}