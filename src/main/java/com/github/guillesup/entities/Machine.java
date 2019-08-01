package com.github.guillesup.entities;

import java.util.Objects;

public class Machine {
    private final int id;

    public Machine(int id) {
        this.id = id;
        assesInput();
    }

    private void assesInput() {
        if (this.id <= 0) {
            throw new MachineException();
        }
    }

    @Override
    public int hashCode() {
        return 31 * Objects.hashCode(this.id);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        if (o == this)
            return true;

        if (!(o instanceof Machine))
            return false;

        Machine otherMachine = (Machine) o;
        return (this.id == otherMachine.id);
    }

    int getId() {
        return this.id;
    }
}

class MachineException extends RuntimeException {
    MachineException() {
        super("Machine id must be greater than zero");
    }
}
