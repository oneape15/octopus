package com.oneape.octopus.datasource.dialect;

import com.oneape.octopus.commons.cause.BizException;
import com.oneape.octopus.commons.dto.DataType;
import com.oneape.octopus.commons.value.DataUtils;
import com.oneape.octopus.commons.value.MathFormulaUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;

/**
 * The PostgreSQL data source executor
 */
@Slf4j
public class PostgreSQLActuator extends Actuator {

    private static final String PAGE_KEY   = "LIMIT";
    private static final String PAGE_KEY_2 = "OFFSET";

    public PostgreSQLActuator(Connection conn) {
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
        String startString = "jdbc:postgresql://";
        if (!StringUtils.startsWithIgnoreCase(url, startString)) {
            throw new BizException("Not a valid PostgreSQL connection address: " + url);
        }
        String tmp = StringUtils.substring(url, startString.length());
        int index;
        if ((index = StringUtils.indexOf(tmp, "/")) > -1 && index + 1 < tmp.length()) {
            String schema = StringUtils.substring(tmp, index + 1);
            return StringUtils.trimToNull(schema);
        }
        return null;
    }

    /**
     * The SQL of gets all database names.
     *
     * @return String
     */
    @Override
    public String getAllDatabaseSql() {
        return "SELECT  datname " + COL_SCHEMA + " FROM pg_database ORDER BY datname";
    }

    /**
     * Gets the table information SQL for the specified database.
     *
     * @return String
     */
    @Override
    public String getTablesSql() {
        return "SELECT  " +
                "  schemaname " + COL_SCHEMA + ",  " +
                "  tablename " + COL_TABLE + ",  " +
                "  obj_description(relfilenode,'pg_class')  " + COL_COMMENT + ", " +
                "  1 " + COL_TABLE + " " +
                "FROM  " +
                "  pg_tables a, pg_class b   " +
                "WHERE  " +
                "  a.tablename = b.relname  " +
                "  AND a.tablename NOT LIKE 'pg%'   " +
                "  AND a.tablename NOT LIKE 'sql_%' AND a.schemaname = ? " +
                "ORDER BY  " +
                "  a.schemaname, a.tablename";
    }

    /**
     * Gets all field information for the specified database.
     *
     * @return String
     */
    @Override
    public String getFieldsSql() {
        return "SELECT  " +
                "  pg_tables.schemaname  as " + COL_SCHEMA + ",  " +
                "  pg_class.relname   as " + COL_TABLE + ",  " +
                "  pg_attribute.attname  as " + COL_COLUMN + ",  " +
                "  null  as " + COL_DEFAULT_VAL + ",  " +
                "  CASE WHEN pg_attribute.attnotnull = 't' THEN 1 ELSE 0 END  as " + COL_NULLABLE + ", " +
                "  pg_type.typname  as " + COL_DATA_TYPE + ",  " +
                "  0  as " + COL_PRI_KEY + ",  " +
                "  col_description ( pg_attribute.attrelid, pg_attribute.attnum )  as " + COL_COMMENT + " " +
                "FROM  " +
                "  pg_class, pg_tables, pg_attribute  " +
                "  INNER JOIN pg_type ON pg_type.oid = pg_attribute.atttypid   " +
                "WHERE  " +
                "  pg_tables.tablename = pg_class.relname  AND pg_tables.schemaname = ? " +
                "  AND pg_attribute.attrelid = pg_class.oid   " +
                "  AND pg_tables.tablename NOT LIKE'pg_%'   " +
                "  AND pg_tables.tablename NOT LIKE'sql_%'   " +
                "  AND pg_attribute.attnum > 0   " +
                "ORDER BY pg_tables.schemaname, pg_class.relname";
    }

    /**
     * Gets the specified table field to execute SQL.
     *
     * @return String
     */
    @Override
    public String getFieldsSqlOfTable() {
        return "SELECT  " +
                "  pg_tables.schemaname  as " + COL_SCHEMA + ",  " +
                "  pg_class.relname   as " + COL_TABLE + ",  " +
                "  pg_attribute.attname  as " + COL_COLUMN + ",  " +
                "  null  as " + COL_DEFAULT_VAL + ",  " +
                "  CASE WHEN pg_attribute.attnotnull = 't' THEN 1 ELSE 0 END  as " + COL_NULLABLE + ", " +
                "  pg_type.typname  as " + COL_DATA_TYPE + ",  " +
                "  0  as " + COL_PRI_KEY + ",  " +
                "  col_description ( pg_attribute.attrelid, pg_attribute.attnum )  as " + COL_COMMENT + " " +
                "FROM  " +
                "  pg_class, pg_tables, pg_attribute  " +
                "  INNER JOIN pg_type ON pg_type.oid = pg_attribute.atttypid   " +
                "WHERE  " +
                "  pg_tables.tablename = pg_class.relname  AND pg_tables.schemaname = ? AND pg_class.relname = ? " +
                "  AND pg_attribute.attrelid = pg_class.oid   " +
                "  AND pg_tables.tablename NOT LIKE'pg_%'   " +
                "  AND pg_tables.tablename NOT LIKE'sql_%'   " +
                "  AND pg_attribute.attnum > 0   " +
                "ORDER BY pg_tables.schemaname, pg_class.relname";
    }

