package com.oneape.octopus.datasource.schema;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 表信息
 */
@Data
@NoArgsConstructor
public class TableInfo implements Serializable {
    /**
     * 表名称
     */
    private String name;
    /**
     * 数据表所属的数据库名
     */
    private String schema;
    /**
     * 表类型 0 - 基本表； 1 - 视图
     */
    private Integer type;
    /**
     * 描述信息
     */
    private String comment;


    public TableInfo(String name, String comment) {
        this.name = name;
        this.comment = comment;
    }
}
