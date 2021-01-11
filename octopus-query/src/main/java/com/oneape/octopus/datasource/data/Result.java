package com.oneape.octopus.datasource.data;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Query data results
 */
@Data
public class Result implements Serializable {
    // query total sql key
    public static final String KEY_TOTAL_SQL   = "total_sql";
    // query sql key
    public static final String KEY_DETAIL_SQL  = "detail_sql";
    // error info message key
    public static final String KEY_ERR_MSG     = "err_msg";
    // run time
    public static final String KEY_RUN_TIME    = "run_time";
    // Export file path
    public static final String KEY_EXPORT_FILE = "export_file";

    /**
     * query status
     */
    private QueryStatus status = QueryStatus.SUCCESS;

    /**
     * The table column information
     */
    private List<ColumnHead>          columns;
    /**
     * The data rows
     */
    private List<Map<String, Object>> rows;
    /**
     * total size
     */
    private Integer                   totalSize;
    /**
     * Attached information.
     */
    private Map<String, String> detailInfo = new HashMap<>();

    /**
     * query status enum
     */
    public enum QueryStatus {
        SUCCESS,
        TIMEOUT,
        ERROR;
    }

    public static Result ofError(String errMsg) {
        Result ret = new Result();
        ret.setErrMsg(errMsg);
        ret.setStatus(QueryStatus.ERROR);
        return ret;
    }

    public static Result ofSuccess() {
        Result ret = new Result();
        ret.setStatus(QueryStatus.SUCCESS);
        return ret;
    }

    public boolean isSuccess() {
        return status == QueryStatus.SUCCESS;
    }

    public Result setDetailSql(String detailSql) {
        detailInfo.put(KEY_DETAIL_SQL, detailSql);
        return this;
    }

    public Result setTotalSql(String totalSql) {
        detailInfo.put(KEY_TOTAL_SQL, totalSql);
        return this;
    }

    public Result setErrMsg(String msg) {
        detailInfo.put(KEY_ERR_MSG, msg);
        return this;
    }

    public Result setRunTime(Long runTime) {
        detailInfo.put(KEY_RUN_TIME, runTime + "");
        return this;
    }

    public Result setExportFile(String filePath) {
        detailInfo.put(KEY_EXPORT_FILE, filePath);
        return this;
    }

}
