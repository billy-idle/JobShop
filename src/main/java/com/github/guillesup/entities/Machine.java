package com.github.guillesup.entities;

public class Machine {
    private int id;

    public Machine(int id) {
        this.id = id;
        assesInput();
    }

    private void assesInput() {
        if (this.id < 0) {
            throw new MachineException("Machine id must be greater than or equal to zero");
        }
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
