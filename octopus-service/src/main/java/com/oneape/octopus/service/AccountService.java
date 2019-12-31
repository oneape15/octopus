package com.oneape.octopus.service;

import com.oneape.octopus.model.DO.UserDO;

public interface AccountService {

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
}
