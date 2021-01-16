package com.oneape.octopus.datasource.dialect;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.google.common.base.Preconditions;
import com.oneape.octopus.commons.cause.BizException;
import com.oneape.octopus.commons.dto.DataType;
import com.oneape.octopus.commons.dto.Value;
import com.oneape.octopus.commons.enums.FileType;
import com.oneape.octopus.commons.files.EasyCsv;
import com.oneape.octopus.commons.files.ZipUtils;
import com.oneape.octopus.commons.value.*;
import com.oneape.octopus.datasource.CellProcess;
import com.oneape.octopus.datasource.data.*;
import com.oneape.octopus.datasource.schema.SchemaTable;
import com.oneape.octopus.datasource.schema.SchemaTableField;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * The SQL actuators.
 */
@Slf4j
public abstract class Actuator {
    private static final int TAG_COUNT_SQL  = 0;
    private static final int TAG_DETAIL_SQL = 1;

    private static final String ZIP_FILE_SUFFIX = ".zip";

    // schema name
    public static final String COL_SCHEMA      = "schema_name";
    // schema table
    public static final String COL_TABLE       = "table_name";
    // schema table type
    public static final String COL_TABLE_TYPE  = "table_type";
    // column name
    public static final String COL_COLUMN      = "column_name";
    // The default column value.
    public static final String COL_DEFAULT_VAL = "default_val";
    // nullable tag
    public static final String COL_NULLABLE    = "nullable";
    // The column data type.
    public static final String COL_DATA_TYPE   = "data_type";
    // Is the primary key name
    public static final String COL_PRI_KEY     = "pri_key";
    public static final String COL_COMMENT     = "comment";
    public static final String COL_COUNT_SIZE  = "count_size";

    protected Connection conn;

    public Actuator(Connection conn) {
        this.conn = conn;
    }

