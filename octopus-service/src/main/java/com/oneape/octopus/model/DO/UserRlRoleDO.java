package com.oneape.octopus.model.DO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserRlRoleDO extends BaseDO {
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "role_id")
    private Long roleId;

    public UserRlRoleDO(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}