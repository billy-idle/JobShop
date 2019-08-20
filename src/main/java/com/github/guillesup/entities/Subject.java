package com.github.guillesup.entities;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

public interface Subject {
    void registerObserver(Observer o);

    void removeObserver(Observer o);

    void notifyObservers(Graph<Task, DefaultWeightedEdge> directedWeightedGraph);
}
