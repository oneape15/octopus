package com.oneape.octopus.commons.algorithm;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-06-29 15:32.
 * Modify:
 */
public class Digraph<T> extends Graph<T> {
    public Digraph(int V) {
        super(V, true);
    }
}
