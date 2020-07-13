package com.oneape.octopus.datasource;

import com.oneape.octopus.commons.dto.Value;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

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
     * 限制数量
     */
    private Integer limitSize = -1;
    /**
     * 需要合计数
     */
    private Boolean needTotalSize = Boolean.FALSE;

    public ExecParam() {
        this.pageIndex = -1;
        this.pageSize = -1;
    }
}
