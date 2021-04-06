package com.oneape.octopus.query;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.oneape.octopus.commons.cause.BizException;
import com.oneape.octopus.query.data.*;
import com.oneape.octopus.query.dialect.Actuator;
import com.oneape.octopus.query.schema.SchemaTable;
import com.oneape.octopus.query.schema.SchemaTableField;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.sql.Connection;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DefaultQueryFactory implements QueryFactory {

    private final DatasourceFactory datasourceFactory;
    // Table information cache
    private static final String KEY_TABLE = "table_";
    // Table field information cache
    private static final String KEY_FIELD = "field_";

    private final Cache<String, List<SchemaTable>> tableCache = CacheBuilder
            .newBuilder()
            .expireAfterWrite(15, TimeUnit.MINUTES)
            .maximumSize(4 * 1024 * 1024)
            .build();

    private final Cache<String, List<SchemaTableField>> fieldCache = CacheBuilder
            .newBuilder()
            .expireAfterWrite(15, TimeUnit.MINUTES)
            .maximumSize(10 * 1024 * 1024)
            .build();

    public DefaultQueryFactory(DatasourceFactory datasourceFactory) {
        this.datasourceFactory = datasourceFactory;
    }

    /**
     * Get the database name.
     *
     * @param dsi DatasourceInfo
     * @return String
     */
    @Override
    public String getSchema(DatasourceInfo dsi) {
        try {
            Actuator actuator = ActuatorFactory.build(null, dsi.getDatasourceType());
            return actuator.getSchema(dsi.getUrl());
        } catch (BizException be) {
            throw be;
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
        try (Connection conn = datasourceFactory.getConnection(dsi)) {
            Actuator actuator = ActuatorFactory.build(conn, dsi.getDatasourceType());
            return actuator.allDatabase();
        } catch (BizException be) {
            throw be;
        } catch (Exception e) {
            log.error("Failed to get table information~", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the specified database table information in the data source.
     *
     * @param dsi    DatasourceInfo
     * @param schema String The database name
     * @return List
     */
    @Override
    public List<SchemaTable> allTables(DatasourceInfo dsi, String schema) {
        try (Connection conn = datasourceFactory.getConnection(dsi)) {
            String cacheKey = KEY_TABLE + datasourceFactory.getDatasourceKey(dsi) + "_" + schema;

            List<SchemaTable> tables = tableCache.getIfPresent(cacheKey);
            if (CollectionUtils.isNotEmpty(tables)) {
                return tables;
            }

            Actuator actuator = ActuatorFactory.build(conn, dsi.getDatasourceType());
            tables = actuator.allTables(schema);
            if (CollectionUtils.isNotEmpty(tables)) {
                tableCache.put(cacheKey, tables);
            }

            return tables;
        } catch (BizException be) {
            throw be;
        } catch (Exception e) {
            log.error("Failed to get table information~", e);
            throw new RuntimeException("Failed to obtain database table information! schema: " + schema, e);
        }
    }

    /**
     * Gets field information for the table in the specified database in the data source.
     *
     * @param dsi    DatasourceInfo
     * @param schema String The database name.
     * @return List
     */
    @Override
    public List<SchemaTableField> allFields(DatasourceInfo dsi, String schema) {
        try (Connection conn = datasourceFactory.getConnection(dsi)) {
            String cacheKey = KEY_FIELD + datasourceFactory.getDatasourceKey(dsi) + "_" + schema + "_ALL";
            List<SchemaTableField> fields = fieldCache.getIfPresent(cacheKey);
            if (CollectionUtils.isNotEmpty(fields)) {
                return fields;
            }

            Actuator actuator = ActuatorFactory.build(conn, dsi.getDatasourceType());
            fields = actuator.allFields(schema);

            if (CollectionUtils.isNotEmpty(fields)) {
                fieldCache.put(cacheKey, fields);
            }

            return fields;
        } catch (BizException be) {
            throw be;
        } catch (Exception e) {
            log.error("Failed to get a list of the specified database fields.", e);
            throw new RuntimeException("Failed to get a list of the specified database fields. schema :" + schema, e);
        }
    }

    /**
     * Gets field information for the specified table.
     *
     * @param dsi       DatasourceInfo
     * @param schema    String The database name
     * @param tableName String The table name
     * @return List
     */
    @Override
    public List<SchemaTableField> fieldOfTable(DatasourceInfo dsi, String schema, String tableName) {
        try (Connection conn = datasourceFactory.getConnection(dsi)) {
            String cacheKey = KEY_FIELD + datasourceFactory.getDatasourceKey(dsi) + "_" + schema + "_" + tableName;
            List<SchemaTableField> fields = fieldCache.getIfPresent(cacheKey);
            if (CollectionUtils.isNotEmpty(fields)) {
                return fields;
            }

            Actuator actuator = ActuatorFactory.build(conn, dsi.getDatasourceType());
            fields = actuator.fieldOfTable(schema, tableName);

            if (CollectionUtils.isNotEmpty(fields)) {
                fieldCache.put(cacheKey, fields);
            }
            return fields;
        } catch (BizException be) {
            throw be;
        } catch (Exception e) {
            log.error("Failed to get table field information. table: {}.{}", schema, tableName, e);
            throw new RuntimeException("Failed to get table field information. table: " + schema + "." + tableName, e);
        }
    }

    /**
     * Run DDL statements.
     *
     * @param dsi    DatasourceInfo
     * @param ddlSql String
     * @return Result
     */
    @Override
    public Result execute(DatasourceInfo dsi, String ddlSql) {
        try (Connection conn = datasourceFactory.getConnection(dsi)) {
            Actuator actuator = ActuatorFactory.build(conn, dsi.getDatasourceType());
            return actuator.execute(ddlSql);
        } catch (BizException be) {
            throw be;
        } catch (Exception e) {
            log.error("run SQL: {}", ddlSql, e);
            return failResult(e);
        }
    }

    /**
     * Perform SQL query operations.
     *
     * @param dsi   DatasourceInfo
     * @param param ExecParam
     * @return Result
     */
    @Override
    public Result executeQuery(DatasourceInfo dsi, ExecParam param) {
        return executeQuery(dsi, param, null);
    }

    /**
     * Perform SQL query operations.
     *
     * @param dsi     DatasourceInfo
     * @param param   ExecParam
     * @param process CellProcess
     * @return Result
     */
    @Override
    public Result executeQuery(DatasourceInfo dsi, ExecParam param, CellProcess<Cell, Object> process) {
        try (Connection conn = datasourceFactory.getConnection(dsi)) {
            Actuator actuator = ActuatorFactory.build(conn, dsi.getDatasourceType());
            return actuator.executeQuery(param, process);
        } catch (BizException be) {
            throw be;
        } catch (Exception e) {
            log.error("exec SQL: {}", JSON.toJSONString(param), e);
            return failResult(e);
        }
    }

    /**
     * Export data to a local file.
     *
     * @param dsi   DatasourceInfo
     * @param param ExportDataParam
     * @return Result
     */
    @Override
    public Result exportData(DatasourceInfo dsi, ExportDataParam param) {
        return exportData(dsi, param, null);
    }

    /**
     * Export data to a local file.
     *
     * @param dsi     DatasourceInfo
     * @param param   ExportDataParam
     * @param process CellProcess
     * @return Result
     */
    @Override
    public Result exportData(DatasourceInfo dsi, ExportDataParam param, CellProcess<Cell, Object> process) {
        try (Connection conn = datasourceFactory.getConnection(dsi)) {
            Actuator actuator = ActuatorFactory.build(conn, dsi.getDatasourceType());
            return actuator.exportData(param, process);
        } catch (BizException be) {
            throw be;
        } catch (Exception e) {
            log.error("exec SQL: {}", JSON.toJSONString(param), e);
            return failResult(e);
        }
    }

    private Result failResult(Exception e) {
        Result result = new Result();
        result.setStatus(QueryStatus.ERROR);
        result.setErrMsg(e.toString());
        return result;
    }
}
