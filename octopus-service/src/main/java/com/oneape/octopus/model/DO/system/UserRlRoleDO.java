package com.oneape.octopus.model.DO.system;

import com.oneape.octopus.model.DO.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserRlRoleDO extends BaseDO {
    @Column(name = "user_id")
    private Long    userId;
    @Column(name = "role_id")
    private Long    roleId;
    // 权限掩码 1 - 查看; 2 - 新增; 4 - 修改; 8 - 删除
    private Integer mask;

    public UserRlRoleDO(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}
