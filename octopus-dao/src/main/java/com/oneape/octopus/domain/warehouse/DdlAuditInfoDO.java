package com.oneape.octopus.domain.warehouse;

import com.oneape.octopus.commons.enums.DdlAuditType;
import com.oneape.octopus.commons.annotation.EntityColumn;
import com.oneape.octopus.domain.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2021-02-25 16:04.
 * Modify:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DdlAuditInfoDO extends BaseDO {
    /**
     * ddl audit name
     */
    private String       name;
    /**
     * the data source id
     */
    @EntityColumn(name = "datasource_id")
    private Long         datasourceId;
    /**
     * The ddl information
     */
    @EntityColumn(name = "ddl_text")
    private String       ddlText;
    /**
     * audit status
     * {@link DdlAuditType}
     */
    private DdlAuditType status;
    /**
     * auditor
     */
    private Long         auditor;
    /**
     * executor
     */
    private Long         executor;
    /**
     * The audit opinion
     */
    private String       opinion;
    private String       comment;
}
