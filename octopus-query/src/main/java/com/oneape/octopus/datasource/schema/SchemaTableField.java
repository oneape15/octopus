package com.oneape.octopus.datasource.schema;

import com.oneape.octopus.commons.dto.DataType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Table field information.
 */
@Data
@NoArgsConstructor
public class SchemaTableField implements Serializable {
    /**
     * Primary key or not.
     */
    private Boolean primaryKey = Boolean.FALSE;
    /**
     * Database name.
     */
    private String   schema;
    /**
     * By the name of the table
     */
    private String   tableName;
    /**
     * Field name
     */
    private String   name;
    /**
     * The data type.
     */
    private DataType dataType;
    /**
     * The default value.
     */
    private Object   defaultValue;
    /**
     * Is null allowed.
     */
    private Boolean allowNull = Boolean.TRUE;
    /**
     * Remark and description.
     */
    private String comment;
}
