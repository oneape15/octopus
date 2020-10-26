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
    public static final String KYE_RUN_TIME    = "run_time";
    // Export file path
    public static final String KYE_EXPORT_FILE = "export_file";


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
     * run information
     */
    private Map<String, String> runInfo = new HashMap<>();

    /**
     * query status enum
     */
    public enum QueryStatus {
        SUCCESS,
        TIMEOUT,
        ERROR;
    }

    public boolean isSuccess() {
        return status == QueryStatus.SUCCESS;
    }

}
