package com.oneape.octopus.admin.controller.datawarehouse.form;

import com.oneape.octopus.commons.validation.IntValueRangeDetection;
import com.oneape.octopus.domain.warehouse.TableSchemaDO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-12-29 18:24.
 * Modify:
 */
@Data
public class TableSchemaForm implements Serializable {
    @NotNull(message = "{global.pKey.empty}", groups = {InfoCheck.class})
    private Long    id;
    /**
     * Data table alias.
     */
    private String  alias;
    /**
     * synchronization state. 0 - Out of sync; 1 - sync.
     */
    @IntValueRangeDetection(range = {0, 1}, groups = {InfoCheck.class})
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
        BeanUtils.copyProperties(this, tsdo);
        return tsdo;
    }
}
