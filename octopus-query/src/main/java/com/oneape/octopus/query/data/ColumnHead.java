package com.oneape.octopus.query.data;

import com.oneape.octopus.commons.dto.DataType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ColumnHead implements Serializable {
    /**
     * The column name.
     */
    private String name;
    /**
     * The column alias.
     */
    private String label;
    /**
     * The column data type.
     */
    private DataType dataType;


    public ColumnHead(String name, String label) {
        this.name = name;
        this.label = label;
    }
}
