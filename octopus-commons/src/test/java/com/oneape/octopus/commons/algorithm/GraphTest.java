package com.oneape.octopus.commons.algorithm;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-06-29 11:02.
 * Modify:
 */
public class GraphTest {
    public static void main(String[] args) {
        Graph<Integer> graph = new Graph<>(13);
        graph.addEdge(0, 5);
        graph.addEdge(4, 3);
        graph.addEdge(0, 1);
        graph.addEdge(9, 12);
        graph.addEdge(6, 4);
        graph.addEdge(5, 4);
        graph.addEdge(0, 2);
        graph.addEdge(11, 12);
        graph.addEdge(9, 10);
        graph.addEdge(0, 6);
        graph.addEdge(7, 8);
        graph.addEdge(9, 11);
        graph.addEdge(5, 3);

        System.out.println(graph);
        System.out.println("max Degree:" + graph.maxDegree());
        System.out.println("avg Degree:" + graph.avgDegree());
    }
}
