package com.oneape.octopus.controller.datawarehouse.form;

import com.oneape.octopus.domain.schema.TableSchemaDO;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-12-29 18:24.
 * Modify:
 */
@Data
public class TableSchemaForm implements Serializable {

    private Long   id;
    /**
     * Synchronous table structure expression.
     * if syncCron is null, then is never sync.
     */
    private String syncCron;
    /**
     * db table description
     */
    private String comment;

    public interface InfoCheck {
    }

    public TableSchemaDO toDO() {
        TableSchemaDO tsdo = new TableSchemaDO();
        tsdo.setId(id);
        tsdo.setSyncCron(syncCron);
        tsdo.setComment(comment);
        return tsdo;
    }
}
