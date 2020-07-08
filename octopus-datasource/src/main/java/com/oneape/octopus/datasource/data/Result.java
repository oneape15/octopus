package com.oneape.octopus.datasource.data;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询数据结果
 */
@Data
public class Result implements Serializable {
    // 查询总条数sql key
    public static final String KEY_TOTAL_SQL   = "total_sql";
    // 查询sql key
    public static final String KEY_DETAIL_SQL  = "detail_sql";
    // 错误信息key
    public static final String KEY_ERR_MSG     = "err_msg";
    // 运行时间
    public static final String KYE_RUN_TIME    = "run_time";
    // 导出文件路径
    public static final String KYE_EXPORT_FILE = "export_file";


    /**
     * 查询状态
     */
    private QueryStatus status = QueryStatus.SUCCESS;

    /**
     * 数据头信息
     */
    private List<ColumnHead>          columns;
    /**
     * 数据行数
     */
    private List<Map<String, Object>> rows;
    /**
     * 总数据条数
     */
    private Integer                   totalSize;
    /**
     * 运行信息
     */
    private Map<String, String> runInfo = new HashMap<>();

    /**
     * 查询状态
     */
    public enum QueryStatus {
        // 成功
        SUCCESS,
        // 超时
        TIMEOUT,
        // 出错
        ERROR;
    }

    public boolean isSuccess() {
        return status == QueryStatus.SUCCESS;
    }

}
