package com.github.guillesup.interactors;

import com.github.guillesup.entities.Problem;

import java.util.List;

public class FlexibleSolution {
    private final List<Problem> problems;

    public FlexibleSolution(List<Problem> problems) {
        this.problems = problems;
        sortProblems();
    }

    private void sortProblems() {
        for (Problem problem : this.problems) {
            problem.sort();
        }
    }


}
