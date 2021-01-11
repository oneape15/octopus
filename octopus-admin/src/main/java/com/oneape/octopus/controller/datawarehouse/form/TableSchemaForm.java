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

    private Long    id;
    /**
     * Data table alias.
     */
    private String  alias;
    /**
     * synchronization state. 0 - Out of sync; 1 - sync.
     */
    private Integer sync;
    /**
     * Synchronous table structure expression.
     * if CRON is null, then is never sync.
     */
    private String  cron;
    /**
     * db table description
     */
    private String  comment;

    public interface InfoCheck {
    }

    public TableSchemaDO toDO() {
        TableSchemaDO tsdo = new TableSchemaDO();
        tsdo.setId(id);
        tsdo.setCron(cron);
        tsdo.setAlias(alias);
        tsdo.setComment(comment);
        return tsdo;
    }
}
