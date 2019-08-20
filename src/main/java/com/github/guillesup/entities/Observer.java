package com.github.guillesup.entities;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

public interface Observer {
    void update(Graph<Task, DefaultWeightedEdge> directedWeightedGraph);
}
