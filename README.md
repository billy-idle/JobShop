# Job-Shop Scheduling Problem

## 1. Description

In the classical Job-Shop problem there are _n_ jobs that must be processed on _m_ machines. Each job consists of a sequence of different tasks _(operations)_. Each task needs to be processed during an _uninterrupted_ period of time on a given machine.

Given a set _J_ of jobs, a set _M_ of machines and a set _T_ of tasks, we denote by _τ<sub>j</sub>_ the number of tasks for a given job _j_ ∈ _J_. To each task _a<sub>ij</sub>_ corresponds an ordered pair _(m<sub>ij</sub>, p<sub>ij</sub>)_: the task _a<sub>ij</sub>_ needs to be processed on machine _m<sub>ij</sub>_ ∈ _M_ for a period of _p<sub>ij</sub>_ units of time.

## 2. How to use it

This approach looks for a simple and unoptimized solution (schedule) to the well-known Singer & Pinedo benchmark-set.

config.properties contains the path to the benchmark-set.

```properties
#JobShop Properties
#Wed Sep 04 08:10:22 COT 2019
path=benchmark-set
```

path (either relative or absolute) where the .txt files are stored.

Scheduling and exporting to dot format:

```java
package com.github.guillesup.example;

import com.github.guillesup.interactors.BenchmarkReport;
import com.github.guillesup.interactors.JobShop;

public class Example {
    public static void main(String[] args) {
        var scheduledBenchmark = JobShop.schedule("b101");
        System.out.println(BenchmarkReport.getInstance(scheduledBenchmark).toDot());
        
        // Also you can save it.
        BenchmarkReport.getInstance(scheduledBenchmark).saveDotToDisk();
    }
}
```

dot format:

```dot
strict digraph b101 {
	graph [center=1 rankdir=LR ordering=out ratio=auto nodesep=0 ranksep=1]
	node [shape=circle height=0.7 fixedsize=true]
	9 [ label="3:1" ];
	8 [ label="2:1" ];
	7 [ label="1:1" ];
	6 [ label="1:2" ];
	5 [ label="3:2" ];
	4 [ label="2:2" ];
	3 [ label="2:3" ];
	2 [ label="1:3" ];
	1 [ label="3:3" ];
	0 [ label="Init" ];
	-1 [ label="End" ];

	subgraph {
		0 -> 1 [ label=0 ]
		0 -> 4 [ label=0 ]
		0 -> 7 [ label=0 ]
	}

	subgraph {
		3 -> -1 [ label=5 ]
		6 -> -1 [ label=3 ]
		9 -> -1 [ label=5 ]
	}

	subgraph cluster_0 {
		subgraph cluster_1 {
			label="Job 1"
			7 -> 8 [label=2];
			8 -> 9 [label=4];
		}

		subgraph cluster_2 {
			label="Job 2"
			4 -> 5 [label=4];
			5 -> 6 [label=2];
		}

		subgraph cluster_3 {
			label="Job 3"
			1 -> 2 [label=5];
			2 -> 3 [label=4];
		}
	}

	/* Critical Path */
	{
		0 -> 1 -> 2 -> 3 -> -1 [color=red]
	}

	/* Conjunctive Edges of machine 1 */
	{
		edge[fontcolor=cornflowerblue color=cornflowerblue style=dashed]
		2 -> 6 [label=4];
		7 -> 2 [label=2];
	}

	/* Conjunctive Edges of machine 2 */
	{
		edge[fontcolor=gold2 color=gold2 style=dashed]
		8 -> 3 [label=4];
		4 -> 8 [label=4];
	}

	/* Conjunctive Edges of machine 3 */
	{
		edge[fontcolor=magenta color=magenta style=dashed]
		1 -> 5 [label=5];
		5 -> 9 [label=2];
	}
}
```
Scheduling and reporting to the console:

```java
package com.github.guillesup.example;

import com.github.guillesup.interactors.BenchmarkReport;
import com.github.guillesup.interactors.JobShop;

public class Example {
    public static void main(String[] args) {
        var scheduledBenchmark = JobShop.schedule("b101");
        System.out.println(BenchmarkReport.getInstance(scheduledBenchmark).getReport());
    }
}
```

benchmark report:

```console
------------------------------------
Benchmark
------------------------------------
	Id: b101.txt
      Jobs: 3
  Machines: 3
     Tasks: 9
Is acyclic: Yes

------------------------------------
Critical Path
------------------------------------
Weight: 14
Length: 4
  Path: [Init, 3:3, 1:3, 2:3, End]

+------------+----------+----------+ 
| Task (m:j) | End time | Due date |
+------------+----------+----------+ 
|        3:1 | 13       | 10       |
|        1:2 | 12       | 11       |
|        2:3 | 14       | 16       |
+------------+----------+----------+  
```

b101.dot to PNG

![alt text][b101.png]

[b101.png]: b101.png "b101.png"
