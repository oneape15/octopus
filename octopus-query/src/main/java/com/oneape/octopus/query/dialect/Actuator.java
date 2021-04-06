package com.oneape.octopus.query.dialect;

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
import com.oneape.octopus.commons.constant.OctopusConstant;
import com.oneape.octopus.commons.dsl.*;
import com.oneape.octopus.commons.dto.*;
import com.oneape.octopus.commons.enums.DateType;
import com.oneape.octopus.commons.enums.FileType;
import com.oneape.octopus.commons.files.EasyCsv;
import com.oneape.octopus.commons.files.ZipUtils;
import com.oneape.octopus.commons.value.CodeBuilderUtils;
import com.oneape.octopus.commons.value.DataUtils;
import com.oneape.octopus.commons.value.DateUtils;
import com.oneape.octopus.commons.value.SystemInfoUtils;
import com.oneape.octopus.query.CellProcess;
import com.oneape.octopus.query.data.*;
import com.oneape.octopus.query.schema.SchemaTable;
import com.oneape.octopus.query.schema.SchemaTableField;
import com.zaxxer.hikari.pool.HikariProxyPreparedStatement;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.xmlbeans.impl.regex.REUtil;
import org.h2.table.Column;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * The SQL actuators.
 */
@Slf4j
public abstract class Actuator {
    private static final int TAG_COUNT_SQL = 0;
    private static final int TAG_DETAIL_SQL = 1;

    final DecimalFormat decimalFormat = new DecimalFormat("#0.00");

    private static final String ZIP_FILE_SUFFIX = ".zip";

    // schema name
    public static final String COL_SCHEMA = "schema_name";
    // schema table
    public static final String COL_TABLE = "table_name";
    // schema table type
    public static final String COL_TABLE_TYPE = "table_type";
    // column name
    public static final String COL_COLUMN = "column_name";
    // The default column value.
    public static final String COL_DEFAULT_VAL = "default_val";
    // nullable tag
    public static final String COL_NULLABLE = "nullable";
    // The column data type.
    public static final String COL_DATA_TYPE = "data_type";
    // Is the primary key name
    public static final String COL_PRI_KEY = "pri_key";
    public static final String COL_COMMENT = "comment";
    public static final String COL_COUNT_SIZE = "count_size";

    protected Connection conn;

