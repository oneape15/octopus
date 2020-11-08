package com.oneape.octopus.domain.system;

import com.oneape.octopus.commons.enums.EntityColumn;
import com.oneape.octopus.domain.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * User - role association table DO.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserRlRoleDO extends BaseDO {
    @EntityColumn(name = "user_id")
    private Long userId;
    @EntityColumn(name = "role_id")
    private Long roleId;

    public UserRlRoleDO(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}
