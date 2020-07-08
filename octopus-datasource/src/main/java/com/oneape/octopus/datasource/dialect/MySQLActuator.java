package com.oneape.octopus.datasource.dialect;

import com.mysql.cj.MysqlType;
import com.oneape.octopus.commons.value.DataUtils;
import com.oneape.octopus.datasource.data.DataType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.Statement;

/**
 * MySQL数据源执行器
 */
@Slf4j
public class MySQLActuator extends Actuator {

    private static final String PAGE_KEY = "LIMIT";

    public MySQLActuator(Statement statement) {
        super(statement);
    }

    /**
     * 从URL中获取数据库名称
     *
     * @param url String
     * @return String
     */
    @Override
    public String getSchemaNameFromUrl(String url) {
        String startString = "jdbc:mysql://";
        if (!StringUtils.startsWithIgnoreCase(url, startString)) {
            throw new RuntimeException("不是合法的MySQL连接地址: " + url);
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
     * 获取所有数据库名称SQL
     *
     * @return String
     */
    @Override
    public String getAllDatabaseSql() {
        return "SELECT " +
                " DISTINCT TABLE_SCHEMA " + COL_SCHEMA + " " +
                "FROM " +
                "information_schema.TABLES";
    }

    /**
     * 获取所有表的执行SQL
     *
     * @return String
     */
    @Override
    public String getAllTablesSql() {
        return getTablesSqlOfDb(null);
    }

    /**
     * 获取指定数据库的表信息SQL
     *
     * @param schema String 数据库名称
     * @return String
     */
    @Override
    public String getTablesSqlOfDb(String schema) {
        String whereSql = " WHERE TABLE_SCHEMA != 'information_schema'";
        if (StringUtils.isNotBlank(schema)) {
            whereSql += "  AND TABLE_SCHEMA = '" + schema + "' ";
        }
        return "SELECT " +
                " TABLE_SCHEMA " + COL_SCHEMA + "," +
                " TABLE_NAME " + COL_TABLE + "," +
                " TABLE_COMMENT  " + COL_COMMENT + ", " +
                " CASE TABLE_TYPE " +
                "   WHEN UPPER(TABLE_TYPE) = 'BASE TABLE' THEN 0 ELSE 1" +
                "  END AS " + COL_TABLE_TYPE + " " +
                "FROM " +
                " information_schema.TABLES  " +
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
    public String getAllFieldsSql() {
        return getFieldsSqlOfTable(null, null);
    }

    /**
     * 获取指定数据库所有字段信息
     *
     * @param schema String 数据库名称
     * @return String
     */
    @Override
    public String getFieldsSqlOfDb(String schema) {
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
    public String getFieldsSqlOfTable(String schema, String tableName) {
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
                " IF ( COLUMN_KEY = 'PRI', 1, 0 ) " + COL_PRI_KEY + ", " +
                " COLUMN_COMMENT  " + COL_COMMENT + " " +
                "FROM information_schema.COLUMNS " +
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
    public DataType dialect2StandDataType(String dataType) {
        String tmp = StringUtils.trimToEmpty(dataType).toUpperCase();
        return mysqlType2DataType(MysqlType.getByName(tmp));
    }

    /**
     * 将列类型转换成标准的数据类型
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
     * MysqlType 转换成 DataType
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
     * 获取数据总条数SQL
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
     * 进行分页处理
     *
     * @param detailSql String 详细sql
     * @param pageIndex int 第几页 从0开始
     * @param pageSize  int 一页显示条数
     * @return String
     */
    @Override
    public String wrapperPageableSql(final String detailSql, int pageIndex, int pageSize) {
        if (StringUtils.isBlank(detailSql)) {
            return detailSql;
        }

        return detailSql + " " +
                PAGE_KEY + " " + (pageIndex * pageSize) + ", " + pageSize;
    }

    /**
     * 判断是否已经分页
     * 主要提防子查询中包含 LIMIT 关键字的情况
     *
     * @param detailSql String
     * @return true - 已经分页； false - 未分页
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
        // 判断SQL是否以 LIMIT int1 或 LIMIT int1, int2结束
        String subString = StringUtils.substring(tmp, startIndex).trim();
        if (StringUtils.isBlank(subString)) {
            log.error("不合法的SQL: {}", detailSql);
            throw new RuntimeException("不合法的SQL:" + detailSql);
        }

        String[] arr = StringUtils.split(subString, ",");
        if (arr == null || arr.length > 2) {
            return false;
        }

        if (arr.length == 1) {
            return DataUtils.isInteger(arr[0]);
        } else if (arr.length == 2) {
            return DataUtils.isInteger(arr[0]) && DataUtils.isInteger(arr[1]);
        }

        return false;
    }
}
