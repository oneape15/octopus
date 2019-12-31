package com.oneape.octopus.service;

import com.oneape.octopus.model.DO.UserDO;

public interface AccountService {

    // 一分钟的毫秒数
    static final long ONE_MINUTE = 60 * 1000;
    // token 失效时间 一个小时(60分钟)
    static final int TOKEN_TIMEOUT = 60;

    /**
     * 根据token获取用户信息
     *
     * @param token String
     * @return UserDO
     */
    UserDO getUserInfoByToken(String token);

    /**
     * 获取当前用户
     *
     * @return UserDO
     */
    UserDO getCurrentUser();

    /**
     * 根据用户名查询用户信息
     *
     * @param username String
     * @return UserDO
     */
    UserDO getByUsername(String username);
}
