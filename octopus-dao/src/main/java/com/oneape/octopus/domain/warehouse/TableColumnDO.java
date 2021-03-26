package com.oneape.octopus.domain.warehouse;

import com.oneape.octopus.commons.annotation.EntityColumn;
import com.oneape.octopus.domain.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-05-28 11:38.
 * Modify:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TableColumnDO extends BaseDO {

    @EntityColumn(name = "datasource_id")
    private Long   datasourceId;
    /**
     * the table name
     */
    @EntityColumn(name = "table_name")
    private String tableName;

    private String  name;
    /**
     * the column alias, default is null
     */
    private String  alias;
    /**
     * eg. INTEGER, FLOAT, STRING, DECIMAL ...
     */
    @EntityColumn(name = "data_type")
    private String  dataType;
    /**
     * 0 - normal column ; 1 - primary key; 2 - foreign key
     */
    private Integer classify;
    /**
     * the column use time;
     */
    private Long    heat;

    /**
     * 0 - normal, 1 - has drop
     */
    private Integer status;

    private String comment;
}
