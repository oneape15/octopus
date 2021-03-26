package com.oneape.octopus.domain.system;

import com.oneape.octopus.commons.annotation.EntityColumn;
import com.oneape.octopus.domain.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2021-01-25 15:22.
 * Modify:
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserRlOrgDO extends BaseDO {
    @EntityColumn(name = "user_id")
    private Long userId;
    @EntityColumn(name = "org_id")
    private Long orgId;

    public UserRlOrgDO(Long userId, Long orgId) {
        this.userId = userId;
        this.orgId = orgId;
    }
}
