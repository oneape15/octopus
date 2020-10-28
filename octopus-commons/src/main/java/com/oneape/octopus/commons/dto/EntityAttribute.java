package com.oneape.octopus.commons.dto;

import lombok.Data;

/**
 * The domain entity attribute.
 */
@Data
public class EntityAttribute {
    /**
     * The field name.
     */
    private String name;
    /**
     * The data type.
     */
    private String type;
    /**
     * The value.
     */
    private Object value;
    /**
     * (Optional) The name of the column. Defaults to the property or field name.
     */
    private String columnName;
    /**
     * Whether the database column
     */
    private Boolean dbColumn = false;
    /**
     * (Optional) Whether the database column is nullable.
     */
    private Boolean nullable = true;
    /**
     * (Optional) The column length.
     * (Applies only if a string-valued column is used.)
     */
    int length    = 255;
    /**
     * (Optional) The precision for a decimal (exact numeric)
     * column. (Applies only if a decimal column is used.)
     * Value must be set by developer if used when generating
     * the DDL for the column.
     */
    int precision = 0;

    /**
     * (Optional) The scale for a decimal (exact numeric) column.
     * (Applies only if a decimal column is used.)
     */
    int scale = 0;
}
