package com.oneape.octopus.query.dialect;

import com.oneape.octopus.commons.cause.BizException;
import com.oneape.octopus.commons.dto.DataType;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;

/**
 * The ODPS data source executor
 * Created by oneape<oneape15@163.com>
 * Created 2020-11-10 14:26.
 * Modify:
 */
public class OdpsActuator extends MySQLActuator {

    public OdpsActuator(Connection conn) {
        super(conn);
    }

    /**
     * Get database name from the URL.
     *
     * @param url String
     * @return String
     */
    @Override
    public String getSchemaNameFromUrl(String url) {
        if (!StringUtils.startsWithIgnoreCase(url, "jdbc:odps:")) {
            throw new BizException("Not a valid ODPS connection address: " + url);
        }

        int index = StringUtils.indexOf(url, "?");
        if (index <= -1) {
            throw new BizException("Not a valid ODPS connection address: " + url);
        }

        String tmp = StringUtils.substring(url, index + 1);
        String[] params = StringUtils.split(tmp, "&");
        if (params == null || params.length <= 0) {
            throw new BizException("The [project] parameters were not found!");
        }

        for (String kv : params) {
            String[] kvArr = StringUtils.split(kv, "=");
            if (kvArr != null && kvArr.length == 2) {
                if (StringUtils.equalsIgnoreCase(kvArr[0], "project")) {
                    return kvArr[1];
                }
            }
        }

        throw new BizException("The [project] parameters were not found!");
    }

    /**
     * The SQL of gets all database names.
     *
     * @return String
     */
    @Override
    public String getAllDatabaseSql() {
        return "SELECT DISTINCT TABLE_SCHEMA " + COL_SCHEMA + " FROM  information_schema.TABLES";
    }

    /**
     * Gets the table information SQL for the specified database.
     *
     * @return String
     */
    @Override
    public String getTablesSql() {
        return "SELECT " +
                " TABLE_SCHEMA " + COL_SCHEMA + "," +
                " TABLE_NAME " + COL_TABLE + "," +
                " TABLE_COMMENT  " + COL_COMMENT + ", " +
                " CASE WHEN UPPER(TABLE_TYPE) = 'MANAGED_TABLE' THEN 0 ELSE 1" +
                "  END AS " + COL_TABLE_TYPE + " " +
                "FROM information_schema.TABLES  " +
                "WHERE TABLE_SCHEMA != 'information_schema'  AND UPPER(TABLE_SCHEMA) = UPPER( ? ) ";
    }

    /**
     * Gets all field information for the specified database.
     *
     * @return String
     */
    @Override
    public String getFieldsSql() {
        return "SELECT " +
                " TABLE_SCHEMA " + COL_SCHEMA + ", " +
                " TABLE_NAME " + COL_TABLE + ", " +
                " COLUMN_NAME " + COL_COLUMN + ", " +
                " COLUMN_DEFAULT " + COL_DEFAULT_VAL + ", " +
                " 1 " + COL_NULLABLE + ", " +
                " DATA_TYPE " + COL_DATA_TYPE + ", " +
                " 0 " + COL_PRI_KEY + ", " +
                " COLUMN_COMMENT  " + COL_COMMENT + " " +
                "FROM information_schema.COLUMNS " +
                "WHERE AND UPPER(TABLE_SCHEMA) = UPPER(?) " +
                "ORDER BY TABLE_NAME, ordinal_position ASC " +
                "LIMIT 500000";
    }

    /**
     * Gets the specified table field to execute SQL.
     * There cannot be more than 5000 fields in a single table.
     *
     * @return String
     */
    @Override
    public String getFieldsSqlOfTable() {
        return "SELECT " +
                " TABLE_SCHEMA " + COL_SCHEMA + ", " +
                " TABLE_NAME " + COL_TABLE + ", " +
                " COLUMN_NAME " + COL_COLUMN + ", " +
                " COLUMN_DEFAULT " + COL_DEFAULT_VAL + ", " +
                " 1 " + COL_NULLABLE + ", " +
                " DATA_TYPE " + COL_DATA_TYPE + ", " +
                " 0 " + COL_PRI_KEY + ", " +
                " COLUMN_COMMENT  " + COL_COMMENT + " " +
                "FROM information_schema.COLUMNS " +
                "WHERE AND UPPER(TABLE_SCHEMA) = UPPER(?) AND TABLE_NAME = ? " +
                "ORDER BY ordinal_position ASC " +
                "LIMIT 5000";
    }

    /**
     * Converts data types from different data sources to standard data types.
     *
     * @param dataType String
     * @return DataType
     */
    @Override
    public DataType dialect2StandDataType(String dataType) {
        String tmp = StringUtils.trimToEmpty(dataType).toUpperCase();
        return odpsType2DataType(tmp);
    }

    /**
     * Converts a column type to a standard data type
     *
     * @param columnType     int
     * @param columnTypeName String
     * @return DataType
     */
    @Override
    public DataType columnType2StandDataType(int columnType, String columnTypeName) {
        return odpsType2DataType(columnTypeName);
    }

    private DataType odpsType2DataType(String mt) {
        switch (mt) {
            case "BIGINT":
                return DataType.LONG;
            case "DOUBLE":
                return DataType.DOUBLE;
            case "BOOLEAN":
                return DataType.BOOLEAN;
            case "DATETIME":
                return DataType.DATETIME;
            case "DECIMAL":
                return DataType.DECIMAL;
            case "MAP":
            case "ARRAY":
            case "VOID":
            case "TINYINT":
            case "SMALLINT":
            case "INT":
                return DataType.INTEGER;
            case "FLOAT":
                return DataType.FLOAT;
            case "DATE":
                return DataType.DATE;
            case "TIMESTAMP":
                return DataType.TIMESTAMP;
            case "STRING":
            case "CHAR":
            case "VARCHAR":
            case "BINARY":
                return DataType.STRING;
            case "INTERVAL_DAY_TIME":
            case "INTERVAL_YEAR_MONTH":
            case "STRUCT":
            default:
                return null;
        }
    }
}
