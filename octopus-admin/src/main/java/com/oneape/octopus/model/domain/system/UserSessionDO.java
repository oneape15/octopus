package com.oneape.octopus.model.domain.system;

import com.oneape.octopus.model.domain.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

/**
 * User session information table DO.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserSessionDO extends BaseDO {
    /**
     * The user primary key.
     */
    @Column(name = "user_id", nullable = false)
    private Long    userId;
    /**
     * User login token.
     */
    @Column(nullable = false)
    private String  token;
    /**
     * Logon time
     */
    @Column(name = "login_time", nullable = false)
    private Long    loginTime;
    /**
     * Token failure time.
     */
    private Integer timeout;

    public UserSessionDO(Long userId, String token) {
        this.userId = userId;
        this.token = token;
    }
}
