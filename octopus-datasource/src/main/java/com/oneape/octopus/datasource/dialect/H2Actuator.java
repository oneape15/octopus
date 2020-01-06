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
    String getAllDatabaseSql() {
        return null;
    }

    /**
     * 获取所有表的执行SQL
     *
     * @return String
     */
    @Override
    String getAllTablesSql() {
        return null;
    }

    /**
     * 获取指定数据库的表信息SQL
     *
     * @param schema String 数据库名称
     * @return String
     */
    @Override
    String getTablesSqlOfDb(String schema) {
        return null;
    }

    /**
     * 获取所有表字段执行SQL
     *
     * @return String
     */
    @Override
    String getAllFieldsSql() {
        return null;
    }

    /**
     * 获取指定数据库所有字段信息
     *
     * @param schema String 数据库名称
     * @return String
     */
    @Override
    String getFieldsSqlOfDb(String schema) {
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
    String getFieldsSqlOfTable(String schema, String tableName) {
        return null;
    }

    /**
     * 将不同数据源的数据类型转换标准的数据类型
     *
     * @param dataType String
     * @return DataType
     */
    @Override
    DataType dialect2StandDataType(String dataType) {
        return null;
    }
}
