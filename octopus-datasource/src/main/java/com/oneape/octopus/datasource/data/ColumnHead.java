package com.oneape.octopus.datasource.data;

import com.oneape.octopus.datasource.DataType;
import lombok.Data;

import java.io.Serializable;

@Data
public class ColumnHead implements Serializable {
    /**
     * 列名称
     */
    private String name;
    /**
     * 数据类型
     */
    private DataType dataType;
}
