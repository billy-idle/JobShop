package com.github.guillesup.interactors;

import com.github.guillesup.entities.Problem;

import java.util.List;

public class ProblemLoader {

    public static List<Problem> getProblems() {
        FileParser fileParser = FileParser.getInstance();
        return fileParser.getProblems();
    }
}