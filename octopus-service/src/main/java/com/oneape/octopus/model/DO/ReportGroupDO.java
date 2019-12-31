package com.oneape.octopus.model.DO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 报表组
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ReportGroupDO extends BaseDO {
    /**
     * 报表组名称
     */
    private String name;
    /**
     * 报表组图标
     */
    private String icon;
    /**
     * 状态 0 - 正常； 1 - 上线中
     */
    private String status;
    /**
     * 拥有者
     */
    private Long owner;
    /**
     * 描述信息
     */
    private String comment;
}
