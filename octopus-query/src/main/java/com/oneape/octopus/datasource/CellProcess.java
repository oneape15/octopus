package com.oneape.octopus.datasource;

/**
 * 数据列加工处理方法
 * @param <T>
 * @param <R>
 */
@FunctionalInterface
public interface CellProcess<T, R> {

    R process(T t);
}
