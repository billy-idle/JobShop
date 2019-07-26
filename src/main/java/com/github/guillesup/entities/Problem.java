package com.github.guillesup.entities;

import java.util.List;

public class Problem {
    private List<Job> jobs;

    public Problem(List<Job> jobs) {
        this.jobs = jobs;
        assesInput();
    }

    private void assesInput() {
        if (this.jobs.isEmpty()) {
            throw new ProblemException("jobs must not be empty");
        }
    }

    public List<Job> getJobs() {
        return jobs;
    }
}

class ProblemException extends RuntimeException {
    ProblemException(String message) {
        super(message);
    }
}
