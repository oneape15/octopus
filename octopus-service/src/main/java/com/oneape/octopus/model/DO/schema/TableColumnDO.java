package com.oneape.octopus.model.DO.schema;

import com.oneape.octopus.model.DO.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-05-28 11:38.
 * Modify:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TableColumnDO extends BaseDO {

    @Column(name = "datasource_id")
    private String datasourceId;
    /**
     * the table name
     */
    @Column(name = "table_name")
    private String tableName;

    private String  name;
    /**
     * the column alias, default is null
     */
    private String  alias;
    /**
     *  eg. INTEGER, FLOAT, STRING, DECIMAL ...
     */
    @Column(name = "data_type")
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
