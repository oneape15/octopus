package com.oneape.octopus.datasource.data;

import com.oneape.octopus.datasource.DataType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Value implements Serializable {
    /**
     * 数据类型
     */
    private DataType dataType;
    /**
     * 具体值
     */
    private Object value;

    public Value(Object value, DataType dt) {
        this.value = value;
        this.dataType = dt;
    }
}
