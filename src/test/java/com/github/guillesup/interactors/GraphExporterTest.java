package com.github.guillesup.interactors;

import com.github.guillesup.entities.Benchmark;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GraphExporterTest {

    private GraphExporter ge;
    private String expected;

    {
        this.expected = "strict digraph problem {\n" +
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
                "\t{\n" +
                "\t\t0 -> 1 -> 2 -> 3 -> -1 [color=red]\n" +
                "\t}\n" +
                "\n" +
                "\t{\n" +
                "\t\tedge[fontcolor=cornflowerblue color=cornflowerblue style=dashed]\n" +
                "\t\t2 -> 6 [label=4];\n" +
                "\t\t7 -> 2 [label=2];\n" +
                "\t}\n" +
                "\n" +
                "\t{\n" +
                "\t\tedge[fontcolor=gold2 color=gold2 style=dashed]\n" +
                "\t\t4 -> 8 [label=4];\n" +
                "\t\t8 -> 3 [label=4];\n" +
                "\t}\n" +
                "\n" +
                "\t{\n" +
                "\t\tedge[fontcolor=magenta color=magenta style=dashed]\n" +
                "\t\t1 -> 5 [label=5];\n" +
                "\t\t5 -> 9 [label=2];\n" +
                "\t}\n" +
                "}";
    }

    @Before
    public void setUp() throws Exception {
        var benchmark = getProblem();
        JobShop.getInstance(benchmark).schedule();
        this.ge = GraphExporter.getInstance(benchmark);
        this.ge.exportToDot();
    }

    private Benchmark getProblem() {
        return FileParser.getInstance().getBenchmarkList().
                stream().
                filter(t -> t.getId().contains("problem")).
                findFirst().orElseThrow();
    }

    @Test
    public void testToGraphviz() {
        assertEquals(this.expected, this.ge.getDotContent());
    }
}