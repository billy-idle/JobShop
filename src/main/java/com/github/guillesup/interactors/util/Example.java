package com.github.guillesup.interactors.util;

import com.github.guillesup.interactors.BenchmarkReport;
import com.github.guillesup.interactors.FileParser;

public class Example {
    public static void main(String[] args) {
        var benchmark = FileParser.getInstance().getBenchmark("abz5-1.3");
        benchmark.schedule();
        var benchmarkReport = BenchmarkReport.getInstance(benchmark);
        System.out.println(benchmarkReport.getReport());
        benchmarkReport.exportToDot();
    }
}