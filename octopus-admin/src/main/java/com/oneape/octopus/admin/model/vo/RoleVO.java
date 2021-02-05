package com.oneape.octopus.admin.model.vo;

import com.oneape.octopus.domain.system.RoleDO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
public class RoleVO implements Serializable {
    private Long id;
    // 角色名称
    private String name;
    // 角色编码
    private String code;
    // 类型, 角色类型: 0 - 普通; 1 - 默认角色
    private Integer type;
    // 部门
    private String department;
    // 描述
    private String comment;

    public static RoleVO ofDO(RoleDO rdo) {
        if (rdo == null) {
            return null;
        }
        RoleVO vo = new RoleVO();
        BeanUtils.copyProperties(rdo, vo);
        return vo;
    }

    public RoleDO toDO() {
        RoleDO rdo = new RoleDO();
        BeanUtils.copyProperties(this, rdo);
        return rdo;
    }

}
