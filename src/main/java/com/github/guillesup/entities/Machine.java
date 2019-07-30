package com.github.guillesup.entities;

import java.util.Objects;

public class Machine {
    private int id;

    public Machine(int id) {
        this.id = id;
        assesInput();
    }

    private void assesInput() {
        if (this.id <= 0) {
            throw new MachineException("Machine id must be greater than zero");
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
        return (this.id == otherMachine.getId());
    }

    public int getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return String.valueOf(this.id);
    }
}

class MachineException extends RuntimeException {
    MachineException(String message) {
        super(message);
    }
}
