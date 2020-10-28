package com.oneape.octopus.datasource;

import com.oneape.octopus.datasource.data.ColumnHead;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 某一单元格数据
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
