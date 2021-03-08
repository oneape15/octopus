package com.oneape.octopus.query.schema;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Database table information object.
 */
@Data
@NoArgsConstructor
public class SchemaTable implements Serializable {
    /**
     * The schema table name.
     */
    private String  name;
    /**
     * The table of schema.
     */
    private String  schema;
    /**
     * The table type 0 - Physical table； 1 - view
     */
    private Integer view;
    /**
     * The schema table description.
     */
    private String  comment;
}