    private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            10,
            30,
            5,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(50),
            new ThreadPoolExecutor.AbortPolicy()
    );

    /**
     * Get database name from the URL.
     *
     * @param url String
     * @return String
     */
    public abstract String getSchemaNameFromUrl(String url);

    /**
     * The SQL of gets all database names.
     *
     * @return String
     */
    public abstract String getAllDatabaseSql();

    /**
     * Gets the table information SQL for the specified database.
     *
     * @return String
     */
    public abstract String getTablesSql();

    /**
     * Gets all field information for the specified database.
     *
     * @return String
     */
    public abstract String getFieldsSql();

    /**
     * Gets the specified table field to execute SQL.
     *
     * @return String
     */
    public abstract String getFieldsSqlOfTable();

    /**
     * Converts data types from different data sources to standard data types.
     *
     * @param dataType String
     * @return DataType
     */
    public abstract DataType dialect2StandDataType(String dataType);

    /**
     * Converts a column type to a standard data type
     *
     * @param columnType     int
     * @param columnTypeName String
     * @return DataType
     */
    public abstract DataType columnType2StandDataType(int columnType, String columnTypeName);

    /**
     * Get the total number of SQL pieces of data
     *
     * @param detailSql String
     * @return String
     */
    public abstract String wrapperTotalSql(String detailSql);

    /**
     * Wrap paging query SQL
     *
     * @param detailSql String detail sql
     * @param pageIndex int start from 0
     * @param pageSize  int
     * @return String
     */
    public abstract String wrapperPageableSql(final String detailSql, int pageIndex, int pageSize);

    /**
     * Determine if pagination has occurred
     *
     * @param detailSql String
     * @return Boolean true
     */
    public abstract boolean hasPageable(String detailSql);

    /**
     * Depending on the data source, values are substituted into the SQL for processing
     *
     * @param value Value
     * @return String
     */
    private String valueQuoting2String(Value value) {
        if (value == null || value.getValue() == null) {
            return "''";
        }
        Object val = value.getValue();
        String ret;
        switch (value.getDataType()) {
            case INTEGER:
                ret = Integer.toString((Integer) val);
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
            case BOOLEAN:
                ret = Boolean.toString((Boolean) val);
                break;
            case TIMESTAMP:
            case DATETIME:
            case STRING:
            case TIME:
            case DATE:
                ret = "'" + val.toString() + "'";
                break;
            case DECIMAL:
            default:
                ret = val.toString();
        }

        return ret;
    }

    /**
     * Get the database name.
     *
     * @param url String
     * @return String
     */
    public String getSchema(String url) {
        if (StringUtils.isBlank(url)) {
            return null;
        }

        if (StringUtils.startsWithIgnoreCase(url, "jdbc:odps")) {
            return getSchemaNameFromUrl(url);
        }

        // Remove the parameters after the URL
        int index = StringUtils.lastIndexOf(url, "?");
        String tmp = url;
        if (index > -1) {
            tmp = StringUtils.substring(url, 0, index);
        }
        return getSchemaNameFromUrl(tmp);
    }

    /**
     * Gets all database names
     *
     * @return List
     */
    public List<String> allDatabase() {
        List<String> dbs = new ArrayList<>();
        String runSql = getAllDatabaseSql();
        if (StringUtils.isBlank(runSql)) {
            throw new RuntimeException("Running SQL is null.");
        }

        try (PreparedStatement ps = conn.prepareStatement(runSql)) {
            try (ResultSet rs = ps.executeQuery(runSql)) {
                while (rs.next()) {
                    String name = rs.getString(COL_SCHEMA);
                    dbs.add(name);
                }
            }
        } catch (SQLException e) {
            log.error("Fail to get database list.", e);
            throw new BizException("Fail to get database list.", e);
        }

        return dbs;
    }

    /**
     * Gets the specified database table information in the data source.
     *
     * @param schema String  schema name.
     * @return List
     */
    public List<SchemaTable> allTables(String schema) {
        Preconditions.checkArgument(StringUtils.isNotBlank(schema), "The schema name is empty.");
        // get the run sql.
        String runSql = getTablesSql();

        List<SchemaTable> schemaTables = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(runSql)) {
            ps.setString(1, StringUtils.trim(schema));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SchemaTable ti = new SchemaTable();
                    ti.setSchema(rs.getString(COL_SCHEMA));
                    ti.setName(rs.getString(COL_TABLE));
                    ti.setComment(rs.getString(COL_COMMENT));
                    ti.setView(rs.getInt(COL_TABLE_TYPE));
                    schemaTables.add(ti);
                }
            }
        } catch (SQLException e) {
            log.error("Failed to get table information! schema: {}", schema, e);
            throw new BizException("Failed to get table information! schema: " + schema, e);
        }
        return schemaTables;
    }

    /**
     * Gets field information for the specified database in the data source.
     *
     * @param schema String
     * @return List
     */
    public List<SchemaTableField> allFields(String schema) {
        Preconditions.checkArgument(StringUtils.isNotBlank(schema), "The schema name is empty.");
        String runSql = getFieldsSql();
        return getFields(runSql, schema);
    }

    /**
     * Gets field information for the table in the specified database in the data source.
     *
     * @param schema    String
     * @param tableName String
     * @return List
     */
    public List<SchemaTableField> fieldOfTable(String schema, String tableName) {
        Preconditions.checkArgument(StringUtils.isNotBlank(schema), "The schema name is empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(schema), "The table name is empty.");
        String runSql = getFieldsSqlOfTable();
        return getFields(runSql, schema, tableName);
    }

    private List<SchemaTableField> getFields(String runSql, String... params) {
        List<SchemaTableField> fields = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(runSql)) {

            // set the param
            int len = params.length;
            for (int i = 1; i <= len; i++) {
                ps.setString(i, params[i]);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SchemaTableField fi = new SchemaTableField();
                    fi.setSchema(rs.getString(COL_SCHEMA));
                    fi.setTableName(rs.getString(COL_TABLE));
                    fi.setPrimaryKey(rs.getInt(COL_PRI_KEY) == 1);
                    fi.setName(rs.getString(COL_COLUMN));
                    fi.setDefaultValue(rs.getObject(COL_DEFAULT_VAL));
                    fi.setAllowNull(rs.getInt(COL_NULLABLE) == 0);
                    fi.setDataType(dialect2StandDataType(rs.getString(COL_DATA_TYPE)));
                    fi.setComment(rs.getString(COL_COMMENT));
                    fields.add(fi);
                }
            }
        } catch (SQLException e) {
            log.error("Fail to get field information!", e);
            throw new BizException("Fail to get field information! ", e);
        }
        return fields;
    }

    /**
     * 1. Paging processing;
     * 2. Generate SQL to compute the total number of entries;
     *
     * @param param ExecParam
     */
    private Pair<String, String> preDealSql(ExecParam param) {

        String detailSql = param.getRawSql();

        // Assemble query SQL.
        detailSql = assembleDetailSql(detailSql, param.getParams());

        String countSql = null;
        if (param.getNeedCountSize()) {
            countSql = wrapperTotalSql(detailSql);
        }

        // Checks whether paging processing is required.
        if (param.getPageSize() != null && param.getPageSize() > 0) {
            int pageSize = param.getPageSize();
            int pageIndex = param.getPageIndex() - 1;
            if (pageIndex < 0) {
                pageIndex = 0;
            }

            // wrapper page information.
            if (!hasPageable(detailSql)) {
                detailSql = wrapperPageableSql(detailSql, pageIndex, pageSize);
            }
        }

        return new Pair<>(detailSql, countSql);
    }

    /**
     * Run DDL statements.
     *
     * @param ddlSql String
     * @return Result
     */
    public Result runSql(String ddlSql) {
        if (StringUtils.isBlank(ddlSql)) {
            return Result.ofError("The DDL sql is blank.");
        }
        try {
            try (Statement statement = conn.createStatement()) {
                statement.execute(ddlSql);
            }
            return Result.ofSuccess();
        } catch (SQLException e) {
            return Result.ofError(e.getMessage());
        }
    }

    /**
     * Perform SQL query operations.
     *
     * @param param   ExecParam
     * @param process CellProcess
     * @return Result
     */
    public Result execSql(ExecParam param, final CellProcess<Cell, Object> process) {
        // deal the run sql.
        Pair<String, String> pair = preDealSql(param);

        // save the run sql.
        Result result = new Result();
        result.setDetailSql(pair.getLeft())
                .setCountSql(pair.getRight());

        Map<Integer, String> querySqlMap = new HashMap<>();
        querySqlMap.put(TAG_DETAIL_SQL, pair.getLeft());

        boolean needCountSize = param.getNeedCountSize();
        if (needCountSize) {
            querySqlMap.put(TAG_COUNT_SQL, pair.getRight());
        }

        StopWatch watch = new StopWatch();
        watch.start();

        try {
            List<ExecuteResult> results = new ArrayList<>();
            CompletableFuture[] cfs = querySqlMap
                    .entrySet()
                    .stream()
                    .map(entry -> CompletableFuture
                            .supplyAsync(() -> fetchResult(entry.getKey(), entry.getValue(), param.getField2Alias(), process))
                            .whenComplete((ret, e) -> results.add(ret)))
                    .toArray(CompletableFuture[]::new);

            CompletableFuture.allOf(cfs).join();

            for (ExecuteResult execRet : results) {
                if (!execRet.getSuccess() && execRet.getException() != null) {
                    result.setErrMsg(execRet.getException().getMessage());
                    continue;
                }

                List<Map<String, Object>> rows = execRet.getRows();
                if (TAG_COUNT_SQL == execRet.getType() && CollectionUtils.isNotEmpty(rows)) {
                    result.setCountSize(TypeValueUtils.obj2int(rows.get(0).get(COL_COUNT_SIZE), -1));
                    continue;
                }
                if (TAG_DETAIL_SQL == execRet.getType()) {
                    result.setRows(rows);
                    result.setColumns(execRet.getColumnHeads());
                }
            }

            // fill the row length to count size
            if (!needCountSize) {
                result.setCountSize(CollectionUtils.isEmpty(result.getRows()) ? 0 : result.getRows().size());
            }

        } catch (Exception e) {
            result.setErrMsg(e.toString());
        } finally {
            watch.stop();
            result.setRunTime(watch.getTime(TimeUnit.MILLISECONDS));
        }

        return result;
    }

    private ExecuteResult fetchResult(int type, String sql, Map<String, String> field2Alias, CellProcess<Cell, Object> process) {
        ExecuteResult ret = new ExecuteResult();
        ret.setType(type);

        if (StringUtils.isBlank(sql)) return ret;

        log.debug("run sql: {}", sql);
        List<Map<String, Object>> rows = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                switch (type) {
                    case TAG_COUNT_SQL: {
                        rs.next();
                        Map<String, Object> row = new HashMap<>();
                        row.put(COL_COUNT_SIZE, rs.getInt(COL_COUNT_SIZE));
                        rows.add(row);
                    }
                    break;
                    case TAG_DETAIL_SQL: {
                        final List<ColumnHead> columnHeads = getColumnHeads(rs.getMetaData(), field2Alias);
                        int columnSize = columnHeads.size();

                        while (rs.next()) {
                            Map<String, Object> row = new HashMap<>(columnSize);
                            int i = 1;
                            for (ColumnHead ch : columnHeads) {
                                Object obj = getCell(rs, i, ch.getDataType());
                                if (process != null) {
                                    obj = process.process(new Cell(ch, obj));
                                }
                                row.put(ch.getName(), obj);
                                i++;
                            }
                            rows.add(row);
                        }
                    }
                    break;
                    default:
                        log.error("undefined the type.");
                        break;
                }
                ret.setSuccess(true);
                ret.setRows(rows);
            }
        } catch (Exception e) {
            log.error("SQL execution error, sql: {}", sql, e);
            ret.setException(e);
            ret.setSuccess(false);
        }

        return ret;
    }


    /**
     * Export data to local file.
     *
     * @param param   ExportDataParam
     * @param process CellProcess
     * @return Result
     */
    public Result exportData(ExportDataParam param, CellProcess<Cell, Object> process) {
        Result result = new Result();
        String fileName = StringUtils.isBlank(param.getTitle()) ? CodeBuilderUtils.randomStr(15) : StringUtils.trim(param.getTitle());
        Pair<String, String> pair = preDealSql(param);

        // save the run sql.
        result.setDetailSql(pair.getLeft())
                .setCountSql(pair.getRight());

        // get the file type.
        FileType fileType = param.getExportFileType() != null ? param.getExportFileType() : FileType.CSV;

        String tmpDir = SystemInfoUtils.tmpDir();
        String fullFileName = tmpDir + fileName + fileType.getSuffix();

        StopWatch watch = new StopWatch();
        watch.start();
        String detailSql = pair.getLeft();
        int status;
        try {
            if (fileType == FileType.XLS) {
                status = writeData2Xls(param, fullFileName, detailSql, result, process);
            } else {
                status = writeData2Csv(param, fullFileName, detailSql, result, process);
            }
        } finally {
            watch.stop();
            result.setRunTime(watch.getTime(TimeUnit.MILLISECONDS));
        }

        // create file error
        if (status < 0) {
            log.error("create local data file fail!");
            return result;
        }

        File file = new File(fullFileName);
        if (!file.exists()) {
            log.error("Create the local file fail! filePath: {}", fullFileName);
            return result.setErrMsg("Create the local file fail! filePath: " + fullFileName);
        }

        try {
            // Compressed file.
            String zipFileName = tmpDir + fileName + ZIP_FILE_SUFFIX;
            File zipFile = new File(zipFileName);
            ZipUtils.toZip(file, new FileOutputStream(zipFile));

            // Delete the original file.
            if (file.exists()) {
                boolean delStatus = file.delete();
                log.debug("Temporary file delete status: {}", delStatus);
            }

            return result.setExportFile(zipFileName);
        } catch (Exception e) {
            log.error("Compressed file fail!", e);
            return result.setErrMsg("Compressed file error! " + e.getMessage());
        }
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
        try (PreparedStatement ps = conn.prepareStatement(countSql)) {
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(COL_COUNT_SIZE);
            }
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
     * write data to excel file.
     *
     * @param param        ExportDataParam
     * @param fullFileName String
     * @param detailSql    String
     * @param result       Result
     * @param process      CellProcess
     * @return int
     */
    private int writeData2Xls(ExportDataParam param, String fullFileName, String detailSql, Result result, CellProcess<Cell, Object> process) {
        try (PreparedStatement ps = conn.prepareStatement(detailSql)) {
            ps.setFetchSize(1000);
            try (ResultSet rs = ps.executeQuery()) {

                ExcelWriterBuilder excelWriterBuilder = EasyExcelFactory.write(fullFileName);
                ExcelWriter writer = excelWriterBuilder.build();
                WriteSheet sheet = excelWriterBuilder.registerConverter(new Converter<BigInteger>() {
                    @Override
                    public Class supportJavaTypeKey() {
                        return BigInteger.class;
                    }

                    @Override
                    public CellDataTypeEnum supportExcelTypeKey() {
                        return CellDataTypeEnum.STRING;
                    }

                    @Override
                    public BigInteger convertToJavaData(CellData cellData, ExcelContentProperty ecp, GlobalConfiguration gc) throws Exception {
                        return BigInteger.valueOf(Long.valueOf(cellData.getData().toString()));
                    }

                    @Override
                    public CellData<String> convertToExcelData(BigInteger value, ExcelContentProperty ecp, GlobalConfiguration gc) throws Exception {
                        return new CellData<>(CellDataTypeEnum.STRING, value.toString());
                    }
                }).sheet(0, "sheet1").build();

                final List<ColumnHead> columnHeads = getColumnHeads(rs.getMetaData(), param.getField2Alias());
                result.setColumns(columnHeads);

                List<List<String>> heads = new ArrayList<>();
                columnHeads.forEach(ch -> {
                    String name = ch.getLabel();
                    List<String> column = new ArrayList<>();
                    column.add(name);
                    heads.add(column);
                });

                // write column info.
                sheet.setHead(heads);

                // loop write data.
                int index = 0;
                int totalSize = 0;
                List<List<Object>> rows = new ArrayList<>();

                while (rs.next()) {
                    totalSize++;
                    index++;

                    List<Object> row = getRowData(columnHeads, rs, process);
                    rows.add(row);

                    // check the limit size.
                    if (param.getLimitSize() != null && param.getLimitSize() > 0 && totalSize > param.getLimitSize()) {
                        break;
                    }

                    // Refresh write every 200 bars
                    if (index >= 200) {
                        index = 0;
                        writer.write(rows, sheet);
                        rows.clear();
                    }
                }

                if (CollectionUtils.isNotEmpty(rows)) {
                    writer.write(rows, sheet);
                }

                // close writer stream.
                writer.finish();
            }
        } catch (Exception e) {
            result.setErrMsg(e.getMessage());
            return 0;
        }
        return 1;
    }

    /**
     * write data to csv file.
     *
     * @param param        ExportDataParam
     * @param fullFileName String
     * @param detailSql    String
     * @param result       Result
     * @param process      CellProcess
     * @return int
     */
    private int writeData2Csv(ExportDataParam param, String fullFileName, String detailSql, Result result, CellProcess<Cell, Object> process) {
        EasyCsv easyCsv = null;
        try (PreparedStatement ps = conn.prepareStatement(detailSql)) {
            ps.setFetchSize(1000);
            try (ResultSet rs = ps.executeQuery()) {

                easyCsv = new EasyCsv(fullFileName);

                final List<ColumnHead> columnHeads = getColumnHeads(rs.getMetaData(), param.getField2Alias());
                result.setColumns(columnHeads);

                // Write the column info.
                List<Object> headString = new ArrayList<>(columnHeads.size());
                columnHeads.forEach(ch -> headString.add(ch.getLabel()));
                easyCsv.writeRow(headString);


                // loop write data.
                int index = 0;
                int totalSize = 0;
                while (rs.next()) {
                    totalSize++;
                    index++;

                    List<Object> row = getRowData(columnHeads, rs, process);

                    //  Write one line.
                    easyCsv.writeRow(row);

                    // check the limit size.
                    if (param.getLimitSize() > 0 && totalSize > param.getLimitSize()) {
                        break;
                    }

                    // Flush every 100 to disk.
                    if (index >= 100) {
                        index = 0;
                        easyCsv.flush();
                        Thread.sleep(1);
                    }
                }
            }
        } catch (Exception e) {
            result.setErrMsg(e.getMessage());
            return 0;
        } finally {
            if (easyCsv != null) {
                easyCsv.finish();
            }
        }

        return 1;
    }


    /**
     * Gets field column information.
     *
     * @param rsmd        ResultSetMetaData
     * @param field2Alias The field alias map.
     * @return list
     * @throws SQLException e
     */
    private List<ColumnHead> getColumnHeads(ResultSetMetaData rsmd, Map<String, String> field2Alias) throws SQLException {
        List<ColumnHead> heads = new ArrayList<>();
        if (rsmd == null) {
            return heads;
        }

        for (int index = 1; index <= rsmd.getColumnCount(); index++) {
            String columnLabel = StringUtils.lowerCase(rsmd.getColumnLabel(index));
            String columnName = StringUtils.lowerCase(rsmd.getColumnName(index));

            if (field2Alias != null && field2Alias.containsKey(columnName)) {
                columnLabel = field2Alias.get(columnName);
            }

            ColumnHead head = new ColumnHead(columnName, columnLabel);
            head.setDataType(columnType2StandDataType(rsmd.getColumnType(index), rsmd.getColumnTypeName(index)));
            heads.add(head);
        }

        return heads;
    }

    /**
     * Read a row of data from a ResultSet.
     *
     * @param columnHeads List
     * @param rs          ResultSet
     * @param process     CellProcess
     * @return List
     * @throws SQLException e
     */
    private List<Object> getRowData(final List<ColumnHead> columnHeads, final ResultSet rs, final CellProcess<Cell, Object> process) throws SQLException {

        List<Object> row = new ArrayList<>(columnHeads.size());

        int i = 1;
        for (ColumnHead ch : columnHeads) {

            Object obj = getCell(rs, i, ch.getDataType());

            if (process != null) {
                obj = process.process(new Cell(ch, obj));
            }

            row.add(obj);
            i++;
        }

        return row;
    }

    /**
     * Get a cell of data.
     *
     * @param rs    ResultSet
     * @param index int
     * @param dt    DataType
     * @return Object
     * @throws SQLException e
     */
    private Object getCell(ResultSet rs, int index, DataType dt) throws SQLException {
        switch (dt) {
            case STRING:
                return rs.getString(index);
            case BOOLEAN:
                return rs.getBoolean(index);
            case INTEGER:
                return rs.getInt(index);
            case DECIMAL:
                return rs.getBigDecimal(index);
            case LONG:
                return rs.getLong(index);
            case FLOAT:
                return rs.getFloat(index);
            case DOUBLE:
                return rs.getDouble(index);
            case DATE:
                Date date = rs.getDate(index);
                if (date != null) {
                    return DateUtils.formatDate(date, DateUtils.DAY_PATTERN);
                } else {
                    return null;
                }
            case TIME:
                Time time = rs.getTime(index);
                if (time != null) {
                    return time.toString();
                } else {
                    return null;
                }
            case TIMESTAMP:
                Timestamp timestamp = rs.getTimestamp(index);
                if (timestamp != null) {
                    return timestamp.toString();
                } else {
                    return null;
                }
            case DATETIME:
                Date datetime = rs.getDate(index);
                if (datetime != null) {
                    return DateUtils.formatDate(datetime, DateUtils.DATETIME_PATTERN);
                } else {
                    return null;
                }
            default:
                return rs.getObject(index);
        }
    }

    @Data
    private class ExecuteResult {
        private Integer                   type;
        private Boolean                   success;
        private List<ColumnHead>          columnHeads;
        private List<Map<String, Object>> rows;
        private Exception                 exception;

        public ExecuteResult() {
            this.success = false;
        }
    }
}
