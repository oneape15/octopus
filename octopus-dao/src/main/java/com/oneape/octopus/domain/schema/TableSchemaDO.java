package com.oneape.octopus.domain.schema;

import com.oneape.octopus.commons.enums.EntityColumn;
import com.oneape.octopus.domain.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Table basic information object.
 * Created by oneape<oneape15@163.com>
 * Created 2020-05-28 09:55.
 * Modify:
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TableSchemaDO extends BaseDO {

    /**
     * the datasource primary key
     */
    @EntityColumn(name = "datasource_id", nullable = false)
    private Long    datasourceId;
    /**
     * db table name
     */
    @EntityColumn(nullable = false)
    private String  name;
    /**
     * the table is view table, 0 - no; 1 - yes
     */
    @EntityColumn(name = "view_table")
    private Integer viewTable;
    /**
     * Latest synchronization table structure time
     */
    @EntityColumn(name = "sync_time")
    private Long    syncTime;
    /**
     * Synchronous table structure expression.
     * if syncCron is null, then is never sync.
     */
    @EntityColumn(name = "sync_cron")
    private String  syncCron;
    /**
     * the table use time;
     */
    private Long    heat;
    /**
     * 0 - normal, 1 - has drop
     */
    private Integer status;
    /**
     * db table description
     */
    private String  comment;


    public TableSchemaDO(Long datasourceId) {
        this.datasourceId = datasourceId;
    }

}