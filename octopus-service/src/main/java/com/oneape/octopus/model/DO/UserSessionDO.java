package com.oneape.octopus.model.DO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserSessionDO extends BaseDO {
    /**
     * 用户Id
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;
    /**
     * 会话token
     */
    @Column(nullable = false)
    private String token;
    /**
     * 登录时间
     */
    @Column(name = "login_time", nullable = false)
    private Long loginTime;
    /**
     * token失效时间
     */
    private Integer timeout = 30;

    public UserSessionDO(Long userId, String token) {
        this.userId = userId;
        this.token = token;
    }
}
