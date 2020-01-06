package com.oneape.octopus.datasource.data;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 查询数据结果
 */
@Data
public class Result implements Serializable {

    /**
     * 查询状态
     */
    private QueryStatus status = QueryStatus.SUCCESS;

    /**
     * 数据头信息
     */
    private List<ColumnHead> columns;
    /**
     * 数据行数
     */
    private List<Object[]> rows;
    /**
     * 总数据条数
     */
    private Integer totalSize;
    /**
     * 运行信息
     */
    private Map<String, String> runInfo;

    /**
     * 查询状态
     */
    enum QueryStatus {
        // 成功
        SUCCESS,
        // 超时
        TIMEOUT,
        // 出错
        ERROR;
    }

}
