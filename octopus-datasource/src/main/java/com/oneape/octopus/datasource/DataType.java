package com.oneape.octopus.datasource;

import org.apache.commons.lang3.StringUtils;

/**
 * 数据类型
 */
public enum DataType {
    // 字符类型
    STRING,
    // 二进制
    BINARY,
    // 布尔类型
    BOOLEAN,
    // 整型
    INTEGER,
    // 高精度
    DECIMAL,
    // 长整数
    LONG,
    // 单精度浮点数
    FLOAT,
    // 双精度浮点数
    DOUBLE,
    // 日期
    DATE,
    // 时期
    TIME,
    // 时间戳
    TIMESTAMP,
    // 日期时间
    DATETIME,
    // 其他对象
    OBJ;


    public static DataType byName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        for (DataType dt : values()) {
            if (StringUtils.equalsIgnoreCase(name, dt.name())) {
                return dt;
            }
        }
        return null;
    }
}
