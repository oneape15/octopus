package com.oneape.octopus.datasource;

import com.oneape.octopus.commons.value.MD5Utils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 数据源管理工厂类
 */
@Slf4j
public class DefaultDatasourceFactory implements DatasourceFactory {

    private static ConcurrentHashMap<String, HikariDataSource> datasourceMap = new ConcurrentHashMap<>();

    private static final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    /**
     * 获取连接对象
     *
     * @param dsInfo DatasourceInfo
     * @return Connection
     */
    @Override
    public Connection getConnection(DatasourceInfo dsInfo) throws SQLException {
        String key = getDatasourceKey(dsInfo);
        try {
            readWriteLock.readLock().lock();
            HikariDataSource ds = datasourceMap.getOrDefault(key, null);
            if (ds == null) {
                datasourceMap.put(key, initDataSource(dsInfo));
            }
            return datasourceMap.get(key).getConnection();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    /**
     * 添加数据源
     *
     * @param dsInfo DatasourceInfo
     * @return int  1 - 成功； 0 - 失败
     */
    @Override
    public int addDatasource(DatasourceInfo dsInfo) {
        String key = getDatasourceKey(dsInfo);
        try {
            readWriteLock.writeLock().lock();
            HikariDataSource ds = datasourceMap.getOrDefault(key, null);
            if (ds != null) {
                log.info("数据源已经存在");
                return 1;
            }
            datasourceMap.put(key, initDataSource(dsInfo));
        } catch (Exception e) {
            log.error("添加数据源失败", e);
            return 0;
        } finally {
            readWriteLock.writeLock().unlock();
        }

        return 1;
    }

    /**
     * 删除数据源
     *
     * @param dsInfo DatasourceInfo
     */
    @Override
    public void removeDatasource(DatasourceInfo dsInfo) {
        String key = getDatasourceKey(dsInfo);
        try {
            readWriteLock.writeLock().lock();
            HikariDataSource ds = datasourceMap.get(key);
            if (ds == null) {
                log.warn("dataSource不存在");
                return;
            }
            ds.close();
            if (ds.isClosed()) {
                datasourceMap.remove(key);
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    /**
     * 刷新数据源( 1 remove, 2 add)
     *
     * @param dsInfo DatasourceInfo
     */
    @Override
    public void refreshDatasource(DatasourceInfo dsInfo) {
        try {
            readWriteLock.writeLock().lock();
            removeDatasource(dsInfo);
            addDatasource(dsInfo);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    /**
     * 获取数据源数量
     *
     * @return int
     */
    @Override
    public int getDatasourceSize() {
        return datasourceMap.size();
    }

    /**
     * 根据DatasourceInfo生成唯一key
     *
     * @param dsi DatasourceInfo
     * @return String
     */
    protected String getDatasourceKey(DatasourceInfo dsi) {
        if (dsi == null) {
            throw new RuntimeException("数据源信息为空~");
        }
        if (StringUtils.isBlank(dsi.getUrl())) {
            throw new RuntimeException("数据源URL为空~");
        }

        String url = StringUtils.substringBefore(dsi.getUrl(), "?");
        String password = StringUtils.trimToEmpty(dsi.getPassword());
        String username = StringUtils.trimToEmpty(dsi.getUsername());

        return MD5Utils.getMD5(url + username + password);
    }

    protected HikariDataSource initDataSource(DatasourceInfo dsi) {
        DatasourceTypeHelper dth = dsi.getDatasourceType();

        try {
            Class.forName(dth.getDriverClass());
        } catch (Exception e) {
            log.error("数据源驱动类：[{}]不存在", dth.getDriverClass());
            throw new RuntimeException("数据源驱动类：[" + dth.getDriverClass() + "]不存在");
        }

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(dth.getDriverClass());
        hikariConfig.setJdbcUrl(dsi.getUrl());
        hikariConfig.setUsername(dsi.getUsername());
        hikariConfig.setPassword(dsi.getPassword());
        hikariConfig.setMaximumPoolSize(20);
        hikariConfig.setConnectionTimeout(60 * 1000);
        hikariConfig.setAutoCommit(true);


        return new HikariDataSource(hikariConfig);
    }
}
