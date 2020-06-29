package com.oneape.octopus.commons.algorithm;

import java.util.Iterator;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-06-29 15:59.
 * Modify:
 */
public class DigraphTest {
    public static void main(String[] args) {
        Digraph<Integer> g = new Digraph<>(4);
        g.addEdge(0, 5);
        g.addEdge(5, 4);
        g.addEdge(4, 3);
        g.addEdge(3, 5);
        System.out.println(g);

        DirectedCycle<Integer> directedCycle = new DirectedCycle<>(g);
        if (directedCycle.hasCycle()) {
            Iterator<Integer> cycle = directedCycle.cycle();
            while (cycle.hasNext()) {
                Integer v = cycle.next();
                System.out.print(v + " ");
            }
        }
    }
}
