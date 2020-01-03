package com.oneape.octopus.model.DO;

import com.oneape.octopus.annotation.SortId;
import com.oneape.octopus.common.GlobalConstant;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

/**
 * 资源信息表
 */
@Data
@Builder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ResourceDO extends BaseDO {
    /**
     * 父节点
     */
    @Column(name = "parent_id")
    private Long parentId;
    /**
     * 层级, 开始为1
     */
    private Integer level;
    /**
     * 资源名称
     */
    private String name;
    /**
     * 资源图标
     */
    private String icon;
    /**
     * 0 - 菜单; 1 - 资源项
     */
    private Integer type;
    /**
     * 资源路径
     */
    private String path;
    /**
     * 权限编码
     */
    @Column(name = "auth_code")
    private String authCode;
    /**
     * 排序Id
     */
    @SortId
    @Column(name = "sort_id")
    private Long sortId = GlobalConstant.DEFAULT_VALUE;
    /**
     * 描述
     */
    private String comment;
}
