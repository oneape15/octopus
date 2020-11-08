package com.oneape.octopus.datasource;

import com.oneape.octopus.commons.security.MD5Utils;
import com.oneape.octopus.commons.security.PBEUtils;
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
     * @return int  1 - success; 0 - fail.
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

    private static String getValueWithDefault(String value, String defaultValue) {
        return StringUtils.isNotEmpty(value) ? value : defaultValue;
    }

    /**
     * 测试数据源是否有效
     *
     * @param dsInfo DatasourceInfo
     * @return boolean  true - 有效的； false - 无效的；
     */
    @Override
    public boolean testDatasource(DatasourceInfo dsInfo) {
        if (dsInfo == null || dsInfo.getDatasourceType() == null) {
            throw new RuntimeException("数据源信息为空");
        }
        String url = dsInfo.getUrl();
        String userName = getValueWithDefault(dsInfo.getUsername(), "");
        String password = getValueWithDefault(dsInfo.getPassword(), "");
        String testSql = getValueWithDefault(dsInfo.getTestSql(), "select 1");

        try {
            Class.forName(dsInfo.getDatasourceType().getDriverClass());
            try (Connection connection = DriverManager.getConnection(url, userName, password);
                 Statement statement = connection.createStatement()) {
                statement.setQueryTimeout(10);
                statement.executeQuery(testSql);
                log.info("数据源连接测试成功");
                return true;
            }
        } catch (Exception e) {
            log.error("数据库连接测试失败", e);
            return false;
        }
    }

    /**
     * 根据DatasourceInfo生成唯一key
     *
     * @param dsi DatasourceInfo
     * @return String
     */
    @Override
    public String getDatasourceKey(DatasourceInfo dsi) {
        if (dsi == null) {
            throw new RuntimeException("数据源信息为空~");
        }
        if (StringUtils.isBlank(dsi.getUrl())) {
            throw new RuntimeException("数据源URL为空~");
        }

        String url = StringUtils.substringBefore(dsi.getUrl(), "?");
        if (dsi.getDatasourceType() == DatasourceTypeHelper.OdpsSQL) {
            // 当odps多个项目同时,要根据 ?project=xxxx来区分数据源
            url = StringUtils.trimToEmpty(dsi.getUrl());
        }
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
        if (StringUtils.isNotBlank(dsi.getPassword()) && StringUtils.startsWith(dsi.getPassword(), PWD_MASK_TAG)) {
            PBEUtils.decrypt(StringUtils.substringAfter(dsi.getPassword(), PWD_MASK_TAG));
        } else {
            hikariConfig.setPassword(dsi.getPassword());
        }
        hikariConfig.setMaximumPoolSize(5);
        hikariConfig.setConnectionTimeout(60 * 1000);
        hikariConfig.setAutoCommit(false);
        hikariConfig.setReadOnly(true);

        return new HikariDataSource(hikariConfig);
    }
}
