package com.oneape.octopus.datasource;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.oneape.octopus.commons.security.MD5Utils;
import com.oneape.octopus.commons.security.PBEUtils;
import com.oneape.octopus.datasource.data.DatasourceInfo;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.oneape.octopus.commons.constant.OctopusConstant.PWD_MASK_TAG;

/**
 * Data source management factory class.
 */
@Slf4j
public class DefaultDatasourceFactory implements DatasourceFactory {

    private static ConcurrentHashMap<String, HikariDataSource> datasourceMap = new ConcurrentHashMap<>();

    private static final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    /**
     * Gets a unique identifier for the data source.
     *
     * @param dsi DatasourceInfo
     * @return String
     */
    @Override
    public String getDatasourceKey(DatasourceInfo dsi) {
        Preconditions.checkNotNull(dsi, "The data source information is empty!");
        Preconditions.checkArgument(StringUtils.isNotBlank(dsi.getUrl()), "The data source URL is empty!");

        String url = StringUtils.substringBefore(dsi.getUrl(), "?");
        if (dsi.getDatasourceType() == DatasourceTypeHelper.Odps) {
            // When ODPS multiple projects at the same time, according to the url param "project=xxx" to differentiate the data sources.
            url = StringUtils.trimToEmpty(dsi.getUrl());
        }
        String password = StringUtils.trimToEmpty(dsi.getPassword());
        String username = StringUtils.trimToEmpty(dsi.getUsername());

        return MD5Utils.getMD5(url + username + password);
    }

    /**
     * Gets the connection database connection object.
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
     * Add data source information.
     *
     * @param dsInfo DatasourceInfo
     * @return int  1 - success; 0 - fail.
     */
    @Override
    public int addDatasource(DatasourceInfo dsInfo) {
        String key = getDatasourceKey(dsInfo);
        try {
            readWriteLock.writeLock().lock();
            HikariDataSource ds = datasourceMap.getOrDefault(key, null);
            if (ds != null) {
                log.info("The data source already exists. datasource: ", JSON.toJSONString(dsInfo));
                return 1;
            }
            datasourceMap.put(key, initDataSource(dsInfo));
        } catch (Exception e) {
            log.error("Failure to add data source.", e);
            return 0;
        } finally {
            readWriteLock.writeLock().unlock();
        }

        return 1;
    }

    /**
     * Remove data source information.
     *
     * @param dsInfo DatasourceInfo
     */
    @Override
    public void removeDatasource(DatasourceInfo dsInfo) {
        String key = getDatasourceKey(dsInfo);
        try {
            readWriteLock.writeLock().lock();
            HikariDataSource ds = datasourceMap.get(key);
            if (ds == null) return;

            ds.close();
            if (ds.isClosed()) datasourceMap.remove(key);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    /**
     * Refresh data source.
     * Complete the following steps:
     * 1 remove from the memory cache;
     * 2 add to the memory cache again;
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
     * Get the number of data sources.
     *
     * @return int
     */
    @Override
    public int getDatasourceSize() {
        return datasourceMap.size();
    }

    /**
     * get the value.
     *
     * @param value        String
     * @param defaultValue String
     * @return String
     */
    private static String getValueWithDefault(String value, String defaultValue) {
        return StringUtils.isNotEmpty(value) ? value : defaultValue;
    }

    /**
     * Test whether the data source is valid.
     *
     * @param dsInfo DatasourceInfo
     * @return boolean  true - effective; false - invalid;
     */
    @Override
    public boolean testDatasource(DatasourceInfo dsInfo) {
        if (dsInfo == null || dsInfo.getDatasourceType() == null) {
            throw new RuntimeException("The data source information is empty!");
        }
        String url = dsInfo.getUrl();
        String userName = getValueWithDefault(dsInfo.getUsername(), "");
        String password = getValueWithDefault(dsInfo.getPassword(), "");
        String testSql = getValueWithDefault(dsInfo.getTestSql(), "select 1");
        if (StringUtils.isNotBlank(password) && StringUtils.startsWith(password, PWD_MASK_TAG)) {
            password = PBEUtils.decrypt(StringUtils.substringAfter(password, PWD_MASK_TAG));
        }
        try {
            Class.forName(dsInfo.getDatasourceType().getDriverClass());
            try (Connection connection = DriverManager.getConnection(url, userName, password);
                 Statement statement = connection.createStatement()) {
                statement.setQueryTimeout(10);
                statement.executeQuery(testSql);
                log.info("Data source connection test successful.");
                return true;
            }
        } catch (Exception e) {
            log.error("The database connection test failed!", e);
            return false;
        }
    }

    /**
     * Initializes the data source connection pool.
     *
     * @param dsi DatasourceInfo.
     * @return HikariDataSource
     */
    protected HikariDataSource initDataSource(DatasourceInfo dsi) {
        DatasourceTypeHelper dth = dsi.getDatasourceType();

        try {
            Class.forName(dth.getDriverClass());
        } catch (Exception e) {
            log.error("The driver class [{}] is not exist!", dth.getDriverClass());
            throw new RuntimeException("The driver class [" + dth.getDriverClass() + "] is not exist!");
        }

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(dth.getDriverClass());
        hikariConfig.setJdbcUrl(dsi.getUrl());
        hikariConfig.setUsername(dsi.getUsername());
        String password = dsi.getPassword();
        if (StringUtils.isNotBlank(dsi.getPassword()) && StringUtils.startsWith(dsi.getPassword(), PWD_MASK_TAG)) {
            password = PBEUtils.decrypt(StringUtils.substringAfter(dsi.getPassword(), PWD_MASK_TAG));
        }
        hikariConfig.setPassword(password);

        hikariConfig.setMaximumPoolSize(dsi.getMaxPoolSize() != null ? dsi.getMaxPoolSize() : DatasourceInfo.DEFAULT_POOL_SIZE);
        hikariConfig.setMinimumIdle(dsi.getMinIdle() != null ? dsi.getMinIdle() : DatasourceInfo.DEFAULT_IDLE);
        hikariConfig.setConnectionTimeout(dsi.getTimeout() != null ? dsi.getTimeout() : DatasourceInfo.DEFAULT_TIMEOUT);
        hikariConfig.setAutoCommit(false);
        hikariConfig.setReadOnly(dsi.getReadOnly());

        return new HikariDataSource(hikariConfig);
    }
}
