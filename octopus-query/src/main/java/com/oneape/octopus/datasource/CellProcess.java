package com.oneape.octopus.datasource;

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
