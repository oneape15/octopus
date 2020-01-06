package com.oneape.octopus.datasource.dialect;

import com.oneape.octopus.datasource.DataType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.Statement;

/**
 * MySQL数据源执行器
 */
@Slf4j
public class MySQLActuator extends Actuator {

    public MySQLActuator(Statement statement) {
        super(statement);
    }

    /**
     * 获取所有数据库名称SQL
     *
     * @return String
     */
    @Override
    String getAllDatabaseSql() {
        return "SELECT " +
                " DISTINCT TABLE_SCHEMA " + COL_SCHEMA + " " +
                "FROM " +
                "information_schema.`TABLES`";
    }

    /**
     * 获取所有表的执行SQL
     *
     * @return String
     */
    @Override
    String getAllTablesSql() {
        return getTablesSqlOfDb(null);
    }

    /**
     * 获取指定数据库的表信息SQL
     *
     * @param schema String 数据库名称
     * @return String
     */
    @Override
    String getTablesSqlOfDb(String schema) {
        String whereSql = "";
        if (StringUtils.isNotBlank(schema)) {
            whereSql = " WHERE TABLE_SCHEMA = '" + schema + "' ";
        }
        return "SELECT " +
                " TABLE_SCHEMA " + COL_SCHEMA + "," +
                " TABLE_NAME " + COL_TABLE + "," +
                " TABLE_COMMENT  " + COL_COMMENT + ", " +
                " CASE TABLE_TYPE " +
                "   WHEN UPPER(TABLE_TYPE) = 'BASE TABLE' THEN 0 ELSE 1" +
                "  END AS " + COL_TABLE_TYPE + " " +
                "FROM " +
                " information_schema.`TABLES`  " +
                whereSql +
                "ORDER BY " +
                " TABLE_SCHEMA";
    }

    /**
     * 获取所有表字段执行SQL
     *
     * @return String
     */
    @Override
    String getAllFieldsSql() {
        return getFieldsSqlOfTable(null, null);
    }

    /**
     * 获取指定数据库所有字段信息
     *
     * @param schema String 数据库名称
     * @return String
     */
    @Override
    String getFieldsSqlOfDb(String schema) {
        return getFieldsSqlOfTable(schema, null);
    }

    /**
     * 获取指定表字段执行SQL
     *
     * @param schema    String 数据库名称
     * @param tableName String 表名称
     * @return String
     */
    @Override
    String getFieldsSqlOfTable(String schema, String tableName) {
        String whereSql = " WHERE 1 = 1 ";
        if (StringUtils.isNotBlank(schema)) {
            whereSql += " AND TABLE_SCHEMA = '" + schema + "'";
        }
        if (StringUtils.isNotBlank(tableName)) {
            whereSql += " AND TABLE_NAME = '" + tableName + "'";
        }
        return "SELECT " +
                " TABLE_SCHEMA " + COL_SCHEMA + ", " +
                " TABLE_NAME " + COL_TABLE + ", " +
                " COLUMN_NAME " + COL_COLUMN + ", " +
                " COLUMN_DEFAULT " + COL_DEFAULT_VAL + ", " +
                " CASE IS_NULLABLE " +
                "   WHEN UPPER( IS_NULLABLE ) = 'YES' THEN 1 ELSE 0 END " + COL_NULLABLE + ", " +
                " DATA_TYPE " + COL_DATA_TYPE + ", " +
                " CHARACTER_MAXIMUM_LENGTH " + COL_CHAR_MAX_LEN + ", " +
                " IF ( COLUMN_KEY = 'PRI', 1, 0 ) " + COL_PRI_KEY + ", " +
                " COLUMN_COMMENT  " + COL_COMMENT + " " +
                "FROM information_schema.`COLUMNS` " +
                whereSql + " " +
                "ORDER BY TABLE_SCHEMA, TABLE_NAME, ORDINAL_POSITION";
    }

    /**
     * 将不同数据源的数据类型转换标准的数据类型
     *
     * @param dataType String
     * @return DataType
     */
    @Override
    DataType dialect2StandDataType(String dataType) {
        String tmp = StringUtils.trimToEmpty(dataType).toLowerCase();
        DataType dt;
        switch (tmp) {
            case "set":
            case "char":
            case "varchar":
            case "json":
            case "text":
            case "mediumtext":
            case "longtext":
            case "enum":
            case "blob":
            case "mediumblob":
            case "longblob":
                dt = DataType.VARCHAR;
                break;
            case "tinyint":
            case "smallint":
            case "int":
            case "bigint":
                dt = DataType.INTEGER;
                break;
            case "time":
                dt = DataType.TIME;
                break;
            case "timestamp":
                dt = DataType.TIMESTAMP;
                break;
            case "datetime":
                dt = DataType.DATETIME;
                break;
            case "float":
                dt = DataType.FLOAT;
                break;
            case "binary":
            case "varbinary":
                dt = DataType.BINARY;
                break;
            case "decimal":
                dt = DataType.DECIMAL;
                break;
            case "double":
                dt = DataType.DOUBLE;
                break;
            default:
                dt = null;
        }
        return dt;
    }
}
