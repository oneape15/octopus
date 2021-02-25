package com.oneape.octopus.domain.schema;

import com.oneape.octopus.commons.enums.EntityColumn;
import com.oneape.octopus.domain.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * The data source DO
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DatasourceDO extends BaseDO {
    /**
     * The data source name.
     */
    private String  name;
    /**
     * The data source type. eg: MySQL, Oracle
     */
    private String  type;
    /**
     * The data source statue , 0 - usable; 1 - disabled
     */
    private Integer status;
    /**
     * The data source jdbc url.
     */
    @EntityColumn(name = "jdbc_url")
    private String  jdbcUrl;
    /**
     * The data source driver class.
     */
    @EntityColumn(name = "jdbc_driver")
    private String  jdbcDriver;
    /**
     * The data source login username.
     */
    private String  username;
    /**
     * The data source login password.
     */
    private String  password;
    /**
     * Data source synchronization state. 0 - Out of sync; 1 - sync
     */
    private Integer sync;
    /**
     * The last sync time.
     */
    @EntityColumn(name = "last_sync_time")
    private Long    lastSyncTime;
    /**
     * Synchronization period expression '0 0 9 * * ?'
     */
    private String  cron;
    /**
     * Connection pool timeout(ms)
     */
    private Integer timeout;
    /**
     * The maximum number of connections in the pool.
     */
    @EntityColumn(name = "max_pool_size")
    private Integer maxPoolSize;
    /**
     * The minimum number of idle connections in the pool to maintain.
     */
    @EntityColumn(name = "min_idle")
    private Integer minIdle;
    /**
     * Read-only data source tag.
     */
    @EntityColumn(name = "read_only")
    private Integer readOnly;
    /**
     * Can support DDL operations.
     */
    @EntityColumn(name = "can_ddl")
    private Integer canDdl;
    /**
     * the sql of check datasource valid
     */
    @EntityColumn(name = "test_sql")
    private String  testSql;
    /**
     * Data source description information
     */
    private String  comment;

    public DatasourceDO(String name) {
        this.name = name;
    }

    public DatasourceDO(Long id) {
        this.setId(id);
    }
}
