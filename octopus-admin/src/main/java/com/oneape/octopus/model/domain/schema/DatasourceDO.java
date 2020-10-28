package com.oneape.octopus.model.domain.schema;

import com.oneape.octopus.commons.enums.EntityColumn;
import com.oneape.octopus.datasource.DatasourceTypeHelper;
import com.oneape.octopus.model.domain.BaseDO;
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
     * {@link DatasourceTypeHelper }
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
     * Synchronization period expression '0 0 9 * * ?'
     */
    private String  cron;
    /**
     * Connection pool timeout(ms)
     */
    private Integer timeout;
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
