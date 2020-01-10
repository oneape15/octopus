package com.oneape.octopus.model.DO.system;

import com.oneape.octopus.model.DO.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;

/**
 * 基础信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CommonInfoDO extends BaseDO {
    /**
     * 父分类
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 基础信息分类名称
     */
    private String classify;
    /**
     * 编码信息
     */
    private String code;
    /**
     * 名称
     */
    private String name;
}