    /**
     * Converts data types from different data sources to standard data types.
     *
     * @param dataType String
     * @return DataType
     */
    @Override
    public DataType dialect2StandDataType(String dataType) {
        return pgsqlType2StandDataType(dataType);
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
        return pgsqlType2StandDataType(columnTypeName);
    }

    private DataType pgsqlType2StandDataType(String type) {
        DataType dt;
        switch (type) {
            case "int2":
            case "int4":
                dt = DataType.INTEGER;
                break;
            case "oid":
            case "int8":
            case "serial":
                dt = DataType.LONG;
                break;
            case "float4":
                dt = DataType.FLOAT;
                break;
            case "money":
            case "float8":
                dt = DataType.DOUBLE;
                break;
            case "numeric":
                dt = DataType.DECIMAL;
                break;
            case "char":
            case "bpchar":
            case "varchar":
            case "text":
            case "name":
                dt = DataType.STRING;
                break;
            case "bool":
            case "bit":
                dt = DataType.BOOLEAN;
                break;
            case "date":
                dt = DataType.DATE;
                break;
            case "time":
            case "timez":
                dt = DataType.TIME;
                break;
            case "timestamp":
            case "timestampz":
                dt = DataType.TIMESTAMP;
                break;
            case "bytea":
            case "refcursor":
            case "json":
            case "point":
            default:
                dt = DataType.STRING;
        }
        return dt;
    }

    /**
     * Get the total number of SQL pieces of data.
     *
     * @param detailSql String
     * @return String
     */
    @Override
    public String wrapperTotalSql(String detailSql) {
        return "SELECT COUNT(1) AS " + COL_COUNT_SIZE +
                " FROM (" + detailSql +
                ") AS tmp";
    }

    /**
     * Wrap paging query SQL
     *
     * @param detailSql String detail sql
     * @param pageIndex int start from 0
     * @param pageSize  int
     * @return String
     */
    @Override
    public String wrapperPageableSql(String detailSql, int pageIndex, int pageSize) {
        if (StringUtils.isBlank(detailSql)) {
            return detailSql;
        }
        return detailSql + " " + PAGE_KEY + " " + pageSize + " " + PAGE_KEY_2 + " " + (pageIndex * pageSize);
    }

    /**
     * Determine if pagination has occurred
     *
     * @param detailSql String
     * @return Boolean
     */
    @Override
    public boolean hasPageable(String detailSql) {
        if (StringUtils.isBlank(detailSql)) {
            return false;
        }
        String tmp = StringUtils.upperCase(detailSql).trim();
        int index = StringUtils.lastIndexOf(tmp, PAGE_KEY);
        if (index < 0) {
            return false;
        }

        int startIndex = index + PAGE_KEY.length();
        // Determine whether the SQL ends with LIMIT int1 or LIMIT int1 OFFSET int2
        String subString = StringUtils.substring(tmp, startIndex).trim();
        if (StringUtils.isBlank(subString)) {
            log.error("Illegal SQL: {}", detailSql);
            throw new BizException("Illegal SQL:" + detailSql);
        }
        if (StringUtils.contains(subString, PAGE_KEY_2)) {
            String[] arr = StringUtils.split(subString, PAGE_KEY_2);
            if (arr == null || arr.length != 2) {
                return false;
            }

            boolean fromIsValid = DataUtils.isInteger(arr[0]) || MathFormulaUtils.isValidMathFormula(arr[0]);
            boolean offsetIsValid = DataUtils.isInteger(arr[1]) || MathFormulaUtils.isValidMathFormula(arr[1]);

            return fromIsValid && offsetIsValid;
        } else {
            return DataUtils.isInteger(subString) || MathFormulaUtils.isValidMathFormula(subString);
        }
    }
}
