package com.oneape.octopus.commons.value;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-07-07 23:24.
 * Modify:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TitleValueDTO<K, V> implements Serializable {
    private K title;
    private V value;
}
