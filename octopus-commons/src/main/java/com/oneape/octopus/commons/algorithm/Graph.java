package com.oneape.octopus.commons.algorithm;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * graph implementation class.
 * <p>
 * 1. Parallel edges are not allowed.
 * <p>
 * Created by oneape<oneape15@163.com>
 * Created 2020-06-28 14:56.
 * Modify:
 */
public class Graph<T> {

    // Is it a directed graph.
    private final boolean directed;

    // The vertex size.
    private int            vertexSize;
    // The edge size.
    private int            edgeSize;
    // The graph adjacency list array.
    private Set<Integer>[] adj;
    // The vertex list.
    private List<T>        keys;

    public Graph(int V) {
        this(V, false);
    }

    /**
     * Create a undirected graph with V vertices but no edges.
     *
     * @param V int The vertices.
     */
    public Graph(int V, boolean directed) {
        this.directed = directed;

        this.vertexSize = V;
        this.edgeSize = 0;

        keys = new ArrayList<>();
        adj = new Set[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new HashSet<>();
        }
    }

    public int V() {
        return vertexSize;
    }

    public int E() {
        return edgeSize;
    }

    public List<T> vertexInfo() {
        return keys;
    }

    /**
     * Adds an edge to the diagram. v --> w
     *
     * @param v T
     * @param w T
     */
    public void addEdge(T v, T w) {
        int indexV = getVertexIndex(v, true);
        int indexW = getVertexIndex(w, true);

        adj[indexV].add(indexW);
        if (!directed) {
            adj[indexW].add(indexV);
        }

        edgeSize++;
    }

    /**
     * Gets the index of the vertex, if the vertex does not exist, adds it, then returns the index
     *
     * @param v T
     * @return int
     */
    public int getVertexIndex(T v) {
        return getVertexIndex(v, false);
    }

    private int getVertexIndex(T v, boolean addIfNotExist) {
        if (!keys.contains(v)) {
            if (addIfNotExist) {
                keys.add(v);
            } else {
                return -1;
            }
        }

        return keys.indexOf(v);
    }

    /**
     * Gets the vertex by index.
     *
     * @param index int
     * @return T
     */
    public T getVertexByIndex(int index) {
        if (index < 0 && keys.size() >= index) {
            throw new IndexOutOfBoundsException("The normal index is [ 0 ~ " + (keys.size() - 1) + "] , input index: " + index);
        }
        return keys.get(index);
    }

    /**
     * Gets all vertices adjacent to V.
     *
     * @param v T
     * @return List
     */
    public List<T> adj(T v) {
        int indexV = getVertexIndex(v);
        List<T> list = new ArrayList<>();

        // The vertex information does not exist.
        if (indexV < 0) {
            return list;
        }
        adj[indexV].forEach(wIndex -> list.add(keys.get(wIndex)));
        return list;
    }

    /**
     * Calculate the degree of V.
     * The vertex information does not exist return -1.
     *
     * @param v T
     * @return int
     */
    public int degree(T v) {
        int index = getVertexIndex(v);

        // The vertex information does not exist.
        if (index < 0) {
            return -1;
        }

        return adj(v).size();
    }

    /**
     * Calculate the maximum degree of all vertices
     *
     * @return int
     */
    public int maxDegree() {
        int max = 0;
        for (T v : keys) {
            int degreeSize = degree(v);
            if (max < degreeSize) {
                max = degreeSize;
            }
        }
        return max;
    }

    /**
     * Calculate the average degree of all vertices.
     *
     * @return double
     */
    public double avgDegree() {
        return 2 * edgeSize / vertexSize;
    }

    /**
     * Calculate the number of self loop.
     *
     * @return int
     */
    public int numberOfSelfLoops() {
        int count = 0;

        for (T v : keys) {
            for (T w : adj(v)) {
                if (v == w) {
                    count++;
                }
            }
        }


        if (directed) {
            return count;
        }

        // each edge is computed twice.
        return count / 2;
    }


    /**
     * A string representation of the adjacency list of graphs.
     *
     * @return String
     */
    @Override
    public String toString() {
        String s = this.getClass().getName() + " { \n" + vertexSize + " vertices, " + edgeSize + " edges\n";
        for (int i = 0; i < vertexSize; i++) {
            T v = getVertexByIndex(i);
            s += v + ": ";
            for (T w : this.adj(v)) {
                s += w + " ";
            }
            s += "\n";
        }
        s += "}\n";
        return s;
    }
}
