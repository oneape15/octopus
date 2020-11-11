package com.oneape.octopus.datasource.dialect;

import com.oneape.octopus.commons.cause.BizException;
import com.oneape.octopus.commons.dto.DataType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;

/**
 * The H2 data source executor
 */
@Slf4j
public class H2Actuator extends MySQLActuator {

    public H2Actuator(Connection conn) {
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
        String startString = "jdbc:h2:tcp://";
        if (!StringUtils.startsWithIgnoreCase(url, startString)) {
            throw new RuntimeException("Not a valid H2 connection address: " + url);
        }
        String tmp = StringUtils.substring(url, startString.length());
        int index;
        if ((index = StringUtils.indexOf(tmp, "/")) > -1 && index + 1 < tmp.length()) {
            String schema = StringUtils.substring(tmp, index + 1);
            return StringUtils.trimToNull(schema);
        }

        throw new BizException("The schema name were not found!");
    }

    /**
     * 获取所有数据库名称SQL
     *
     * @return String
     */
    @Override
    public String getAllDatabaseSql() {
        return "SELECT DISTINCT TABLE_CATALOG " + COL_SCHEMA + " FROM INFORMATION_SCHEMA.TABLES";
    }

    /**
     * Gets the table information SQL for the specified database.
     *
     * @return String
     */
    @Override
    public String getTablesSql() {
        return "SELECT " +
                " TABLE_CATALOG " + COL_SCHEMA + "," +
                " TABLE_NAME " + COL_TABLE + ", " +
                " REMARKS  " + COL_COMMENT + "," +
                " 1 " + COL_TABLE_TYPE + " " +
                "FROM " +
                " INFORMATION_SCHEMA.TABLES " +
                "WHERE " +
                " TABLE_SCHEMA='PUBLIC' AND TABLE_CATALOG = ?";
    }

    /**
     * Gets all field information for the specified database.
     *
     * @return String
     */
    @Override
    public String getFieldsSql() {
        return "SELECT " +
                " TABLE_CATALOG " + COL_SCHEMA + ", " +
                " TABLE_NAME " + COL_TABLE + ", " +
                " COLUMN_NAME " + COL_COLUMN + ", " +
                " COLUMN_DEFAULT " + COL_DEFAULT_VAL + ", " +
                " CASE WHEN NULLABLE = 1 THEN 0 ELSE 1 END " + COL_NULLABLE + ", " +
                " DATA_TYPE " + COL_DATA_TYPE + ", " +
                " 0 " + COL_PRI_KEY + ", " +
                " REMARKS  " + COL_COMMENT + " " +
                "FROM information_schema.COLUMNS " +
                " WHERE TABLE_SCHEMA='PUBLIC' AND TABLE_CATALOG = ? " +
                "ORDER BY TABLE_SCHEMA, TABLE_NAME, ORDINAL_POSITION";
    }

    /**
     * Gets the specified table field to execute SQL.
     *
     * @return String
     */
    @Override
    public String getFieldsSqlOfTable() {
        return "SELECT " +
                " TABLE_CATALOG " + COL_SCHEMA + ", " +
                " TABLE_NAME " + COL_TABLE + ", " +
                " COLUMN_NAME " + COL_COLUMN + ", " +
                " COLUMN_DEFAULT " + COL_DEFAULT_VAL + ", " +
                " CASE WHEN NULLABLE = 1 THEN 0 ELSE 1 END " + COL_NULLABLE + ", " +
                " DATA_TYPE " + COL_DATA_TYPE + ", " +
                " 0 " + COL_PRI_KEY + ", " +
                " REMARKS  " + COL_COMMENT + " " +
                "FROM information_schema.COLUMNS " +
                " WHERE TABLE_SCHEMA='PUBLIC' AND TABLE_CATALOG = ? AND TABLE_NAME = ? " +
                "ORDER BY TABLE_SCHEMA, TABLE_NAME, ORDINAL_POSITION";
    }

    /**
     * Converts data types from different data sources to standard data types.
     *
     * @param dataType String
     * @return DataType
     */
    @Override
    public DataType dialect2StandDataType(String dataType) {
        return h2type2StandDataType(dataType);
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
        return h2type2StandDataType(columnTypeName);
    }

    private DataType h2type2StandDataType(String dataType) {
        String tmp = StringUtils.trimToEmpty(dataType).toLowerCase();
        int index;
        if ((index = StringUtils.indexOf(tmp, "(")) > -1) {
            tmp = StringUtils.substring(tmp, 0, index).trim();
        }
        if ((index = StringUtils.indexOf(tmp, " ")) > -1) {
            tmp = StringUtils.substring(tmp, 0, index).trim();
        }
        DataType dt;
        switch (tmp) {
            case "int":
            case "integer":
            case "mediumint":
            case "int4":
            case "signed":
            case "tinyint":
            case "smallint":
            case "int2":
            case "year":
            case "enum":
                dt = DataType.INTEGER;
                break;
            case "bigint":
            case "int8":
            case "identity":
                dt = DataType.LONG;
                break;
            case "decimal":
            case "number":
            case "dec":
            case "numeric":
                dt = DataType.DECIMAL;
                break;
            case "double":
            case "float8":
                dt = DataType.DOUBLE;
                break;
            case "float":
            case "real":
            case "float4":
                dt = DataType.FLOAT;
                break;
            case "boolean":
            case "bit":
            case "bool":
                dt = DataType.BOOLEAN;
                break;
            case "time":
                dt = DataType.TIME;
                break;
            case "date":
                dt = DataType.DATE;
                break;
            case "timestamp":
            case "datetime":
            case "smalldatetime":
                dt = DataType.TIMESTAMP;
                break;
            case "binary":
            case "varbinary":
            case "longvarbinary":
            case "raw":
            case "bytea":
            case "blob":
            case "tinyblob":
            case "mediumblob":
            case "longblob":
            case "image":
            case "oid":
            case "clob":
            case "tinytext":
            case "text":
            case "longtext":
            case "ntext":
            case "nclob":
            case "varchar":
            case "varchar2":
            case "nvarchar":
            case "varchar_casesensitive":
            case "varchar_ignorecase":
            case "longvarchar":
            case "char":
            case "character":
            case "nchar":
            case "other":
                dt = DataType.STRING;
                break;
            default:
                dt = DataType.STRING;
                break;
        }
        return dt;
    }

}
