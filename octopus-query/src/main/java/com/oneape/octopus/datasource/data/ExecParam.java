package com.oneape.octopus.datasource.data;

import com.oneape.octopus.commons.dto.Value;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Execute SQL parameters.
 */
@Data
public class ExecParam implements Serializable {
    /**
     * Native SQL
     */
    private String      rawSql;
    /**
     * The parameters is That Replace native SQL "?".
     */
    private List<Value> params;
    /**
     * Page number, starting at 1.
     */
    private Integer     pageIndex;
    /**
     * Number of entries per page.
     */
    private Integer     pageSize;
    /**
     * The limit size.
     * An exception is thrown if the number of queries is greater than the limit.
     */
    private Integer limitSize     = -1;
    /**
     * Whether need total size.
     */
    private Boolean needCountSize = Boolean.FALSE;
    /**
     * Field alias list.
     */
    private Map<String, String> field2Alias;

    public ExecParam() {
        this.pageIndex = -1;
        this.pageSize = -1;
    }
}
