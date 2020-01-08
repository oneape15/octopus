package com.oneape.octopus.datasource.dialect;

import com.oneape.octopus.commons.value.DataUtils;
import com.oneape.octopus.datasource.DataType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.Statement;

/**
 * H2数据源执行器
 */
@Slf4j
public class H2Actuator extends Actuator {
    private static final String PAGE_KEY = "LIMIT";

    public H2Actuator(Statement statement) {
        super(statement);
    }

    /**
     * 获取所有数据库名称SQL
     *
     * @return String
     */
    @Override
    public String getAllDatabaseSql() {
        return "SELECT " +
                " DISTINCT TABLE_CATALOG " + COL_SCHEMA + " " +
                "FROM " +
                "INFORMATION_SCHEMA.`TABLES`";
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
        return "SELECT " +
                " TABLE_CATALOG " + COL_SCHEMA + "," +
                " TABLE_NAME " + COL_TABLE + ", " +
                " REMARKS  " + COL_COMMENT + "," +
                " 1 " + COL_TABLE_TYPE + " " +
                "FROM " +
                " INFORMATION_SCHEMA.TABLES " +
                "WHERE " +
                " TABLE_SCHEMA='PUBLIC'";
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
        String whereSql = " WHERE TABLE_SCHEMA='PUBLIC' ";
        if (StringUtils.isNotBlank(schema)) {
            whereSql += " AND TABLE_CATALOG = '" + schema + "'";
        }
        if (StringUtils.isNotBlank(tableName)) {
            whereSql += " AND TABLE_NAME = '" + tableName + "'";
        }
        return "SELECT " +
                " TABLE_CATALOG " + COL_SCHEMA + ", " +
                " TABLE_NAME " + COL_TABLE + ", " +
                " COLUMN_NAME " + COL_COLUMN + ", " +
                " COLUMN_DEFAULT " + COL_DEFAULT_VAL + ", " +
                " CASE WHEN NULLABLE = 1 THEN 0 ELSE 1 END " + COL_NULLABLE + ", " +
                " DATA_TYPE " + COL_DATA_TYPE + ", " +
                " 0 " + COL_PRI_KEY + ", " +
                " REMARKS  " + COL_COMMENT + " " +
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
    public DataType dialect2StandDataType(String dataType) {
        return h2type2StandDataType(dataType);
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
        DataType dt = null;
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
                dt = DataType.BINARY;
                break;
            case "varchar":
            case "varchar2":
            case "nvarchar":
            case "varchar_casesensitive":
            case "varchar_ignorecase":
            case "longvarchar":
            case "char":
            case "character":
            case "nchar":
                dt = DataType.STRING;
                break;
            case "other":
            default:
                dt = DataType.OBJ;
                break;
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
     * @param pageIndex int 第几页 从0开始
     * @param pageSize  int 一页显示条数
     * @return String
     */
    @Override
    public String wrapperPageableSql(String detailSql, int pageIndex, int pageSize) {
        if (StringUtils.isBlank(detailSql)) {
            return detailSql;
        }

        return detailSql + " " +
                PAGE_KEY + " " + (pageIndex * pageSize) + ", " + pageSize;
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
