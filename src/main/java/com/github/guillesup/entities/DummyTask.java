package com.github.guillesup.entities;

import java.util.Objects;

enum Order {
    INIT, END, NONE
}

public class DummyTask extends Task {
    private Order order;

    private DummyTask(int id, int machine, int time, Job job, Order order) {
        super(id, machine, time, job);
        this.order = order;
    }

    public static DummyTask getInitDummyTask() {
        return new DummyTask(0, 0, 0, Job.createDummyJob(), Order.INIT);
    }

    public static DummyTask getEndDummyTask() {
        return new DummyTask(1, 0, 0, Job.createDummyJob(), Order.END);
    }

    public static DummyTask createDummyTask() {
        return new DummyTask(0, 0, 0, Job.createDummyJob(), Order.NONE);
    }

    @Override
    public int hashCode() {
        return 501 + Objects.hashCode(this.order);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        if (o == this)
            return true;

        if (!(o instanceof DummyTask))
            return false;

        var otherTask = (DummyTask) o;

        return (this.order == otherTask.order);
    }

    @Override
    public String toString() {
        return "" + this.order;
    }
}
