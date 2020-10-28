package com.oneape.octopus.model.domain.system;

import com.oneape.octopus.commons.enums.EntityColumn;
import com.oneape.octopus.model.domain.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
    @EntityColumn(name = "user_id", nullable = false)
    private Long    userId;
    /**
     * User login token.
     */
    @EntityColumn(nullable = false)
    private String  token;
    /**
     * Logon time
     */
    @EntityColumn(name = "login_time", nullable = false)
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
