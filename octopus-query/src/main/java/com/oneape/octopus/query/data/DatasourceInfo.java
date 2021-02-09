package com.oneape.octopus.query.data;

import com.oneape.octopus.query.DatasourceTypeHelper;
import lombok.Data;

import java.io.Serializable;

@Data
public class DatasourceInfo implements Serializable {

    public static final Integer DEFAULT_TIMEOUT   = 60 * 1000;
    public static final Integer DEFAULT_POOL_SIZE = 5;
    public static final Integer DEFAULT_IDLE      = 1;

    /**
     * the datasource primary key
     */
    private           Long                 id;
    /**
     * The Data source type helper class.
     */
    private           DatasourceTypeHelper datasourceType;
    /**
     * DB url
     */
    private           String               url;
    /**
     * Database login name
     */
    private           String               username;
    /**
     * Database login password
     */
    private transient String               password;
    /**
     * The sql of use to test.
     */
    private           String               testSql;
    /**
     * Data source connection timeout(ms).
     */
    private Integer timeout     = DEFAULT_TIMEOUT;
    /**
     * Data source max pool size.
     */
    private Integer maxPoolSize = DEFAULT_POOL_SIZE;
    /**
     * Minimum free connection.
     */
    private Integer minIdle     = 1;
    /**
     * The datasource is read only.
     */
    private Boolean readOnly    = Boolean.TRUE;
}
