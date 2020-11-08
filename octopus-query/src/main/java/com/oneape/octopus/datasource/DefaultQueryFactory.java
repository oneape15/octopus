package com.oneape.octopus.datasource;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.oneape.octopus.datasource.data.Result;
import com.oneape.octopus.datasource.dialect.Actuator;
import com.oneape.octopus.datasource.schema.SchemaTableField;
import com.oneape.octopus.datasource.schema.SchemaTable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DefaultQueryFactory implements QueryFactory {

    private DatasourceFactory datasourceFactory;
    // Table information cache
    private static final String KEY_TABLE = "table_";
    // Table field information cache
    private static final String KEY_FIELD = "field_";

    private Cache<String, List<SchemaTable>> tableCache = CacheBuilder
            .newBuilder()
            .expireAfterWrite(15, TimeUnit.MINUTES)
            .maximumSize(4 * 1024 * 1024)
            .build();

    private Cache<String, List<SchemaTableField>> fieldCache = CacheBuilder
            .newBuilder()
            .expireAfterWrite(15, TimeUnit.MINUTES)
            .maximumSize(10 * 1024 * 1024)
            .build();

    public DefaultQueryFactory(DatasourceFactory datasourceFactory) {
        this.datasourceFactory = datasourceFactory;
    }

    /**
     * Get the database name
     *
     * @param dsi DatasourceInfo
     * @return String
     */
    @Override
    public String getSchema(DatasourceInfo dsi) {
        try {
            Actuator actuator = ActuatorFactory.build(null, dsi.getDatasourceType());
            return actuator.getSchema(dsi.getUrl());
        } catch (Exception e) {
            log.error("Failed to get database~", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets all database names.
     *
     * @param dsi DatasourceInfo
     * @return List
     */
    @Override
    public List<String> allDatabase(DatasourceInfo dsi) {
        try {
            Connection conn = datasourceFactory.getConnection(dsi);
            try (Statement statement = conn.createStatement()) {
                Actuator actuator = ActuatorFactory.build(statement, dsi.getDatasourceType());
                return actuator.allDatabase();
            }
        } catch (Exception e) {
            log.error("Failed to get table information~", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the database of all table names .
     *
     * @param dsi DatasourceInfo
     * @return List
     */
    @Override
    public List<SchemaTable> allTables(DatasourceInfo dsi) {
        try {
            String cacheKey = KEY_TABLE + datasourceFactory.getDatasourceKey(dsi) + "_ALL";

            List<SchemaTable> tables = tableCache.getIfPresent(cacheKey);
            if (CollectionUtils.isNotEmpty(tables)) {
                return tables;
            }

            Connection conn = datasourceFactory.getConnection(dsi);
            try (Statement statement = conn.createStatement()) {
                Actuator actuator = ActuatorFactory.build(statement, dsi.getDatasourceType());
                tables = actuator.allTables();
                if (CollectionUtils.isNotEmpty(tables)) {
                    tableCache.put(cacheKey, tables);
                }
            }
            return tables;
        } catch (Exception e) {
            log.error("Failed to get table information~", e);
            throw new RuntimeException("Failed to get table information.", e);
        }
    }

    /**
     * 获取所有表信息
     *
     * @param dsi    DatasourceInfo
     * @param schema String The database name
     * @return List
     */
    @Override
    public List<SchemaTable> allTables(DatasourceInfo dsi, String schema) {
        try {
            String cacheKey = KEY_TABLE + datasourceFactory.getDatasourceKey(dsi) + "_" + schema;

            List<SchemaTable> tables = tableCache.getIfPresent(cacheKey);
            if (CollectionUtils.isNotEmpty(tables)) {
                return tables;
            }

            Connection conn = datasourceFactory.getConnection(dsi);
            try (Statement statement = conn.createStatement()) {
                Actuator actuator = ActuatorFactory.build(statement, dsi.getDatasourceType());
                tables = actuator.allTablesOfDb(schema);
                if (CollectionUtils.isNotEmpty(tables)) {
                    tableCache.put(cacheKey, tables);
                }
            }
            return tables;
        } catch (Exception e) {
            log.error("Failed to get table information~", e);
            throw new RuntimeException("获取数据库：" + schema + " 表信息失败", e);
        }
    }

    /**
     * 获取所有表的字段信息
     *
     * @param dsi DatasourceInfo
     * @return List
     */
    @Override
    public List<SchemaTableField> allFields(DatasourceInfo dsi) {
        try {
            String cacheKey = KEY_FIELD + datasourceFactory.getDatasourceKey(dsi) + "_ALL_ALL";
            List<SchemaTableField> fields = fieldCache.getIfPresent(cacheKey);
            if (CollectionUtils.isNotEmpty(fields)) {
                return fields;
            }

            Connection conn = datasourceFactory.getConnection(dsi);
            try (Statement statement = conn.createStatement()) {
                Actuator actuator = ActuatorFactory.build(statement, dsi.getDatasourceType());
                fields = actuator.allFields();

                if (CollectionUtils.isNotEmpty(fields)) {
                    fieldCache.put(cacheKey, fields);
                }
            }

            return fields;
        } catch (Exception e) {
            log.error("获取所有表字段信息失败", e);
            throw new RuntimeException("获取数据源字段列表失败", e);
        }
    }

    /**
     * 获取所有表的字段信息
     *
     * @param dsi    DatasourceInfo
     * @param schema String 数据库名称
     * @return List
     */
    @Override
    public List<SchemaTableField> allFields(DatasourceInfo dsi, String schema) {
        try {
            String cacheKey = KEY_FIELD + datasourceFactory.getDatasourceKey(dsi) + "_" + schema + "_ALL";
            List<SchemaTableField> fields = fieldCache.getIfPresent(cacheKey);
            if (CollectionUtils.isNotEmpty(fields)) {
                return fields;
            }

            Connection conn = datasourceFactory.getConnection(dsi);
            try (Statement statement = conn.createStatement()) {
                Actuator actuator = ActuatorFactory.build(statement, dsi.getDatasourceType());
                fields = actuator.allFieldsOfDb(schema);

                if (CollectionUtils.isNotEmpty(fields)) {
                    fieldCache.put(cacheKey, fields);
                }
            }
            return fields;
        } catch (Exception e) {
            log.error("获取所有表字段信息失败", e);
            throw new RuntimeException("获取数据库:" + schema + " 表字段信息失败", e);
        }
    }

    /**
     * 获取表字段信息
     *
     * @param dsi       DatasourceInfo
     * @param schema    String 数据库名称
     * @param tableName String 表名称
     * @return List
     */
    @Override
    public List<SchemaTableField> fieldOfTable(DatasourceInfo dsi, String schema, String tableName) {
        try {
            String cacheKey = KEY_FIELD + datasourceFactory.getDatasourceKey(dsi) + "_" + schema + "_" + tableName;
            List<SchemaTableField> fields = fieldCache.getIfPresent(cacheKey);
            if (CollectionUtils.isNotEmpty(fields)) {
                return fields;
            }

            Connection conn = datasourceFactory.getConnection(dsi);
            try (Statement statement = conn.createStatement()) {
                Actuator actuator = ActuatorFactory.build(statement, dsi.getDatasourceType());
                fields = actuator.fieldOfTable(schema, tableName);

                if (CollectionUtils.isNotEmpty(fields)) {
                    fieldCache.put(cacheKey, fields);
                }
            }
            return fields;
        } catch (Exception e) {
            log.error("获取表: {} 字段信息失败", tableName, e);
            throw new RuntimeException("获取数据库：" + schema + " 中表:" + tableName + " 字段信息失败", e);
        }
    }

    /**
     * Exec SQL operations
     *
     * @param dsi   DatasourceInfo
     * @param param ExecParam
     * @return Result
     */
    @Override
    public Result execSql(DatasourceInfo dsi, ExecParam param) {
        return execSql(dsi, param, null);
    }

    /**
     * Exec SQL operations
     *
     * @param dsi     DatasourceInfo
     * @param param   ExecParam
     * @param process CellProcess
     * @return Result
     */
    @Override
    public Result execSql(DatasourceInfo dsi, ExecParam param, CellProcess<Cell, Object> process) {
        try {
            Connection conn = datasourceFactory.getConnection(dsi);
            try (Statement statement = conn.createStatement()) {
                Actuator actuator = ActuatorFactory.build(statement, dsi.getDatasourceType());
                return actuator.execSql(param, process);
            }
        } catch (Exception e) {
            log.error("exec SQL: {}", JSON.toJSONString(param), e);
            return failResult(e);
        }
    }

    /**
     * 导出数据操作
     *
     * @param dsi   DatasourceInfo
     * @param param ExportDataParam
     * @return int 1 - 成功; 0 - 失败;
     */
    @Override
    public Result exportData(DatasourceInfo dsi, ExportDataParam param) {
        return exportData(dsi, param, null);
    }

    /**
     * 导出数据操作
     *
     * @param dsi     DatasourceInfo
     * @param param   ExportDataParam
     * @param process CellProcess
     * @return int 1 - 成功; 0 - 失败;
     */
    @Override
    public Result exportData(DatasourceInfo dsi, ExportDataParam param, CellProcess<Cell, Object> process) {
        try {
            Connection conn = datasourceFactory.getConnection(dsi);
            try (Statement statement = conn.createStatement()) {
                Actuator actuator = ActuatorFactory.build(statement, dsi.getDatasourceType());
                return actuator.exportData(param, process);
            }
        } catch (Exception e) {
            log.error("执行SQL: {} 失败", JSON.toJSONString(param), e);
            return failResult(e);
        }
    }

    private Result failResult(Exception e) {
        Result result = new Result();
        result.setStatus(Result.QueryStatus.ERROR);
        result.getRunInfo().put(Result.KEY_ERR_MSG, JSON.toJSONString(e));
        return result;
    }
}
