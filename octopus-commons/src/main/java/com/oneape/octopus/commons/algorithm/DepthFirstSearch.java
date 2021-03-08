package com.oneape.octopus.commons.algorithm;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-06-28 21:05.
 * Modify:
 */
public class DepthFirstSearch<T> {

    private final boolean[] marked;
    private int       count;

    public DepthFirstSearch(Graph<T> graph, T s) {
        marked = new boolean[graph.V()];
        dfs(graph, s);
    }

    private void dfs(Graph<T> graph, T v) {
        marked[graph.getVertexIndex(v)] = true;
        count++;
        for (T w : graph.adj(v)) {
            int index = graph.getVertexIndex(w);
            if (!marked[index]) {
                dfs(graph, w);
            }
        }
    }

    public int count() {
        return count;
    }
}
