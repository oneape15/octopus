package com.oneape.octopus.commons.algorithm;

import java.util.*;

/**
 * Directed acyclic graph.
 * Created by oneape<oneape15@163.com>
 * Created 2020-07-07 11:15.
 * Modify:
 */
public class DirectedAcyclicGraph<T> {

    // The dag vertex list.
    private Set<T>         vertex;
    // A set of pre vertices of vertices.
    private Map<T, Set<T>> map;

    public DirectedAcyclicGraph(Digraph<T> digraph) {
        vertex = new HashSet<>();
        map = new HashMap<>();
        build(digraph);
    }

    private void build(Digraph<T> digraph) {
        // Save the vertex list.
        for (T v : digraph.vertexInfo()) {
            if (vertex.contains(v)) {
                throw new IllegalArgumentException("There are identical vertex.");
            }
            vertex.add(v);
        }

        // Add the edge info.
        for (T v : digraph.vertexInfo()) {
            for (T w : digraph.adj(v)) {
                addEdge(v, w);
            }
        }
    }

    private void addEdge(T v, T w) {
        if (!vertex.contains(v) || !vertex.contains(w)) {
            throw new IllegalArgumentException("Directed graph edge information exception.");
        }

        Set<T> prevs = map.get(v);
        if (prevs == null) {
            prevs = new HashSet<>();
            map.put(v, prevs);
        }

        if (prevs.contains(w)) {
            throw new IllegalArgumentException("The edges of a directed graph with multiple duplicates.");
        }

        prevs.add(w);
    }

    public Set<T> getVertex() {
        return vertex;
    }

    public Set<T> getPrevs(T v) {
        return map.get(v);
    }


}
