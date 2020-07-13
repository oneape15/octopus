package com.oneape.octopus.commons.dto;

import org.apache.commons.lang3.StringUtils;

/**
 * 数据类型
 */
public enum DataType {
    STRING,
    BOOLEAN,
    INTEGER,
    DECIMAL,
    LONG,
    FLOAT,
    DOUBLE,
    DATE,
    TIME,
    TIMESTAMP,
    DATETIME;


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
