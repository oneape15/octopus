package com.oneape.octopus.query.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Data for a cell.
 */
@Data
@NoArgsConstructor
public class Cell implements Serializable {
    /**
     * The Head information.
     */
    private ColumnHead head;
    /**
     * The value of cell.
     */
    private Object value;

    public Cell(ColumnHead head, Object value) {
        this.head = head;
        this.value = value;
    }
}
