package com.github.guillesup.entities;

import java.util.Objects;

/**
 * This class represents a machine in the Job-Shop problem.
 *
 * @author m.mcfly
 */
public final class Machine implements Comparable<Machine> {
    private static final Machine FICTIVE_MACHINE;

    static {
        FICTIVE_MACHINE = new Machine(0);
    }

    private int id;

    private Machine(int id) {
        this.id = id;
        assessInput();
    }

    private void assessInput() {
        if (this.id < 0) {
            throw new MachineException("Machine Id must be greater than or equal to zero");
        }
    }

    /**
     * Machine factory method.
     *
     * @param id int >= 0
     * @return A new machine instance.
     */
    public static Machine createMachine(int id) {
        return new Machine(id);
    }

    /**
     * {@link #id} int = 0.
     *
     * @return This fictive machine is only used by Task.FICTIVE_INIT_TASK and Task.FICTIVE_END_TASK.
     */
    public static Machine getFictiveMachine() {
        return FICTIVE_MACHINE;
    }

    /**
     * @return Machine's Id.
     */
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

/**
 * Use to throw exceptions related only with the Machine class.
 */
class MachineException extends RuntimeException {
    MachineException(String message) {
        super(message);
    }
}
