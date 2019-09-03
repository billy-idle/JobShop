package com.github.guillesup.interactors;

import com.github.guillesup.entities.Benchmark;
import com.github.guillesup.entities.Task;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class GraphExporter {
    private final Map<Integer, String> colorMap = new HashMap<>();
    private Benchmark benchmark;
    private StringBuilder dotContent;

    {
        this.colorMap.put(1, "cornflowerblue");
        this.colorMap.put(2, "gold2");
        this.colorMap.put(3, "magenta");
        this.colorMap.put(4, "orange");
        this.colorMap.put(5, "green");
        this.colorMap.put(6, "cyan2");
        this.colorMap.put(7, "deeppink");
        this.colorMap.put(8, "olivedrab");
        this.colorMap.put(9, "lightsalmon");
        this.colorMap.put(10, "navy");
        this.colorMap.put(11, "red");
    }

    private GraphExporter(Benchmark benchmark) {
        this.benchmark = benchmark;
        this.dotContent = new StringBuilder();
    }

    public static GraphExporter getInstance(Benchmark benchmark) {
        return new GraphExporter(benchmark);
    }

    public void exportToDot() {
        appendTopLevelGraphStatement();
        appendGraphSettings();
        appendNodes();
        appendEdgesForFictiveNode(Task.getFictiveInitTask());
        appendEdgesForFictiveNode(Task.getFictiveEndTask());
        appendClusterByJob();
        appendCriticalPath();
        appendDisjunctiveEdges();
        createFile();
    }

    private void appendTopLevelGraphStatement() {
        this.dotContent.
                append("strict digraph ").
                append(this.benchmark.getId().replace(".txt", "")
                        .replace("-", "").
                                replace(".", "")).
                append(" {");
    }

    private void appendGraphSettings() {
        this.dotContent.
                append("\n\tgraph [center=1 rankdir=LR ordering=out ratio=auto nodesep=0 ranksep=1]").
                append("\n\tnode [shape=circle height=0.7 fixedsize=true]\n");
    }

    private void appendNodes() {
        StringBuilder nodes = new StringBuilder();
        var taskList = this.benchmark.getTaskList();
        taskList.sort(Collections.reverseOrder());

        for (var t : taskList) {
            nodes.append("\t").
                    append(t.getId()).
                    append(" [ label=").
                    append('\"').
                    append(t.toJobShopNotation()).
                    append('\"').
                    append(" ];\n");
        }

        nodes.append("\t").append("0 [ label=\"Init\" ];\n");
        nodes.append("\t").append("-1 [ label=\"End\" ];\n");

        this.dotContent.append(nodes);
    }

    private void appendEdgesForFictiveNode(Task task) {
        this.dotContent.append("\n\tsubgraph {");
        var graph = this.benchmark.getDirectedWeightedGraph();
        var edgesForFictiveNode = graph.edgesOf(task);

        for (var ce : edgesForFictiveNode) {
            String ces = ce.toString().
                    replace(":", "->").
                    replace("(", "").
                    replace(")", "").
                    concat(" [ label=").
                    concat("" + Math.round(this.benchmark.getDirectedWeightedGraph().getEdgeWeight(ce))).
                    concat(" ]");

            this.dotContent.append("\n\t\t").append(ces);
        }

        this.dotContent.append("\n\t}\n");
    }

    private void appendClusterByJob() {
        var graph = this.benchmark.getDirectedWeightedGraph();
        var taskList = this.benchmark.getTaskList();
        int totalJobs = this.benchmark.getTotalJobs();
        StringBuilder cluster = new StringBuilder();

        this.dotContent.append("\n\tsubgraph cluster_0 {");

        for (int i = 1; i <= totalJobs; i++) {
            final int jobId = i;

            List<Task> tasksByJob = taskList.stream().
                    filter(task -> task.getJob().getId() == jobId).sorted().collect(Collectors.toList());

            cluster.append("\t\tsubgraph cluster_").append(jobId).append(" {").
                    append("\n\t\t\tlabel=").append("\"Job ").append(jobId).append("\"");

            for (int j = 0; j < tasksByJob.size() - 1; j++) {
                var source = tasksByJob.get(j);
                var target = tasksByJob.get(j + 1);
                cluster.append("\n\t\t\t").append(source).append(" -> ").append(target);
                double weight = graph.getEdgeWeight(graph.getAllEdges(source, target).stream().findFirst().get());
                cluster.append(" [label=").append(Math.round(weight)).append("];");
            }

            cluster.append("\n\t\t}\n\n");
        }

        this.dotContent.append("\n\t\t").append(cluster.toString().trim()).append("\n\t}");
    }

    private void appendCriticalPath() {
        String criticalPath = this.benchmark.getCriticalPath().getVertexList().
                toString().
                replace(",", " ->").
                replace("[", "").
                replace("]", "").
                trim();

        this.dotContent.append("\n\n\t{\n\t\t").append(criticalPath).
                append(" [color=").append(this.colorMap.get(11)).append("]").append("\n\t}");
    }

    private void appendDisjunctiveEdges() {
        int totalMachines = this.benchmark.getTotalMachines();
        var taskList = this.benchmark.getTaskList();
        var graph = this.benchmark.getDirectedWeightedGraph();
        graph.removeAllEdges(this.benchmark.getConjunctiveEdgesList());
        StringBuilder colorDisjunctiveEdge = new StringBuilder();

        for (int i = 1; i <= totalMachines; i++) {
            final int machineId = i;
            List<Task> tasksByMachine = taskList.stream().
                    filter(task -> task.getMachine().getId() == machineId).sorted(Collections.reverseOrder()).collect(Collectors.toList());

            Set<DefaultWeightedEdge> edgesOf = new HashSet<>();

            for (Task task : tasksByMachine) {
                edgesOf.addAll(graph.edgesOf(task));
            }

            colorDisjunctiveEdge.append("\n\t{\n\t\tedge[fontcolor=").append(colorMap.get(i)).
                    append(" color=").append(colorMap.get(i)).append(" style=dashed]\n\t");

            for (var edge : edgesOf) {
                var source = graph.getEdgeSource(edge);
                var target = graph.getEdgeTarget(edge);
                var weight = graph.getEdgeWeight(edge);

                colorDisjunctiveEdge.append("\t").append(source).append(" -> ").append(target).
                        append(" [label=").append(Math.round(weight)).append("];\n\t");
            }

            colorDisjunctiveEdge.append("}\n");
        }

        this.dotContent.append("\n").append(colorDisjunctiveEdge).append("}");
    }

    private void createFile() {
        String filename = this.benchmark.getId().replace(".txt", "")
                .replace("-", "").
                        replace(".", "").concat(".dot");

        try (var out = new PrintWriter(filename)) {
            out.println(this.dotContent);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getDotContent() {
        return this.dotContent.toString();
    }
}
