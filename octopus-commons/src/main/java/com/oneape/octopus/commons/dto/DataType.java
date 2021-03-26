package com.oneape.octopus.commons.dto;

import org.apache.commons.lang3.StringUtils;

/**
 * The field data type
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

    /**
     * Determine if it is a numeric type.
     *
     * @return boolean
     */
    public boolean isNumber() {
        switch (this) {
            case INTEGER:
            case DECIMAL:
            case LONG:
            case FLOAT:
            case DOUBLE:
                return true;
            default:
                return false;
        }
    }
}
