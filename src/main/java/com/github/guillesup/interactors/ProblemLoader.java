package com.github.guillesup.interactors;

import com.github.guillesup.entities.Problem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ProblemLoader {
    private List<Problem> problems;
    private static final String PROBLEMS_DIRECTORY = "problems";
    private Path workPath;

    public ProblemLoader() {
        this.workPath = Paths.get(this.PROBLEMS_DIRECTORY);
    }

    public void problemFactory() {
        List<Path> paths = new ArrayList<>();
        List<String> lines;

        try (Stream<Path> pathStream = Files.walk(workPath).filter(p -> p.getFileName().toString().endsWith(".txt"))) {

            pathStream.forEach(paths::add);

            for (Path path : paths) {
                lines = Files.readAllLines(path);
                
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<Problem> getProblems() {
        return problems;
    }

    public String getProblemsDirectory() {
        return PROBLEMS_DIRECTORY;
    }

    public Path getWorkPath() {
        return workPath;
    }

}
