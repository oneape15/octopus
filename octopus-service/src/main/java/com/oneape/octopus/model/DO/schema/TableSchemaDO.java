package com.oneape.octopus.model.DO.schema;

import com.oneape.octopus.model.DO.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

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
    @Column(name = "datasource_id", nullable = false)
    private Long    datasourceId;
    /**
     * db table name
     */
    @Column(nullable = false)
    private String  name;
    /**
     * the table is view table, 0 - no; 1 - yes
     */
    @Column(name = "view_table")
    private Integer viewTable;
    /**
     * Latest synchronization table structure time
     */
    @Column(name = "sync_time")
    private Long    syncTime;
    /**
     * Synchronous table structure expression.
     * if syncCron is null, then is never sync.
     */
    @Column(name = "sync_cron")
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
