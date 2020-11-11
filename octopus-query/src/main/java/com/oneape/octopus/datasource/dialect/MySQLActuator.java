package com.oneape.octopus.datasource.dialect;

import com.mysql.cj.MysqlType;
import com.oneape.octopus.commons.cause.BizException;
import com.oneape.octopus.commons.dto.DataType;
import com.oneape.octopus.commons.value.DataUtils;
import com.oneape.octopus.commons.value.MathFormulaUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;

/**
 * The MySQL data source executor
 */
@Slf4j
public class MySQLActuator extends Actuator {

    private static final String PAGE_KEY = "LIMIT";

    public MySQLActuator(Connection conn) {
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
        String startString = "jdbc:mysql://";
        if (!StringUtils.startsWithIgnoreCase(url, startString)) {
            throw new RuntimeException("Not a valid MySQL connection address: " + url);
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
                " CASE TABLE_TYPE " +
                "   WHEN UPPER(TABLE_TYPE) = 'BASE TABLE' THEN 0 ELSE 1" +
                "  END AS " + COL_TABLE_TYPE + " " +
                "FROM  information_schema.TABLES  " +
                "WHERE TABLE_SCHEMA != 'information_schema' AND TABLE_SCHEMA = ? " +
                "ORDER BY TABLE_SCHEMA";
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
                " CASE IS_NULLABLE " +
                "   WHEN UPPER( IS_NULLABLE ) = 'YES' THEN 1 ELSE 0 END " + COL_NULLABLE + ", " +
                " DATA_TYPE " + COL_DATA_TYPE + ", " +
                " IF ( COLUMN_KEY = 'PRI', 1, 0 ) " + COL_PRI_KEY + ", " +
                " COLUMN_COMMENT  " + COL_COMMENT + " " +
                "FROM information_schema.COLUMNS " +
                "WHERE TABLE_SCHEMA = ? " +
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
                " TABLE_SCHEMA " + COL_SCHEMA + ", " +
                " TABLE_NAME " + COL_TABLE + ", " +
                " COLUMN_NAME " + COL_COLUMN + ", " +
                " COLUMN_DEFAULT " + COL_DEFAULT_VAL + ", " +
                " CASE IS_NULLABLE " +
                "   WHEN UPPER( IS_NULLABLE ) = 'YES' THEN 1 ELSE 0 END " + COL_NULLABLE + ", " +
                " DATA_TYPE " + COL_DATA_TYPE + ", " +
                " IF ( COLUMN_KEY = 'PRI', 1, 0 ) " + COL_PRI_KEY + ", " +
                " COLUMN_COMMENT  " + COL_COMMENT + " " +
                "FROM information_schema.COLUMNS " +
                "WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ? " +
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
        String tmp = StringUtils.trimToEmpty(dataType).toUpperCase();
        return mysqlType2DataType(MysqlType.getByName(tmp));
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
        MysqlType mt = MysqlType.getByJdbcType(columnType);
        return mysqlType2DataType(mt);
    }

    /**
     * Converts the mysqlType to the DataType
     *
     * @param mt MysqlType
     * @return DataType
     */
    private DataType mysqlType2DataType(MysqlType mt) {
        switch (mt) {
            case BIT:
            case INT:
            case TINYINT:
            case SMALLINT:
            case MEDIUMINT:
            case INT_UNSIGNED:
            case TINYINT_UNSIGNED:
            case SMALLINT_UNSIGNED:
            case MEDIUMINT_UNSIGNED:
                return DataType.INTEGER;
            case SET:
            case ENUM:
            case CHAR:
            case VARCHAR:
            case JSON:
            case TINYTEXT:
            case TEXT:
            case MEDIUMTEXT:
            case LONGTEXT:
            case TINYBLOB:
            case BLOB:
            case MEDIUMBLOB:
            case LONGBLOB:
            case BINARY:
            case GEOMETRY:
            case VARBINARY:
                return DataType.STRING;
            case TIME:
                return DataType.TIME;
            case DATE:
            case YEAR:
                return DataType.DATE;
            case TIMESTAMP:
                return DataType.TIMESTAMP;
            case DATETIME:
                return DataType.DATETIME;
            case FLOAT:
            case FLOAT_UNSIGNED:
                return DataType.FLOAT;
            case DOUBLE:
            case DOUBLE_UNSIGNED:
                return DataType.DOUBLE;
            case BOOLEAN:
                return DataType.BOOLEAN;
            case DECIMAL:
            case DECIMAL_UNSIGNED:
                return DataType.DECIMAL;
            case BIGINT:
            case BIGINT_UNSIGNED:
                return DataType.LONG;
            case NULL:
            case UNKNOWN:
            default:
                return null;
        }
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
    public String wrapperPageableSql(final String detailSql, int pageIndex, int pageSize) {
        if (StringUtils.isBlank(detailSql)) {
            return detailSql;
        }

        if (hasPageable(detailSql)) {
            return detailSql;
        }

        return detailSql + " " + PAGE_KEY + " " + (pageIndex * pageSize) + ", " + pageSize;
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

        // Determines whether the SQL ends with LIMIT int1 or LIMIT int1, int2
        String subString = StringUtils.substring(tmp, startIndex).trim();
        if (StringUtils.isBlank(subString)) {
            log.error("Illegal SQL: {}", detailSql);
            throw new BizException("Illegal SQL:" + detailSql);
        }

        String[] arr = StringUtils.split(subString, ",");
        if (arr == null || arr.length > 2) {
            return false;
        }

        boolean fromIsValid = DataUtils.isInteger(arr[0]) || MathFormulaUtils.isValidMathFormula(arr[0]);
        if (arr.length == 1) {
            return fromIsValid;
        } else if (arr.length == 2) {
            boolean offsetIsValid = DataUtils.isInteger(arr[1]) || MathFormulaUtils.isValidMathFormula(arr[1]);
            return fromIsValid && offsetIsValid;
        }

        return false;
    }
}
