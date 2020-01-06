package com.oneape.octopus.datasource;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatasourceFactory {

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
     * @return int  1 - 成功； 0 - 失败
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
}
