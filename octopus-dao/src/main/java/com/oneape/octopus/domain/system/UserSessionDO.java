package com.oneape.octopus.domain.system;

import com.oneape.octopus.commons.annotation.EntityColumn;
import com.oneape.octopus.domain.BaseDO;
import com.oneape.octopus.dto.system.AppType;
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
     * The application type.
     *
     * @see AppType
     */
    @EntityColumn(name = "app_type", nullable = false)
    private Integer appType;
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
     * User login time
     */
    @EntityColumn(name = "login_time", nullable = false)
    private Long    loginTime;
    /**
     * User logout time.
     */
    @EntityColumn(name = "logout_time")
    private Long    logoutTime;
    /**
     * Token expiration time.
     */
    @EntityColumn(name = "expire_at")
    private Long    expireAt;

    public UserSessionDO(Long userId, String token) {
        this.userId = userId;
        this.token = token;
    }
}
