package com.oneape.octopus.datasource.data;

import com.oneape.octopus.datasource.DataType;
import lombok.Data;

import java.io.Serializable;

@Data
public class Value implements Serializable {
    /**
     * 数据类型
     */
    private DataType dataType;
    /**
     * 具体值
     */
    private Object value;
}
