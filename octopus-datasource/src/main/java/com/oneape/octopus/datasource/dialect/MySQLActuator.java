package com.oneape.octopus.datasource.dialect;

import com.mysql.cj.MysqlType;
import com.mysql.cj.result.Field;
import com.oneape.octopus.commons.value.DataUtils;
import com.oneape.octopus.datasource.DataType;
import com.oneape.octopus.datasource.data.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
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
        String tmp = StringUtils.trimToEmpty(dataType).toUpperCase();
        return mysqlType2DataType(MysqlType.getByName(tmp));
    }

    /**
     * 将列类型转换成标准的数据类型
     *
     * @param columnType int
     * @return DataType
     */
    @Override
    DataType columnType2StandDataType(int columnType) {
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
                return DataType.VARCHAR;
            case TINYBLOB:
            case BLOB:
            case MEDIUMBLOB:
            case LONGBLOB:
            case BINARY:
            case GEOMETRY:
            case VARBINARY:
                return DataType.BINARY;
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
    String wrapperTotalSql(String detailSql) {
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
    String wrapperPageableSql(final String detailSql, int pageIndex, int pageSize) {
        if (StringUtils.isBlank(detailSql)) {
            return detailSql;
        }

        return detailSql + " " + PAGE_KEY + " " + pageIndex * pageSize + ", " + pageSize;
    }

    /**
     * 判断是否已经分页
     * 主要提防子查询中包含 LIMIT 关键字的情况
     *
     * @param detailSql String
     * @return true - 已经分页； false - 未分页
     */
    @Override
    boolean hasPageable(String detailSql) {
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
        String subString = StringUtils.substring(tmp, startIndex);
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

    /**
     * 根据不同的数据源，将值代入SQL中进行处理
     *
     * @param value Value
     * @return String
     */
    @Override
    String valueQuoting2String(Value value) {
        if (value == null || value.getValue() == null) {
            return "''";
        }
        Object val = value.getValue();
        String ret;
        switch (value.getDataType()) {
            case INTEGER:
                ret = Integer.toString((Integer) val);
                break;
            case DECIMAL:
                ret = val.toString();
                break;
            case BOOLEAN:
                ret = Boolean.toString((Boolean) val);
                break;
            case DOUBLE:
                ret = Double.toString((Double) val);
                break;
            case FLOAT:
                ret = Float.toString((Float) val);
                break;
            case LONG:
                ret = Long.toString((Long) val);
                break;
            case TIMESTAMP:
            case DATETIME:
            case VARCHAR:
            case BINARY:
            case TIME:
            case DATE:
                ret = "'" + val.toString() + "'";
                break;
            default:
                ret = val.toString();
        }

        return ret;
    }
}
