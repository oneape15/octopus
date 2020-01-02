package com.oneape.octopus.model.DO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RoleDO extends BaseDO {
    /**
     * 角色名称
     */
    private String name;
    /**
     * 角色编码
     */
    private String code;
    /**
     * 类型, 角色类型: 0 - 普通; 1 - 默认角色
     */
    private Integer type;
    /**
     * 部门
     */
    private String department;
    /**
     * 描述
     */
    private String comment;

    public RoleDO(String name) {
        this.name = name;
    }
}
