package com.oneape.octopus.datasource.data;

import com.oneape.octopus.datasource.DataType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ColumnHead implements Serializable {
    /**
     * 列名称
     */
    private String name;
    /**
     * 列昵称
     */
    private String label;
    /**
     * 数据类型
     */
    private DataType dataType;


    public ColumnHead(String name, String label) {
        this.name = name;
        this.label = label;
    }
}
