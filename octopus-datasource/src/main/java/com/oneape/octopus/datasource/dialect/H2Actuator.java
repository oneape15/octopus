package com.oneape.octopus.datasource.dialect;

import com.oneape.octopus.datasource.DataType;
import lombok.extern.slf4j.Slf4j;

import java.sql.Statement;

/**
 * H2数据源执行器
 */
@Slf4j
public class H2Actuator extends Actuator {

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
        return null;
    }

    /**
     * 获取所有表字段执行SQL
     *
     * @return String
     */
    @Override
    public String getAllFieldsSql() {
        return null;
    }

    /**
     * 获取指定数据库所有字段信息
     *
     * @param schema String 数据库名称
     * @return String
     */
    @Override
    public String getFieldsSqlOfDb(String schema) {
        return null;
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
        return null;
    }

    /**
     * 将不同数据源的数据类型转换标准的数据类型
     *
     * @param dataType String
     * @return DataType
     */
    @Override
    public DataType dialect2StandDataType(String dataType) {
        return null;
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
        return null;
    }

    /**
     * 获取数据总条数SQL
     *
     * @param detailSql String
     * @return String
     */
    @Override
    public String wrapperTotalSql(String detailSql) {
        return null;
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
        return null;
    }

    /**
     * 判断是否已经分页
     *
     * @param detailSql String
     * @return true - 已经分页； false - 未分页
     */
    @Override
    public boolean hasPageable(String detailSql) {
        return false;
    }
}
