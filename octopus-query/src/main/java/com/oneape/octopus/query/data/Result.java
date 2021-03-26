package com.oneape.octopus.query.data;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Query data results
 */
@Data
public class Result implements Serializable {

    /**
     * query status
     */
    private QueryStatus status = QueryStatus.SUCCESS;

    /**
     * The table column information
     */
    private List<ColumnHead> columns;
    /**
     * The data rows
     */
    private List<Map<String, Object>> rows;
    /**
     * total size
     */
    private Integer countSize;
    /**
     * Attached information.
     */
    private DetailInfo detailInfo = new DetailInfo();

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
        detailInfo.setDetailSql(detailSql);
        return this;
    }

    public Result setCountSql(String countSql) {
        detailInfo.setCountSql(countSql);
        return this;
    }

    public Result setErrMsg(String msg) {
        detailInfo.setErrMsg(msg);
        return this;
    }

    public Result setRunTime(Long runTime) {
        detailInfo.setRunTime(runTime);
        return this;
    }

    public Result setExportFile(String filePath) {
        detailInfo.setExportFile(filePath);
        return this;
    }

}
