package com.oneape.octopus.service;

import com.oneape.octopus.model.VO.UserVO;

public interface AccountService {

    // 一分钟的毫秒数
    static final long ONE_MINUTE = 60 * 1000;
    // token 失效时间 一个小时(60分钟)
    static final int TOKEN_TIMEOUT = 60;
    // token原生信息分隔字符串
    static final String TOKEN_INFO_SPLIT = "<@>";

    /**
     * 根据token获取用户信息
     *
     * @param token String
     * @return UserVO
     */
    UserVO getUserInfoByToken(String token);

    /**
     * 获取当前用户
     *
     * @return UserVO
     */
    UserVO getCurrentUser();

    /**
     * 获取当前用户Id
     *
     * @return Long
     */
    Long getCurrentUserId();

    /**
     * 根据用户名查询用户信息
     *
     * @param username String
     * @return UserVO
     */
    UserVO getByUsername(String username);

    /**
     * 用户登录操作
     *
     * @param username String
     * @param password String
     * @return UserVO
     */
    UserVO login(String username, String password);

    /**
     * 创建用户
     *
     * @param user UserVO
     * @return int
     */
    int addUser(UserVO user);
}
