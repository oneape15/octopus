package com.oneape.octopus.datasource.dialect;

import com.oneape.octopus.datasource.DataType;
import com.oneape.octopus.datasource.ExecParam;
import com.oneape.octopus.datasource.data.ColumnHead;
import com.oneape.octopus.datasource.data.Result;
import com.oneape.octopus.datasource.data.Value;
import com.oneape.octopus.datasource.schema.FieldInfo;
import com.oneape.octopus.datasource.schema.TableInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
    // 是否为主键名称
    public static final String COL_PRI_KEY = "pri_key";
    // 备注字段名称
    public static final String COL_COMMENT = "comment";
    // 总条数字段名称
    public static final String COL_COUNT_SIZE = "count_size";


    protected Statement statement;

    public Actuator(Statement statement) {
        this.statement = statement;
    }

    /**
     * 获取所有数据库名称SQL
     *
     * @return String
     */
    public abstract String getAllDatabaseSql();

    /**
     * 获取所有表的执行SQL
     *
     * @return String
     */
    public abstract String getAllTablesSql();

    /**
     * 获取指定数据库的表信息SQL
     *
     * @param schema String 数据库名称
     * @return String
     */
    public abstract String getTablesSqlOfDb(String schema);

    /**
     * 获取所有表字段执行SQL
     *
     * @return String
     */
    public abstract String getAllFieldsSql();

    /**
     * 获取指定数据库所有字段信息
     *
     * @param schema String 数据库名称
     * @return String
     */
    public abstract String getFieldsSqlOfDb(String schema);

    /**
     * 获取指定表字段执行SQL
     *
     * @param schema    String 数据库名称
     * @param tableName String 表名称
     * @return String
     */
    public abstract String getFieldsSqlOfTable(String schema, String tableName);

    /**
     * 将不同数据源的数据类型转换标准的数据类型
     *
     * @param dataType String
     * @return DataType
     */
    public abstract DataType dialect2StandDataType(String dataType);

    /**
     * 将列类型转换成标准的数据类型
     *
     * @param columnType     int
     * @param columnTypeName String
     * @return DataType
     */
    public abstract DataType columnType2StandDataType(int columnType, String columnTypeName);

    /**
     * 获取数据总条数SQL
     *
     * @param detailSql String
     * @return String
     */
    public abstract String wrapperTotalSql(String detailSql);

    /**
     * 进行分页处理
     *
     * @param detailSql String 详细sql
     * @param pageIndex int 第几页 从0开始
     * @param pageSize  int 一页显示条数
     * @return String
     */
    public abstract String wrapperPageableSql(final String detailSql, int pageIndex, int pageSize);

    /**
     * 判断是否已经分页
     *
     * @param detailSql String
     * @return true - 已经分页； false - 未分页
     */
    public abstract boolean hasPageable(String detailSql);

    /**
     * 根据不同的数据源，将值代入SQL中进行处理
     *
     * @param value Value
     * @return String
     */
    public String valueQuoting2String(Value value) {
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
            case STRING:
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


    /**
     * 获取所有数据库名称
     *
     * @return List
     */
    public List<String> allDatabase() {
        List<String> dbs = new ArrayList<>();
        String runSql = getAllDatabaseSql();
        if (StringUtils.isBlank(runSql)) {
            throw new RuntimeException("运行SQL为空");
        }
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
        Result result = new Result();

        if (param == null) {
            result.getRunInfo().put(Result.KEY_ERR_MSG, "查询参数为空");
            return result;
        }

        String detailSql = param.getRawSql();
        if (StringUtils.isBlank(detailSql)) {
            result.getRunInfo().put(Result.KEY_ERR_MSG, "运行SQL为空");
            return result;
        }

        // 组装查询SQL
        detailSql = assembleDetailSql(detailSql, param.getParams());

        /*
         * 获取数据总条数
         */
        boolean needTotalSize = param.getNeedTotalSize();
        int countSize = -1;
        if (needTotalSize) {
            String countSql = wrapperTotalSql(detailSql);
            result.getRunInfo().put(Result.KEY_TOTAL_SQL, countSql);
            try {
                countSize = getTotalSizeBySql(countSql);
            } catch (Exception e) {
                result.getRunInfo().put(Result.KEY_ERR_MSG, e.toString());
                return result;
            }
        }
        result.setTotalSize(countSize);

        // 判断是否为分页
        if (param.getPageSize() != null && param.getPageSize() > 0) {
            int pageSize = param.getPageSize();
            int pageIndex = param.getPageIndex() - 1;
            if (pageIndex < 0) {
                pageIndex = 0;
            }
            boolean hasPageable = hasPageable(detailSql);
            // 未进行分页设置
            if (!hasPageable) {
                detailSql = wrapperPageableSql(detailSql, pageIndex, pageSize);
            }
        }

        // 设置查询SQL
        result.getRunInfo().put(Result.KEY_DETAIL_SQL, detailSql);

        StopWatch watch = new StopWatch();
        watch.start();
        try (ResultSet rs = statement.executeQuery(detailSql)) {
            // 得到 ResultSetMetaData 对象
            List<ColumnHead> columnHeads = getColumnHeads(rs.getMetaData());
            result.setColumns(columnHeads);
            int columnSize = columnHeads.size();

            List<Object[]> rows = new ArrayList<>();
            // 循环获取数据行
            while (rs.next()) {
                Object[] row = new Object[columnSize];
                for (int i = 0; i < columnSize; i++) {
                    row[i] = rs.getObject(i + 1);
                }
                rows.add(row);
            }
            result.setRows(rows);
        } catch (Exception e) {
            result.getRunInfo().put(Result.KEY_ERR_MSG, e.toString());
        } finally {
            watch.stop();
            result.getRunInfo().put(Result.KYE_RUN_TIME, watch.getTime() + "");
        }

        return result;
    }

    /**
     * 获取数据总条数
     *
     * @param countSql String
     * @return int -1 - 未获取到
     */
    private int getTotalSizeBySql(String countSql) {
        if (StringUtils.isBlank(countSql)) {
            throw new RuntimeException("执行TOTAL查询语句为空");
        }
        try (ResultSet rs = statement.executeQuery(countSql)) {
            rs.next();
            return rs.getInt(COL_COUNT_SIZE);
        } catch (Exception e) {
            throw new RuntimeException("获取数据总条数异常~", e);
        }
    }

    /**
     * 组装查询SQL
     *
     * @param rawSql String
     * @param params List
     * @return String
     */
    private String assembleDetailSql(String rawSql, List<Value> params) {
        if (CollectionUtils.isEmpty(params)) {
            return rawSql;
        }

        for (Value val : params) {
            String tmp = valueQuoting2String(val);
            rawSql = StringUtils.replaceOnce(rawSql, "?", tmp);
        }
        return rawSql;
    }

    /**
     * 获取字段列信息
     *
     * @param rsmd ResultSetMetaData
     * @return list
     * @throws SQLException e
     */
    private List<ColumnHead> getColumnHeads(ResultSetMetaData rsmd) throws SQLException {
        List<ColumnHead> heads = new ArrayList<>();
        if (rsmd == null) {
            return heads;
        }

        for (int i = 0; i < rsmd.getColumnCount(); i++) {
            int index = i + 1;
            String columnLabel = rsmd.getColumnLabel(index);
            String columnName = rsmd.getColumnName(index);
            ColumnHead head = new ColumnHead(columnName, columnLabel);
            head.setDataType(columnType2StandDataType(rsmd.getColumnType(index), rsmd.getColumnTypeName(index)));
            heads.add(head);
        }

        return heads;
    }
}
