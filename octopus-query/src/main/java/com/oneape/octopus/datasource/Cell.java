package com.oneape.octopus.datasource;

import com.oneape.octopus.datasource.data.ColumnHead;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Data for a cell.
 */
@Data
@NoArgsConstructor
public class Cell implements Serializable {
    private ColumnHead head;
    private Object value;

    public Cell(ColumnHead head, Object value) {
        this.head = head;
        this.value = value;
    }
}
