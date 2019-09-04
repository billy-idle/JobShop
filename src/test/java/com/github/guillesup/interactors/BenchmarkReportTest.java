package com.github.guillesup.interactors;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BenchmarkReportTest {
    private final String expectedReport;
    private final String expectedDotContent;
    private BenchmarkReport report;

    {
        this.expectedReport = "------------------------------------\n" +
                "Benchmark\n" +
                "------------------------------------\n" +
                "\t\tId: b101.txt\n" +
                "\t  Jobs: 3\n" +
                "  Machines: 3\n" +
                "\t Tasks: 9\n" +
                "Is acyclic: Yes\n" +
                "\n" +
                "------------------------------------\n" +
                "Critical Path\n" +
                "------------------------------------\n" +
                "Weight: 14\n" +
                "Length: 4\n" +
                "  Path: [Init, 3:3, 1:3, 2:3, End]\n" +
                "\n" +
                "+------------+----------+----------+ \n" +
                "| Task (m:j) | End time | Due date |\n" +
                "+------------+----------+----------+ \n" +
                "|        3:1 | 13       | 10       |\n" +
                "|        1:2 | 12       | 11       |\n" +
                "|        2:3 | 14       | 16       |\n" +
                "+------------+----------+----------+ \n";

        this.expectedDotContent = "strict digraph b101 {\n" +
                "\tgraph [center=1 rankdir=LR ordering=out ratio=auto nodesep=0 ranksep=1]\n" +
                "\tnode [shape=circle height=0.7 fixedsize=true]\n" +
                "\t9 [ label=\"3:1\" ];\n" +
                "\t8 [ label=\"2:1\" ];\n" +
                "\t7 [ label=\"1:1\" ];\n" +
                "\t6 [ label=\"1:2\" ];\n" +
                "\t5 [ label=\"3:2\" ];\n" +
                "\t4 [ label=\"2:2\" ];\n" +
                "\t3 [ label=\"2:3\" ];\n" +
                "\t2 [ label=\"1:3\" ];\n" +
                "\t1 [ label=\"3:3\" ];\n" +
                "\t0 [ label=\"Init\" ];\n" +
                "\t-1 [ label=\"End\" ];\n" +
                "\n" +
                "\tsubgraph {\n" +
                "\t\t0 -> 1 [ label=0 ]\n" +
                "\t\t0 -> 4 [ label=0 ]\n" +
                "\t\t0 -> 7 [ label=0 ]\n" +
                "\t}\n" +
                "\n" +
                "\tsubgraph {\n" +
                "\t\t3 -> -1 [ label=5 ]\n" +
                "\t\t6 -> -1 [ label=3 ]\n" +
                "\t\t9 -> -1 [ label=5 ]\n" +
                "\t}\n" +
                "\n" +
                "\tsubgraph cluster_0 {\n" +
                "\t\tsubgraph cluster_1 {\n" +
                "\t\t\tlabel=\"Job 1\"\n" +
                "\t\t\t7 -> 8 [label=2];\n" +
                "\t\t\t8 -> 9 [label=4];\n" +
                "\t\t}\n" +
                "\n" +
                "\t\tsubgraph cluster_2 {\n" +
                "\t\t\tlabel=\"Job 2\"\n" +
                "\t\t\t4 -> 5 [label=4];\n" +
                "\t\t\t5 -> 6 [label=2];\n" +
                "\t\t}\n" +
                "\n" +
                "\t\tsubgraph cluster_3 {\n" +
                "\t\t\tlabel=\"Job 3\"\n" +
                "\t\t\t1 -> 2 [label=5];\n" +
                "\t\t\t2 -> 3 [label=4];\n" +
                "\t\t}\n" +
                "\t}\n" +
                "\n" +
                "\t/* Critical Path */\n" +
                "\t{\n" +
                "\t\t0 -> 1 -> 2 -> 3 -> -1 [color=red]\n" +
                "\t}\n" +
                "\n" +
                "\t/* Conjunctive Edges of machine 1 */\n" +
                "\t{\n" +
                "\t\tedge[fontcolor=cornflowerblue color=cornflowerblue style=dashed]\n" +
                "\t\t2 -> 6 [label=4];\n" +
                "\t\t7 -> 2 [label=2];\n" +
                "\t}\n" +
                "\n" +
                "\t/* Conjunctive Edges of machine 2 */\n" +
                "\t{\n" +
                "\t\tedge[fontcolor=gold2 color=gold2 style=dashed]\n" +
                "\t\t4 -> 8 [label=4];\n" +
                "\t\t8 -> 3 [label=4];\n" +
                "\t}\n" +
                "\n" +
                "\t/* Conjunctive Edges of machine 3 */\n" +
                "\t{\n" +
                "\t\tedge[fontcolor=magenta color=magenta style=dashed]\n" +
                "\t\t1 -> 5 [label=5];\n" +
                "\t\t5 -> 9 [label=2];\n" +
                "\t}\n" +
                "}";
    }


    @Before
    public void setUp() throws Exception {
        var scheduledBenchmark = JobShop.schedule("b101");
        this.report = BenchmarkReport.getInstance(scheduledBenchmark);
    }

    @Test
    @Ignore
    public void testReport() {
        assertEquals(this.expectedReport, report.getReport());
    }

    @Test
    @Ignore
    public void testDotContent() {
        assertEquals(this.expectedDotContent, this.report.toDot());
    }

}