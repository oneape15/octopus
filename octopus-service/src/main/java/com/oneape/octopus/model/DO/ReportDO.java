package com.oneape.octopus.model.DO;

import com.oneape.octopus.model.enums.ReportType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 报表信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ReportDO extends BaseDO {
    /**
     * 名称
     */
    private String name;
    /**
     * 图标
     */
    private String icon;
    /**
     * 报表类型
     */
    private ReportType reportType;
    /**
     * 数据源Id
     */
    private String datasourceId;
    /**
     * 查询SQL
     */
    private String rawSql;
    /**
     * 拥有者
     */
    private Long owner;
}
