package com.github.guillesup.entities;

import java.util.*;

public final class Machine implements Comparable<Machine> {
    private static final Machine FICTIVE_MACHINE;

    static {
        FICTIVE_MACHINE = new Machine(0);
    }

    private final Map<Integer, Machine> machineMap;
    private int id;

    {
        this.machineMap = new HashMap<>();
    }

    private Machine(int id) {
        this.id = id;
        assessInput();
    }

    private void assessInput() {
        if (this.id < 0) {
            throw new MachineException("Machine Id must be greater than or equal to zero");
        }
    }

    private Machine() {

    }

    public static Machine getInstance() {
        return new Machine();
    }

    public static Machine getFictiveMachine() {
        return FICTIVE_MACHINE;
    }

    public List<Machine> getSortedMachineList() {
        Collection<Machine> values = machineMap.values();
        List<Machine> machineList = new ArrayList<>(values);
        Collections.sort(machineList);
        return machineList;
    }

    public Machine createMachine(int id) {
        machineMap.put(id, new Machine(id));
        return machineMap.get(id);
    }

    public int getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return 89 +
                Objects.hashCode(this.id) +
                Objects.hashCode(this.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        if (o == this)
            return true;

        if (!(o instanceof Machine))
            return false;

        var otherMachine = (Machine) o;

        return (this.id == otherMachine.id);
    }

    @Override
    public String toString() {
        return "m" + this.id;
    }

    @Override
    public int compareTo(Machine otherMachine) {
        if (this.id > otherMachine.id) {
            return 1;
        } else if (this.id < otherMachine.id) {
            return -1;
        }

        return 0;
    }
}

class MachineException extends RuntimeException {
    MachineException(String message) {
        super(message);
    }
}