    public Actuator(Connection conn) {
        this.conn = conn;
    }

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
     * There cannot be more than 5000 fields in a single table.
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
                ps.setString(i, params[i - 1]);
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
    public Result execute(String ddlSql) {
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
    public Result executeQuery(ExecParam param, final CellProcess<Cell, Object> process) {
        // deal the run sql.
        Pair<String, String> pair = preDealSql(param);

        // save the run sql.
        Result result = new Result();
        result.setDetailSql(pair.getLeft())
                .setCountSql(pair.getRight());

        StopWatch watch = new StopWatch();
        watch.start();

        try {
            List<Macro> macros = param.getMacros();
            CompareMacro compareMacro = null;
            CrossTabMacro crossTabMacro = null;
            TotalMacro totalMacro = null;

            for (Macro macro : macros) {
                if (macro.getMacroType() == MacroType.CROSS_TAB) {
                    crossTabMacro = (CrossTabMacro) macro;
                } else if (macro.getMacroType() == MacroType.COMPARE) {
                    compareMacro = (CompareMacro) macro;
                } else if (macro.getMacroType() == MacroType.TOTAL) {
                    totalMacro = (TotalMacro) macro;
                }
            }

            if (crossTabMacro != null) {
                crossTabMacroQuery(result, param, process, crossTabMacro);
            } else if (compareMacro != null) {
                compareMacroQuery(result, param, process, compareMacro);
            } else {
                normalQuery(result, param, process);
            }

            // deal total macro
            if (totalMacro != null) {
                calcTotalInfo(result, totalMacro);
            }
        } catch (Exception e) {
            result.setErrMsg(e.toString());
            result.setStatus(QueryStatus.ERROR);
        } finally {
            watch.stop();
            result.setRunTime(watch.getTime(TimeUnit.MILLISECONDS));
        }

        return result;
    }

    private void normalQuery(Result result, ExecParam param, final CellProcess<Cell, Object> process) {
        Map<Integer, String> querySqlMap = new HashMap<>();
        querySqlMap.put(TAG_DETAIL_SQL, result.getDetailInfo().getDetailSql());

        boolean needCountSize = param.getNeedCountSize();
        if (needCountSize) {
            querySqlMap.put(TAG_COUNT_SQL, result.getDetailInfo().getCountSql());
        }

        List<ExecuteResult> results = new ArrayList<>();
        CompletableFuture[] cfs = querySqlMap
                .entrySet()
                .stream()
                .map(entry -> CompletableFuture
                        .supplyAsync(() -> fetchResult(entry.getKey(), entry.getValue(), param.getParams(), process))
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
                result.setCountSize(DataUtils.obj2Integer(rows.get(0).get(COL_COUNT_SIZE), -1));
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
    }

    /**
     * Crosstab query
     *
     * @param result  Result
     * @param param   ExecParam
     * @param process CellProcess
     * @param macro   CrossTabMacro
     */
    private void compareMacroQuery(Result result, ExecParam param, final CellProcess<Cell, Object> process, CompareMacro macro) {
        if (macro.getDateType() == null || StringUtils.isAnyBlank(macro.getDateParam(), macro.getDateFormat())) {
            return;
        }

        // Get the current date.
        String currentDate = null;
        for (Value value : param.getParams()) {
            if (StringUtils.equals(value.getName(), macro.getDateParam())) {
                currentDate = DataUtils.obj2String(value.getValue(), null);
                break;
            }
        }
        if (StringUtils.isBlank(currentDate)) {
            throw new BizException("The current date is empty.");
        }

        // Get the chain rate query params.
        String chainRateDate = DateUtils.getChainRateDate(macro.getDateType(), currentDate, macro.getDateFormat());
        // Get the year on year query params.
        String yoyDate = DateUtils.getYoyDate(macro.getDateType(), currentDate, macro.getDateFormat());
        List<Value> chainRateParams = new ArrayList<>();
        List<Value> yoyParams = new ArrayList<>();
        for (Value value : param.getParams()) {
            if (StringUtils.equals(value.getName(), macro.getDateParam())) {
                Value chainRateValue = value.clone();
                chainRateValue.setValue(chainRateDate);
                chainRateParams.add(chainRateValue);

                Value yoyValue = value.clone();
                yoyValue.setValue(yoyDate);
                yoyParams.add(yoyValue);
            } else {
                chainRateParams.add(value);
                yoyParams.add(value);
            }
        }

        final int yoyTag = TAG_DETAIL_SQL + 1;
        final int chainRateTag = yoyTag + 1;
        Map<Integer, String> querySqlMap = new HashMap<>();
        querySqlMap.put(TAG_DETAIL_SQL, result.getDetailInfo().getDetailSql());
        querySqlMap.put(yoyTag, result.getDetailInfo().getDetailSql());
        querySqlMap.put(chainRateTag, result.getDetailInfo().getDetailSql());

        // total sql
        boolean needCountSize = param.getNeedCountSize();
        if (needCountSize) {
            querySqlMap.put(TAG_COUNT_SQL, result.getDetailInfo().getCountSql());
        }

        List<ExecuteResult> results = new ArrayList<>();
        CompletableFuture[] cfs = querySqlMap
                .entrySet()
                .stream()
                .map(entry -> CompletableFuture
                        .supplyAsync(() -> {
                            List<Value> params = param.getParams();
                            if (entry.getKey() == yoyTag) {
                                params = yoyParams;
                            } else if (entry.getKey() == chainRateTag) {
                                params = chainRateParams;
                            }
                            return fetchResult(entry.getKey(), entry.getValue(), params, process);
                        })
                        .whenComplete((ret, e) -> results.add(ret)))
                .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(cfs).join();

        List<ColumnHead> heads = null;
        Map<String, Map<String, Object>> currentRows = null, yoyRows = null, chainRateRows = null;

        for (ExecuteResult execRet : results) {
            if (!execRet.getSuccess() && execRet.getException() != null) {
                result.setErrMsg(execRet.getException().getMessage());
                result.setStatus(QueryStatus.ERROR);
                return;
            }

            if (TAG_COUNT_SQL == execRet.getType() && CollectionUtils.isNotEmpty(execRet.getRows())) {
                Integer totalSize = DataUtils.obj2Integer(execRet.getRows().get(0).get(COL_COUNT_SIZE), -1);
                result.setCountSize(totalSize);
                continue;
            }
            if (TAG_DETAIL_SQL == execRet.getType()) {
                heads = execRet.getColumnHeads();
                currentRows = list2Map(execRet.getRows(), macro.getRowKeys());
            } else if (yoyTag == execRet.getType()) {
                yoyRows = list2Map(execRet.getRows(), macro.getRowKeys());
            } else if (chainRateTag == execRet.getType()) {
                chainRateRows = list2Map(execRet.getRows(), macro.getRowKeys());
            }
        }

        // Gets the field you want to compare.
        Map<String, ColumnHead> calcColumnList = mergeCompareFiled(heads, macro.getCompareFiledList(), macro.getCompareFilterFiledList());

        if (currentRows == null) {
            return;
        }

        // compare
        List<Map<String, Object>> rows = new ArrayList<>();
        final Map<String, Map<String, Object>> finalYoyRows = yoyRows;
        final Map<String, Map<String, Object>> finalChainRows = chainRateRows;
        currentRows.forEach((rowKey, curRow) -> {
            Map<String, Object> row = new HashMap<>();
            curRow.forEach((k, v) -> {
                if (calcColumnList.containsKey(k)) {
                    BigDecimal value = DataUtils.obj2Decimal(v, BigDecimal.ZERO);
                    BigDecimal yoyValue = DataUtils.obj2Decimal(getCellValue(finalYoyRows, rowKey, k), BigDecimal.ZERO);
                    BigDecimal chainValue = DataUtils.obj2Decimal(getCellValue(finalChainRows, rowKey, k), BigDecimal.ZERO);
                    TabulationCell cellValue = new TabulationCell();
                    cellValue.setValue(v);
                    cellValue.setPropKV(TabulationCellPropKey.yoy.getCode(), compareValue(value, yoyValue))
                            .setPropKV(TabulationCellPropKey.rate.getCode(), compareValue(value, chainValue));
                    row.put(k, cellValue);
                } else {
                    row.put(k, v);
                }
            });
            rows.add(row);
        });
        result.setRows(rows);

        // change the compare ColumnHead dataType
        for (ColumnHead ch : heads) {
            if (calcColumnList.containsKey(ch.getName())) {
                ch.setDataType(DataType.JSON);
            }
        }
        result.setColumns(heads);

        // fill the row length to count size
        if (!needCountSize) {
            result.setCountSize(CollectionUtils.isEmpty(result.getRows()) ? 0 : result.getRows().size());
        }
    }

    /**
     * 求同环比的值
     * 如果比较值为0或为空时, 则增长值为1
     *
     * @param value        BigDecimal
     * @param compareValue BigDecimal
     * @return BigDecimal
     */
    private BigDecimal compareValue(BigDecimal value, BigDecimal compareValue) {
        if (compareValue == null || compareValue.intValue() == 0) {
            return BigDecimal.ONE;
        }

        return value.subtract(compareValue).divide(compareValue, 4, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Get the cell value
     *
     * @param rows    Map
     * @param rowKey  String
     * @param cellKey String
     * @return Object
     */
    private Object getCellValue(Map<String, Map<String, Object>> rows, String rowKey, String cellKey) {
        if (rows == null) return null;

        Map<String, Object> row = rows.getOrDefault(rowKey, null);
        if (row == null) return null;

        return row.getOrDefault(cellKey, null);
    }

    /**
     * Converts row data from a List to a Map according to the rowKey list.
     *
     * @param rows    List
     * @param rowKeys List
     * @return Map
     */
    private Map<String, Map<String, Object>> list2Map(List<Map<String, Object>> rows, List<String> rowKeys) {
        Map<String, Map<String, Object>> linkedMap = new LinkedHashMap<>();
        if (CollectionUtils.isEmpty(rowKeys)) {
            return linkedMap;
        }
        rows.forEach(row -> {
            StringBuffer sb = new StringBuffer("rk_");
            rowKeys.forEach(key -> {
                Object obj = row.getOrDefault(key, null);
                sb.append(DataUtils.obj2String(obj, "_"));
            });
            linkedMap.put(sb.toString(), row);
        });

        return linkedMap;
    }

    /**
     * Gets the field you want to compare.
     *
     * @param heads                  List<ColumnHead>
     * @param compareFiledList       List
     * @param compareFilterFiledList List
     * @return Map
     */
    private Map<String, ColumnHead> mergeCompareFiled(List<ColumnHead> heads, List<String> compareFiledList, List<String> compareFilterFiledList) {
        List<String> fields = new ArrayList<>();
        if (CollectionUtils.isEmpty(compareFiledList)) {
            heads.forEach(ch -> fields.add(ch.getName()));
        }

        if (CollectionUtils.isNotEmpty(compareFilterFiledList)) {
            fields.removeAll(compareFilterFiledList);
        }

        Map<String, ColumnHead> ret = new HashMap<>();
        for (ColumnHead ch : heads) {
            if (ch.getDataType().isNumber() && fields.contains(ch.getName())) {
                ret.put(ch.getName(), ch);
            }
        }

        return ret;
    }

    /**
     * Crosstab query
     *
     * @param result  Result
     * @param param   ExecParam
     * @param process CellProcess
     * @param macro   CrossTabMacro
     */
    private void crossTabMacroQuery(Result result, ExecParam param, final CellProcess<Cell, Object> process, CrossTabMacro macro) {
        if (param == null || macro == null || CollectionUtils.isEmpty(macro.getRowKeys())) {
            return;
        }

        String sql = result.getDetailInfo().getDetailSql();
        Map<String, Map<String, Object>> rowsMap = new LinkedHashMap<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            // set param
            fillQueryParams(ps, param.getParams());

            // execute query
            try (ResultSet rs = ps.executeQuery()) {
                final List<ColumnHead> columnHeads = getColumnHeads(rs.getMetaData());

                while (rs.next()) {
                    // get one row.
                    Map<String, Object> row = getRow(rs, columnHeads, process);

                    String crossTabRowKey = calcCrossTabRowKey(row, macro.getRowKeys());
                    String typeNameKey = DataUtils.obj2String(row.getOrDefault(macro.getTypeNameKey(), "other"));
                    Object typeNameValue = row.getOrDefault(macro.getTypeValueKey(), null);
                    if (rowsMap.containsKey(crossTabRowKey)) {
                        rowsMap.get(crossTabRowKey).put(typeNameKey, typeNameValue);
                    } else {
                        Map<String, Object> tmpMap = new HashMap<>();
                        macro.getRowKeys().forEach(k -> tmpMap.put(k, row.get(k)));
                        tmpMap.put(typeNameKey, typeNameValue);
                        rowsMap.put(crossTabRowKey, tmpMap);
                    }
                }
                result.setStatus(QueryStatus.SUCCESS);
                result.setRows((List<Map<String, Object>>) rowsMap.values());
                result.setCountSize(result.getRows().size());
                result.setCountSql(null);
            }
        } catch (Exception e) {
            log.error("SQL execution error, sql: {}", sql, e);
            result.setErrMsg(e.getMessage());
            result.setStatus(QueryStatus.ERROR);
        }
    }

    /**
     * From the ResultSet get one row
     *
     * @param rs          ResultSet
     * @param columnHeads List
     * @param process     CellProcess
     * @return Map
     * @throws SQLException e
     */
    private Map<String, Object> getRow(ResultSet rs,
                                       List<ColumnHead> columnHeads,
                                       final CellProcess<Cell, Object> process) throws SQLException {
        Map<String, Object> row = new HashMap<>(columnHeads.size());
        int i = 1;
        for (ColumnHead ch : columnHeads) {
            Object obj = getCell(rs, i, ch.getDataType());
            if (process != null) {
                obj = process.process(new Cell(ch, obj));
            }
            row.put(ch.getName(), obj);
            i++;
        }
        return row;
    }

    /**
     * calc cross table row key
     *
     * @param row     Map
     * @param rowKeys List
     * @return String
     */
    private String calcCrossTabRowKey(Map<String, Object> row, List<String> rowKeys) {
        StringBuffer sb = new StringBuffer();
        rowKeys.forEach(k -> sb.append(row.getOrDefault(k, "null")).append("_"));
        return sb.toString();
    }

    /**
     * Total bank calculation.
     *
     * @param result Result
     * @param macro  TotalMacro
     */
    private void calcTotalInfo(Result result, TotalMacro macro) {
        if (result.getStatus() != QueryStatus.SUCCESS || macro == null || CollectionUtils.isEmpty(macro.getSumFiledList())) {
            return;
        }

        Map<String, DataType> headDataTypeMap = result.getColumns()
                .stream()
                .filter(ch -> macro.getSumFiledList().contains(ch.getName()) && ch.getDataType().isNumber())
                .collect(Collectors.toMap(ColumnHead::getName, ColumnHead::getDataType, (k1, k2) -> k2));

        Map<String, Object> totalRow = new HashMap<>();

        // Calculate aggregate row information.
        result.getRows().forEach(
                row -> headDataTypeMap.forEach(
                        (key, dt) -> totalRow.put(key, sumValue(dt, row.get(key), totalRow.get(key)))
                )
        );

        // Insert total information
        result.getColumns().forEach(ch -> {
            if (!headDataTypeMap.containsKey(ch.getName())) {
                totalRow.put(ch.getName(), "-");
            }
        });
        String tagField = macro.getTagField();
        if (StringUtils.isBlank(tagField)) {
            tagField = result.getColumns().get(0).getName();
        }
        totalRow.put(tagField, "Total");

        if (macro.getInsertType() == 0) {
            result.getRows().add(0, totalRow);
        } else {
            result.getRows().add(totalRow);
        }
    }

    /**
     * Calculate and operate on different data types.
     *
     * @param dt     DataType
     * @param augend Object
     * @param addend Object
     * @return Object
     */
    private Object sumValue(DataType dt, Object augend, Object addend) {
        if (augend == null || addend == null) return null;

        switch (dt) {
            case INTEGER:
                int int1 = DataUtils.obj2Integer(augend, 0);
                int int2 = DataUtils.obj2Integer(addend, 0);
                return int1 + int2;
            case DECIMAL:
                BigDecimal bd1 = DataUtils.obj2Decimal(augend, BigDecimal.ZERO);
                BigDecimal bd2 = DataUtils.obj2Decimal(addend, BigDecimal.ZERO);
                return bd1.add(bd2);
            case LONG:
                long long1 = DataUtils.obj2Long(augend, 0L);
                long long2 = DataUtils.obj2Long(addend, 0L);
                return long1 + long2;
            case FLOAT:
                float float1 = DataUtils.obj2Float(augend, 0F);
                float float2 = DataUtils.obj2Float(addend, 0F);
                return float1 + float2;
            case DOUBLE:
                double double1 = DataUtils.obj2Double(augend, 0D);
                double double2 = DataUtils.obj2Double(addend, 0D);
                return double1 + double2;
            default:
                return null;
        }
    }

    /**
     * fetch the data by sql
     *
     * @param type    int
     * @param sql     String
     * @param params  List
     * @param process CellProcess
     * @return ExecuteResult
     */
    private ExecuteResult fetchResult(int type, String sql, List<Value> params, CellProcess<Cell, Object> process) {
        ExecuteResult ret = new ExecuteResult();
        ret.setType(type);

        if (StringUtils.isBlank(sql)) return ret;

        List<Map<String, Object>> rows = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            // set param
            fillQueryParams(ps, params);

            // execute query
            try (ResultSet rs = ps.executeQuery()) {
                if (type == TAG_COUNT_SQL) {
                    rs.next();
                    Map<String, Object> row = new HashMap<>();
                    row.put(COL_COUNT_SIZE, rs.getInt(COL_COUNT_SIZE));
                    rows.add(row);
                } else {
                    final List<ColumnHead> columnHeads = getColumnHeads(rs.getMetaData());
                    while (rs.next()) {
                        Map<String, Object> row = getRow(rs, columnHeads, process);
                        rows.add(row);
                    }
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
     * The parameters are populated into the PreparedStatement.
     *
     * @param ps     PreparedStatement
     * @param params List
     * @throws SQLException e
     */
    private void fillQueryParams(PreparedStatement ps, List<Value> params) throws SQLException {
        int index = 1;
        for (Value value : params) {
            switch (value.getDataType()) {
                case BOOLEAN:
                    ps.setBoolean(index, DataUtils.obj2Boolean(value.getValue()));
                    break;
                case INTEGER:
                    ps.setInt(index, DataUtils.obj2Integer(value.getValue()));
                    break;
                case DECIMAL:
                    ps.setBigDecimal(index, DataUtils.obj2Decimal(value.getValue()));
                    break;
                case LONG:
                    ps.setLong(index, DataUtils.obj2Long(value.getValue()));
                    break;
                case FLOAT:
                    ps.setFloat(index, DataUtils.obj2Float(value.getValue()));
                    break;
                case DOUBLE:
                    ps.setDouble(index, DataUtils.obj2Double(value.getValue()));
                    break;
                case DATE:
                case TIME:
                case TIMESTAMP:
                case DATETIME:
                case STRING:
                    ps.setString(index, DataUtils.obj2String(value.getValue()));
                    break;
            }
            index++;
        }
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
                WriteSheet sheet = excelWriterBuilder
                        .registerConverter(new Converter<BigInteger>() {
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
                                return BigInteger.valueOf(Long.parseLong(cellData.getData().toString()));
                            }

                            @Override
                            public CellData<String> convertToExcelData(BigInteger value, ExcelContentProperty ecp, GlobalConfiguration gc) throws Exception {
                                return new CellData<>(CellDataTypeEnum.STRING, value.toString());
                            }
                        })
                        .registerConverter(new Converter<Timestamp>() {
                            @Override
                            public Class supportJavaTypeKey() {
                                return Timestamp.class;
                            }

                            @Override
                            public CellDataTypeEnum supportExcelTypeKey() {
                                return CellDataTypeEnum.STRING;
                            }

                            @Override
                            public Timestamp convertToJavaData(CellData cellData, ExcelContentProperty ecp, GlobalConfiguration gc) throws Exception {
                                return Timestamp.valueOf(cellData.getData().toString());
                            }

                            @Override
                            public CellData convertToExcelData(Timestamp value, ExcelContentProperty ecp, GlobalConfiguration gc) throws Exception {
                                return new CellData<>(CellDataTypeEnum.STRING, value.toString());
                            }
                        })
                        .sheet(0, "sheet1").build();

                final List<ColumnHead> columnHeads = getColumnHeads(rs.getMetaData());
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

                final List<ColumnHead> columnHeads = getColumnHeads(rs.getMetaData());
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
     * @param rsmd ResultSetMetaData
     * @return list
     * @throws SQLException e
     */
    private List<ColumnHead> getColumnHeads(ResultSetMetaData rsmd) throws SQLException {
        List<ColumnHead> heads = new ArrayList<>();
        if (rsmd == null) {
            return heads;
        }

        for (int index = 1; index <= rsmd.getColumnCount(); index++) {
            String columnLabel = StringUtils.lowerCase(rsmd.getColumnLabel(index));
            String columnName = StringUtils.lowerCase(rsmd.getColumnName(index));

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
    private static class ExecuteResult {
        private Integer type;
        private Boolean success;
        private List<ColumnHead> columnHeads;
        private List<Map<String, Object>> rows;
        private Exception exception;

        public ExecuteResult() {
            this.success = false;
        }
    }
}
