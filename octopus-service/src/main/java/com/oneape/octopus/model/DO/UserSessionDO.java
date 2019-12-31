package com.oneape.octopus.model.DO;

import lombok.Data;

@Data
public class UserSessionDO extends BaseDO {
    /**
     * 用户Id
     */
    private Long userId;
    /**
     * 会话token
     */
    private String token;
    /**
     * 登录时间
     */
    private Long loginTime;
    /**
     * token失效时间
     */
    private Integer timeout = 30;
}
