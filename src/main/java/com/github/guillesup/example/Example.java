package com.github.guillesup.example;

import com.github.guillesup.interactors.BenchmarkReport;
import com.github.guillesup.interactors.JobShop;

public class Example {
    public static void main(String[] args) {
        var scheduledBenchmark = JobShop.schedule("b101");
        var br = BenchmarkReport.getInstance(scheduledBenchmark);
        System.out.println(br.getReport());
        System.out.println(br.toDot());
        // Optional
        br.saveDotToDisk();
    }
}