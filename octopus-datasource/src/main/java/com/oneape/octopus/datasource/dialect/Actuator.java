package com.oneape.octopus.datasource.dialect;

import com.oneape.octopus.datasource.DataType;
import com.oneape.octopus.datasource.ExecParam;
import com.oneape.octopus.datasource.data.Result;
import com.oneape.octopus.datasource.schema.FieldInfo;
import com.oneape.octopus.datasource.schema.TableInfo;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * SQL执行器
 */
@Slf4j
public abstract class Actuator {
    // 数据库字段名称
    public static final String COL_SCHEMA = "schema_name";
    // 表字段名称
    public static final String COL_TABLE = "table_name";
    // 表类型字段名称
    public static final String COL_TABLE_TYPE = "table_type";
    // 列字段名称
    public static final String COL_COLUMN = "column_name";
    // 默认值字段名称
    public static final String COL_DEFAULT_VAL = "default_val";
    // 是否为空字段名称
    public static final String COL_NULLABLE = "nullable";
    // 数据类型字段名称
    public static final String COL_DATA_TYPE = "data_type";
    // 字符最大长度字段名称
    public static final String COL_CHAR_MAX_LEN = "max_len";
    // 是否为主键名称
    public static final String COL_PRI_KEY = "pri_key";
    // 备注字段名称
    public static final String COL_COMMENT = "comment";


    protected Statement statement;

    public Actuator(Statement statement) {
        this.statement = statement;
    }

    /**
     * 获取所有数据库名称SQL
     *
     * @return String
     */
    abstract String getAllDatabaseSql();

    /**
     * 获取所有表的执行SQL
     *
     * @return String
     */
    abstract String getAllTablesSql();

    /**
     * 获取指定数据库的表信息SQL
     *
     * @param schema String 数据库名称
     * @return String
     */
    abstract String getTablesSqlOfDb(String schema);

    /**
     * 获取所有表字段执行SQL
     *
     * @return String
     */
    abstract String getAllFieldsSql();

    /**
     * 获取指定数据库所有字段信息
     *
     * @param schema String 数据库名称
     * @return String
     */
    abstract String getFieldsSqlOfDb(String schema);

    /**
     * 获取指定表字段执行SQL
     *
     * @param schema    String 数据库名称
     * @param tableName String 表名称
     * @return String
     */
    abstract String getFieldsSqlOfTable(String schema, String tableName);

    /**
     * 将不同数据源的数据类型转换标准的数据类型
     *
     * @param dataType String
     * @return DataType
     */
    abstract DataType dialect2StandDataType(String dataType);

    /**
     * 获取所有数据库名称
     *
     * @return List
     */
    public List<String> allDatabase() {
        List<String> dbs = new ArrayList<>();
        String runSql = getAllDatabaseSql();
        try (ResultSet rs = statement.executeQuery(runSql)) {
            while (rs.next()) {
                String name = rs.getString(COL_SCHEMA);
                dbs.add(name);
            }
        } catch (SQLException e) {
            log.error("获取数据库列表失败", e);
            throw new RuntimeException("获取数据库列表失败", e);
        }
        return dbs;
    }

    /**
     * 获取所有表信息
     *
     * @return List
     */
    public List<TableInfo> allTables() {
        String runSql = getAllTablesSql();
        return getTables(runSql);
    }

    /**
     * 获取指定数据库所有表信息
     *
     * @param schema String 数据库名称
     * @return List
     */
    public List<TableInfo> allTablesOfDb(String schema) {
        String runSql = getTablesSqlOfDb(schema);
        return getTables(runSql);
    }

    /**
     * 根据Sql获取数据表信息
     *
     * @param runSql String
     * @return List
     */
    public List<TableInfo> getTables(String runSql) {
        List<TableInfo> tableInfos = new ArrayList<>();
        log.info("执行获取表信息SQL: {}", runSql);
        try (ResultSet rs = statement.executeQuery(runSql)) {
            while (rs.next()) {
                TableInfo ti = new TableInfo();
                ti.setSchema(rs.getString(COL_SCHEMA));
                ti.setName(rs.getString(COL_TABLE));
                ti.setComment(rs.getString(COL_COMMENT));
                ti.setType(rs.getInt(COL_TABLE_TYPE));
                tableInfos.add(ti);
            }
        } catch (SQLException e) {
            log.error("获取表信息失败", e);
            throw new RuntimeException("获取表信息失败", e);
        }
        return tableInfos;
    }

    /**
     * 获取所有表的字段信息
     *
     * @return List
     */
    public List<FieldInfo> allFields() {
        String runSql = getAllFieldsSql();
        return getFields(runSql);
    }

    /**
     * 获取指定数据库下所有字段信息
     *
     * @param schema String 数据库名称
     * @return List
     */
    public List<FieldInfo> allFieldsOfDb(String schema) {
        String runSql = getFieldsSqlOfDb(schema);
        return getFields(runSql);
    }

    /**
     * 获取表字段信息
     *
     * @param schema    String 数据库名称
     * @param tableName String 表名称
     * @return List
     */
    public List<FieldInfo> fieldOfTable(String schema, String tableName) {
        String runSql = getFieldsSqlOfTable(schema, tableName);
        return getFields(runSql);
    }

    public List<FieldInfo> getFields(String runSql) {
        List<FieldInfo> infos = new ArrayList<>();
        log.info("查询字段信息：{}", runSql);
        try (ResultSet rs = statement.executeQuery(runSql)) {
            while (rs.next()) {
                FieldInfo fi = new FieldInfo();
                fi.setSchema(rs.getString(COL_SCHEMA));
                fi.setTableName(rs.getString(COL_TABLE));
                fi.setPrimaryKey(rs.getInt(COL_PRI_KEY) == 1);
                fi.setName(rs.getString(COL_COLUMN));
                fi.setDefaultValue(rs.getObject(COL_DEFAULT_VAL));
                fi.setAllowNull(rs.getInt(COL_NULLABLE) == 0);
                fi.setDataType(dialect2StandDataType(rs.getString(COL_DATA_TYPE)));
                fi.setMaxLen(rs.getInt(COL_CHAR_MAX_LEN));
                fi.setComment(rs.getString(COL_COMMENT));
                infos.add(fi);
            }
        } catch (SQLException e) {
            log.error("获取表字段信息失败", e);
            throw new RuntimeException("获取表字段信息失败~", e);
        }
        return infos;
    }

    /**
     * 执行SQL操作
     *
     * @param param ExecParam
     * @return Result
     */
    public Result execSql(ExecParam param) {

        return null;
    }
}
