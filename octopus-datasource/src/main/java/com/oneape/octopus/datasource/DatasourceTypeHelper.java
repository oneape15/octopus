package com.oneape.octopus.datasource;

import org.apache.commons.lang3.StringUtils;

/**
 * 数据源类型
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
    SQLite("org.sqlite.SQLiteDataSource", "");

    /**
     * 驱动类全称
     */
    private String driverClass;
    /**
     * URL地址模板
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
     * 是否为支持的数据源
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
}
