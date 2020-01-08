package com.oneape.octopus.model.DO.report;

import com.oneape.octopus.annotation.Creator;
import com.oneape.octopus.annotation.SortId;
import com.oneape.octopus.common.GlobalConstant;
import com.oneape.octopus.model.DO.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

/**
 * 报表组
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ReportGroupDO extends BaseDO {
    /**
     * 父节点Id
     */
    @Column(name = "parent_id", nullable = false)
    private Long parentId = GlobalConstant.DEFAULT_VALUE;
    /**
     * 报表组名称
     */
    @Column(nullable = false)
    private String name;
    /**
     * 报表组图标
     */
    private String icon;
    /**
     * 状态 0 - 正常； 1 - 上线中
     */
    private Integer status;
    /**
     * 所在层级
     */
    private Integer level;
    /**
     * 拥有者
     */
    @Creator
    private Long owner;
    /**
     * 排序Id
     */
    @SortId
    @Column(name = "sort_id")
    private Long sortId = GlobalConstant.DEFAULT_VALUE;
    /**
     * 描述信息
     */
    private String comment;

    public ReportGroupDO(Long parentId, String name) {
        this.parentId = parentId;
        this.name = name;
    }
}
