package com.oneape.octopus.datasource;

import com.oneape.octopus.datasource.data.Result;
import com.oneape.octopus.datasource.schema.FieldInfo;
import com.oneape.octopus.datasource.schema.TableInfo;

import java.util.List;

public interface QueryFactory {

    /**
     * 获取所有数据库名称
     *
     * @param dsi DatasourceInfo
     * @return List
     */
    List<String> allDatabase(DatasourceInfo dsi);

    /**
     * 获取所有表信息
     *
     * @param dsi DatasourceInfo
     * @return List
     */
    List<TableInfo> allTables(DatasourceInfo dsi);

    /**
     * 获取所有表信息
     *
     * @param dsi    DatasourceInfo
     * @param schema String 数据库名称
     * @return List
     */
    List<TableInfo> allTables(DatasourceInfo dsi, String schema);

    /**
     * 获取所有表的字段信息
     *
     * @param dsi DatasourceInfo
     * @return List
     */
    List<FieldInfo> allFields(DatasourceInfo dsi);

    /**
     * 获取所有表的字段信息
     *
     * @param dsi    DatasourceInfo
     * @param schema String 数据库名称
     * @return List
     */
    List<FieldInfo> allFields(DatasourceInfo dsi, String schema);

    /**
     * 获取表字段信息
     *
     * @param dsi       DatasourceInfo
     * @param schema    String 数据库名称
     * @param tableName String 表名称
     * @return List
     */
    List<FieldInfo> fieldOfTable(DatasourceInfo dsi, String schema, String tableName);

    /**
     * 执行SQL操作
     *
     * @param dsi   DatasourceInfo
     * @param param ExecParam
     * @return Result
     */
    Result execSql(DatasourceInfo dsi, ExecParam param);
}