package com.oneape.octopus.datasource;

import com.oneape.octopus.datasource.data.*;
import com.oneape.octopus.datasource.schema.SchemaTable;
import com.oneape.octopus.datasource.schema.SchemaTableField;

import java.util.List;

public interface QueryFactory {

    /**
     * Get the database name.
     *
     * @param dsi DatasourceInfo
     * @return String
     */
    String getSchema(DatasourceInfo dsi);

    /**
     * Gets all database names.
     *
     * @param dsi DatasourceInfo
     * @return List
     */
    List<String> allDatabase(DatasourceInfo dsi);

    /**
     * Gets the specified database table information in the data source.
     *
     * @param dsi    DatasourceInfo
     * @param schema String  the database name.
     * @return List
     */
    List<SchemaTable> allTables(DatasourceInfo dsi, String schema);

    /**
     * Gets field information for the table in the specified database in the data source.
     *
     * @param dsi    DatasourceInfo
     * @param schema String The database name
     * @return List
     */
    List<SchemaTableField> allFields(DatasourceInfo dsi, String schema);

    /**
     * Gets field information for the specified table.
     *
     * @param dsi       DatasourceInfo
     * @param schema    String The database name
     * @param tableName String The table name
     * @return List
     */
    List<SchemaTableField> fieldOfTable(DatasourceInfo dsi, String schema, String tableName);

    /**
     * Run DDL statements.
     *
     * @param dsi    DatasourceInfo
     * @param ddlSql String
     * @return Result
     */
    Result runSql(DatasourceInfo dsi, String ddlSql);

    /**
     * Perform SQL query operations.
     *
     * @param dsi   DatasourceInfo
     * @param param ExecParam
     * @return Result
     */
    Result execSql(DatasourceInfo dsi, ExecParam param);

    /**
     * Perform SQL query operations.
     *
     * @param dsi     DatasourceInfo
     * @param param   ExecParam
     * @param process CellProcess
     * @return Result
     */
    Result execSql(DatasourceInfo dsi, ExecParam param, CellProcess<Cell, Object> process);

    /**
     * Export data to a local file.
     *
     * @param dsi   DatasourceInfo
     * @param param ExportDataParam
     * @return Result
     */
    Result exportData(DatasourceInfo dsi, ExportDataParam param);

    /**
     * Export data to a local file.
     *
     * @param dsi     DatasourceInfo
     * @param param   ExportDataParam
     * @param process CellProcess
     * @return Result
     */
    Result exportData(DatasourceInfo dsi, ExportDataParam param, CellProcess<Cell, Object> process);
}
