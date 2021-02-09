package com.oneape.octopus.query;

/**
 * Data column processing method.
 *
 * @param <T>
 * @param <R>
 */
@FunctionalInterface
public interface CellProcess<T, R> {

    R process(T t);
}
