package com.oneape.octopus.datasource.schema;

import com.oneape.octopus.datasource.DataType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 表字段信息
 */
@Data
@NoArgsConstructor
public class FieldInfo implements Serializable {
    /**
     * 是否为主键
     */
    private Boolean primaryKey = Boolean.FALSE;
    /**
     * 所属数据库名称
     */
    private String schema;
    /**
     * 所属表名
     */
    private String tableName;
    /**
     * 字段名称
     */
    private String name;
    /**
     * 数据类型
     */
    private DataType dataType;
    /**
     * 默认值
     */
    private Object defaultValue;
    /**
     * 是否允许为空
     */
    private Boolean allowNull = Boolean.TRUE;
    /**
     * 备注、描述
     */
    private String comment;
}
