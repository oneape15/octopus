package com.oneape.octopus.model.DO.system;

import com.oneape.octopus.model.DO.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RoleRlResourceDO extends BaseDO {
    /**
     * 角色id
     */
    @Column(name = "role_id")
    private Long roleId;
    /**
     * 资源Id
     */
    @Column(name = "resource_id")
    private Long resourceId;
    /**
     * 权限掩码 1 - 查看; 2 - 新增; 4 - 修改; 8 - 删除
     */
    @Column(name = "mask")
    private Integer mask;

    public RoleRlResourceDO(Long roleId, Long resourceId) {
        this.roleId = roleId;
        this.resourceId = resourceId;
    }
}
