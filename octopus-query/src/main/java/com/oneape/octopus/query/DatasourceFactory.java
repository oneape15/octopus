package com.oneape.octopus.query;

import com.oneape.octopus.query.data.DatasourceInfo;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatasourceFactory {

    /**
     * Gets a unique identifier for the data source.
     *
     * @param dsi DatasourceInfo
     * @return String
     */
    String getDatasourceKey(DatasourceInfo dsi);

    /**
     * Gets the connection database connection object.
     *
     * @param dsInfo DatasourceInfo
     * @return Connection
     */
    Connection getConnection(DatasourceInfo dsInfo) throws SQLException;

    /**
     * Add data source information.
     *
     * @param dsInfo DatasourceInfo
     * @return int  1 - success; 0 - fail.
     */
    int addDatasource(DatasourceInfo dsInfo);

    /**
     * Remove data source information.
     *
     * @param dsInfo DatasourceInfo
     */
    void removeDatasource(DatasourceInfo dsInfo);

    /**
     * Refresh data source.
     * Complete the following steps:
     * 1 remove from the memory cache;
     * 2 add to the memory cache again;
     *
     * @param dsInfo DatasourceInfo
     */
    void refreshDatasource(DatasourceInfo dsInfo);

    /**
     * Get the number of data sources.
     *
     * @return int
     */
    int getDatasourceSize();

    /**
     * Test whether the data source is valid.
     *
     * @param dsInfo DatasourceInfo
     * @return boolean  true - effective; false - invalid;
     */
    boolean testDatasource(DatasourceInfo dsInfo);
}
