package com.oneape.octopus.datasource;

import org.apache.commons.lang3.StringUtils;

/**
 * The Data source type helper class.
 */
public enum DatasourceTypeHelper {
    Firebird("org.firebirdsql.ds.FBSimpleDataSource", ""),
    H2("org.h2.jdbcx.JdbcDataSource", "jdbc:h2:tcp://{host}:{port}/{dbName}"),
    HSQLDB("org.hsqldb.jdbc.JDBCDataSource", ""),
    IbmDB2("com.ibm.db2.jcc.DB2SimpleDataSource", ""),
    IbmInformix("com.informix.jdbcx.IfxDataSource", ""),
    MariaDB("org.mariadb.jdbc.Driver", "jdbc:mariadb://{host}:{port}/{dbName}"),
    MySQL("com.mysql.cj.jdbc.Driver", "jdbc:mysql://{host}:{port}/{dbName}"),
    Oracle("oracle.jdbc.OracleDriver", "jdbc:oracle:thin:@{host}:{port}:{dbName}"),
    PostgreSQL("org.postgresql.Driver", "jdbc:postgresql://{host}:{port}/{dbName}"),
    Odps("com.aliyun.odps.jdbc.OdpsDriver", "jdbc:odps:{host}?project={dbName}"),
    SQLite("org.sqlite.SQLiteDataSource", "");

    /**
     * Full name of the driver class.
     */
    private String driverClass;
    /**
     * URL address template.
     */
    private String urlTemplate;

    DatasourceTypeHelper(String driverClass, String urlTemplate) {
        this.driverClass = driverClass;
        this.urlTemplate = urlTemplate;
    }

    public String getDriverClass() {
        return this.driverClass;
    }

    public String getUrlTemplate() {
        return this.urlTemplate;
    }

    /**
     * Is the supported data source.
     *
     * @param databaseName String
     * @return boolean
     */
    public static boolean isSupport(String databaseName) {
        if (StringUtils.isBlank(databaseName)) {
            return false;
        }
        for (DatasourceTypeHelper dbth : values()) {
            if (StringUtils.equalsIgnoreCase(dbth.name(), databaseName)) {
                return true;
            }
        }
        return false;
    }

    public static DatasourceTypeHelper byName(String databaseName) {
        if (StringUtils.isBlank(databaseName)) {
            return null;
        }
        for (DatasourceTypeHelper dbth : values()) {
            if (StringUtils.equalsIgnoreCase(dbth.name(), databaseName)) {
                return dbth;
            }
        }
        return null;
    }
}
