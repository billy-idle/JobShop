package com.github.guillesup.interactors;

import java.util.ArrayList;
import java.util.List;

public class Example {
    public static void main(String[] args) {
        var benchmarkList = FileParser.getInstance().getBenchmarkList();

        List<JobShop> jobShopList = new ArrayList<>();

        for (var benchmark : benchmarkList) {
            jobShopList.add(JobShop.getInstance(benchmark));
        }

        jobShopList.parallelStream().forEach(JobShop::schedule);

        List<BenchmarkReport> benchmarkReportList = new ArrayList<>();

        for (var benchmark : benchmarkList) {
            benchmarkReportList.add(BenchmarkReport.createReport(benchmark));
        }

        benchmarkReportList.forEach(t -> System.out.println(t.getReport()));
    }
}