package com.oneape.octopus.datasource;

import com.oneape.octopus.datasource.data.Value;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 查询参数
 */
@Data
public class ExecParam implements Serializable {
    /**
     * 原生SQL
     */
    private String rawSql;
    /**
     * 查询参数
     */
    private List<Value> params;
    /**
     * 第几页
     */
    private Integer pageIndex;
    /**
     * 一页条数
     */
    private Integer pageSize;
    /**
     * 需要合计数
     */
    private Boolean needTotalSize = Boolean.FALSE;

    public ExecParam() {
        this.pageIndex = -1;
        this.pageSize = -1;
    }
}
