package com.oneape.octopus.commons.algorithm;

import java.util.Iterator;
import java.util.Stack;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-06-29 15:37.
 * Modify:
 */
public class DirectedCycle<T> {

    private boolean[] marked;
    private int[]     edgeTo;
    // All vertices in a directed ring (if any).
    private Stack<T>  cycle;
    // All vertices on the stack of recursive calls.
    private boolean[] onStack;

    public DirectedCycle(Digraph<T> digraph) {
        onStack = new boolean[digraph.V()];
        edgeTo = new int[digraph.V()];
        marked = new boolean[digraph.V()];
        for (T v : digraph.vertexInfo()) {
            int vIndex = digraph.getVertexIndex(v);
            if (!marked[vIndex]) {
                dfs(digraph, v);
            }
        }
    }

    private void dfs(Digraph<T> digraph, T v) {
        int vIndex = digraph.getVertexIndex(v);
        onStack[vIndex] = true;
        marked[vIndex] = true;
        for (T w : digraph.adj(v)) {
            int wIndex = digraph.getVertexIndex(w);
            if (this.hasCycle()) {
                return;
            } else if (!marked[wIndex]) {
                edgeTo[wIndex] = vIndex;
                dfs(digraph, w);
            } else if (onStack[wIndex]) {
                cycle = new Stack<T>();
                for (int x = vIndex; x != wIndex; x = edgeTo[x]) {
                    cycle.push(digraph.getVertexByIndex(x));
                }
                cycle.push(w);
                cycle.push(v);
            }
            onStack[vIndex] = false;
        }
    }

    public boolean hasCycle() {
        return cycle != null;
    }

    public Iterator<T> cycle() {
        return cycle.iterator();
    }

}
