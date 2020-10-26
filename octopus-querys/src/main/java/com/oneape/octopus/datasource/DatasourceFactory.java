package com.oneape.octopus.datasource;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatasourceFactory {

    /**
     * 获取数据源唯一识别码
     *
     * @param dsi DatasourceInfo
     * @return String
     */
    String getDatasourceKey(DatasourceInfo dsi);

    /**
     * 获取连接对象
     *
     * @param dsInfo DatasourceInfo
     * @return Connection
     */
    Connection getConnection(DatasourceInfo dsInfo) throws SQLException;

    /**
     * 添加数据源
     *
     * @param dsInfo DatasourceInfo
     * @return int  1 - success; 0 - fail.
     */
    int addDatasource(DatasourceInfo dsInfo);

    /**
     * 删除数据源
     *
     * @param dsInfo DatasourceInfo
     */
    void removeDatasource(DatasourceInfo dsInfo);

    /**
     * 刷新数据源( 1 remove, 2 add)
     *
     * @param dsInfo DatasourceInfo
     */
    void refreshDatasource(DatasourceInfo dsInfo);

    /**
     * 获取数据源数量
     *
     * @return int
     */
    int getDatasourceSize();

    /**
     * 测试数据源是否有效
     *
     * @param dsInfo DatasourceInfo
     * @return boolean  true - 有效的； false - 无效的；
     */
    boolean testDatasource(DatasourceInfo dsInfo);
}