package com.oneape.octopus.model.DO.system;

import com.oneape.octopus.common.MaskUtils;
import com.oneape.octopus.model.DO.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

/**
 * Role - resource association table DO.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RoleRlResourceDO extends BaseDO {
    /**
     * The role id.
     */
    @Column(name = "role_id")
    private Long    roleId;
    /**
     * The resource id
     */
    @Column(name = "resource_id")
    private Long    resourceId;
    /**
     * {@link MaskUtils}
     * Permissions mask 0 - blank; 1 - View; 2 - Add; 4 - Modification; 8 - Delete;
     */
    @Column(name = "mask")
    private Integer mask;

    public RoleRlResourceDO(Long roleId, Long resourceId) {
        this.roleId = roleId;
        this.resourceId = resourceId;
    }
}
