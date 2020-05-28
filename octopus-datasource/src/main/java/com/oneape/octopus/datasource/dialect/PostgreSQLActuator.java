package com.oneape.octopus.datasource.dialect;

import com.oneape.octopus.commons.value.DataUtils;
import com.oneape.octopus.datasource.DataType;
import com.oneape.octopus.datasource.data.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.Statement;

/**
 * PostgreSql数据源执行器
 */
@Slf4j
public class PostgreSQLActuator extends Actuator {

    private static final String PAGE_KEY = "LIMIT";
    private static final String PAGE_KEY_2 = "OFFSET";

    public PostgreSQLActuator(Statement statement) {
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
        String startString = "jdbc:postgresql://";
        if (!StringUtils.startsWithIgnoreCase(url, startString)) {
            throw new RuntimeException("不是合法的PostgreSQL连接地址: " + url);
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
                "  datname " + COL_SCHEMA +
                " FROM  pg_database " +
                "  ORDER BY datname";
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
        String whereSql = "";
        if (StringUtils.isNotBlank(schema)) {
            whereSql = " AND a.schemaname ='" + schema + "'";
        }
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
                "  AND a.tablename NOT LIKE 'sql_%'   " + whereSql +
                "ORDER BY  " +
                "  a.schemaname, a.tablename";
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
        return getFieldsSqlOfTable(null, null);
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
        String whereSql = " ";
        if (StringUtils.isNotBlank(schema)) {
            whereSql += " AND pg_tables.schemaname = '" + schema + "' ";
        }
        if (StringUtils.isNotBlank(tableName)) {
            whereSql += " AND pg_class.relname = '" + tableName + "' ";
        }
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
                "  pg_tables.tablename = pg_class.relname   " +
                whereSql +
                "  AND pg_attribute.attrelid = pg_class.oid   " +
                "  AND pg_tables.tablename NOT LIKE'pg_%'   " +
                "  AND pg_tables.tablename NOT LIKE'sql_%'   " +
                "  AND pg_attribute.attnum > 0   " +
                "ORDER BY pg_tables.schemaname, pg_class.relname";
    }

    /**
     * 将不同数据源的数据类型转换标准的数据类型
     *
     * @param dataType String
     * @return DataType
     */
    @Override
    public DataType dialect2StandDataType(String dataType) {
        return pgsqlType2StandDataType(dataType);
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
                log.warn("未匹配到相应的数据类型---> {}", type);
                dt = DataType.OBJ;
        }
        return dt;
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
     * @param pageIndex int 第几页
     * @param pageSize  int 一页显示条数
     * @return String
     */
    @Override
    public String wrapperPageableSql(String detailSql, int pageIndex, int pageSize) {
        if (StringUtils.isBlank(detailSql)) {
            return detailSql;
        }
        return detailSql + " " +
                PAGE_KEY + " " + pageSize + " " + PAGE_KEY_2 + " " + (pageIndex * pageSize);
    }

    /**
     * 判断是否已经分页
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
        // 判断SQL是否以 LIMIT int1 或 LIMIT int1 OFFSET int2结束
        String subString = StringUtils.substring(tmp, startIndex).trim();
        if (StringUtils.isBlank(subString)) {
            log.error("不合法的SQL: {}", detailSql);
            throw new RuntimeException("不合法的SQL:" + detailSql);
        }
        if (StringUtils.contains(subString, PAGE_KEY_2)) {
            String[] arr = StringUtils.split(subString, PAGE_KEY_2);
            if (arr == null || arr.length != 2) {
                return false;
            }
            return DataUtils.isInteger(arr[0]) && DataUtils.isInteger(arr[1]);
        } else {
            return DataUtils.isInteger(subString);
        }
    }

    /**
     * 根据不同的数据源，将值代入SQL中进行处理
     *
     * @param value Value
     * @return String
     */
    @Override
    public String valueQuoting2String(Value value) {
        log.info("---->pgsql----> deal");
        return super.valueQuoting2String(value);
    }
}
