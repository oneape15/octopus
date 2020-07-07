package com.oneape.octopus.commons.algorithm;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-07-07 11:44.
 * Modify:
 */
public class DirectedAcyclicTest {

    private static Digraph<String> buildDigraph() {
        Digraph<String> digraph = new Digraph<>(11);
        digraph.addEdge("C", "F");
        digraph.addEdge("A", "C");
        digraph.addEdge("C", "H");
        digraph.addEdge("B", "D");
        digraph.addEdge("B", "E");
        digraph.addEdge("E", "H");
        digraph.addEdge("H", "K");
        digraph.addEdge("G", "I");
        digraph.addEdge("G", "J");
        return digraph;
    }

    public static void main(String[] args) {
        Digraph<String> g = buildDigraph();
        System.out.println("有向无环图: " + g.toString());
        System.out.println("所有节点:" + g.vertexInfo());
        DirectedCycle<String> dc = new DirectedCycle<>(g);

    }
}
