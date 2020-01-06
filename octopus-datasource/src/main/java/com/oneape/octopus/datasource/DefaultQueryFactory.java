package com.oneape.octopus.datasource;

import com.alibaba.fastjson.JSON;
import com.oneape.octopus.datasource.data.Result;
import com.oneape.octopus.datasource.dialect.Actuator;
import com.oneape.octopus.datasource.schema.FieldInfo;
import com.oneape.octopus.datasource.schema.TableInfo;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

@Slf4j
public class DefaultQueryFactory implements QueryFactory {

    private DatasourceFactory datasourceFactory;

    public DefaultQueryFactory(DatasourceFactory datasourceFactory) {
        this.datasourceFactory = datasourceFactory;
    }

    /**
     * 获取所有数据库名称
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
            log.error("获取表信息失败~", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取所有表信息
     *
     * @param dsi DatasourceInfo
     * @return List
     */
    @Override
    public List<TableInfo> allTables(DatasourceInfo dsi) {
        try {
            Connection conn = datasourceFactory.getConnection(dsi);
            try (Statement statement = conn.createStatement()) {
                Actuator actuator = ActuatorFactory.build(statement, dsi.getDatasourceType());
                return actuator.allTables();
            }
        } catch (Exception e) {
            log.error("获取表信息失败~", e);
        }
        return null;
    }

    /**
     * 获取所有表信息
     *
     * @param dsi    DatasourceInfo
     * @param schema String 数据库名称
     * @return List
     */
    @Override
    public List<TableInfo> allTables(DatasourceInfo dsi, String schema) {
        try {
            Connection conn = datasourceFactory.getConnection(dsi);
            try (Statement statement = conn.createStatement()) {
                Actuator actuator = ActuatorFactory.build(statement, dsi.getDatasourceType());
                return actuator.allTablesOfDb(schema);
            }
        } catch (Exception e) {
            log.error("获取表信息失败~", e);
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
    public List<FieldInfo> allFields(DatasourceInfo dsi) {
        try {
            Connection conn = datasourceFactory.getConnection(dsi);
            try (Statement statement = conn.createStatement()) {
                Actuator actuator = ActuatorFactory.build(statement, dsi.getDatasourceType());
                return actuator.allFields();
            }
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
    public List<FieldInfo> allFields(DatasourceInfo dsi, String schema) {
        try {
            Connection conn = datasourceFactory.getConnection(dsi);
            try (Statement statement = conn.createStatement()) {
                Actuator actuator = ActuatorFactory.build(statement, dsi.getDatasourceType());
                return actuator.allFieldsOfDb(schema);
            }
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
    public List<FieldInfo> fieldOfTable(DatasourceInfo dsi, String schema, String tableName) {
        try {
            Connection conn = datasourceFactory.getConnection(dsi);
            try (Statement statement = conn.createStatement()) {
                Actuator actuator = ActuatorFactory.build(statement, dsi.getDatasourceType());
                return actuator.fieldOfTable(schema, tableName);
            }
        } catch (Exception e) {
            log.error("获取表: {} 字段信息失败", tableName, e);
            throw new RuntimeException("获取数据库：" + schema + " 中表:" + tableName + " 字段信息失败", e);
        }
    }

    /**
     * 执行SQL操作
     *
     * @param dsi   DatasourceInfo
     * @param param ExecParam
     * @return Result
     */
    @Override
    public Result execSql(DatasourceInfo dsi, ExecParam param) {
        try {
            Connection conn = datasourceFactory.getConnection(dsi);
            try (Statement statement = conn.createStatement()) {
                Actuator actuator = ActuatorFactory.build(statement, dsi.getDatasourceType());
                return actuator.execSql(param);
            }
        } catch (Exception e) {
            log.error("执行SQL: {} 失败", JSON.toJSONString(param), e);
        }
        return null;
    }
}
