package com.oneape.octopus.domain.system;

import com.oneape.octopus.commons.annotation.EntityColumn;
import com.oneape.octopus.commons.value.MaskUtils;
import com.oneape.octopus.domain.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
    @EntityColumn(name = "role_id", nullable = false)
    private Long    roleId;
    /**
     * The resource id
     */
    @EntityColumn(name = "resource_id", nullable = false)
    private Long    resourceId;
    /**
     * {@link MaskUtils}
     * Permissions mask 0 - blank; 1 - View; 2 - Add; 4 - Modification; 8 - Delete;
     */
    @EntityColumn(name = "mask")
    private Integer mask;

    public RoleRlResourceDO(Long roleId, Long resourceId) {
        this.roleId = roleId;
        this.resourceId = resourceId;
    }
}
