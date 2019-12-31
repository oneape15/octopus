package com.oneape.octopus.service.impl;

import com.oneape.octopus.common.BizException;
import com.oneape.octopus.common.SessionThreadLocal;
import com.oneape.octopus.common.StateCode;
import com.oneape.octopus.mapper.UserMapper;
import com.oneape.octopus.mapper.UserSessionMapper;
import com.oneape.octopus.model.DO.UserDO;
import com.oneape.octopus.model.DO.UserSessionDO;
import com.oneape.octopus.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserSessionMapper userSessionMapper;

    /**
     * 根据token获取用户信息
     *
     * @param token String
     * @return UserDO
     */
    @Override
    public UserDO getUserInfoByToken(String token) {
        Assert.isTrue(StringUtils.isNotBlank(token), "会话Token为空");
        // 根据token获取用户信息
        UserSessionDO us = userSessionMapper.findByToken(token);

        // 验证token
        if (us == null) {
            throw new BizException(StateCode.Unauthorized.getCode(), "无效的Token");
        }

        Long loginTime = us.getLoginTime();

        int timeout = us.getTimeout();
        if (timeout <= 0) {
            timeout = TOKEN_TIMEOUT; // 默认60分钟失效
        }
        if (loginTime == null || loginTime + timeout * ONE_MINUTE < System.currentTimeMillis()) {
            throw new BizException(StateCode.Unauthorized.getCode(), "登录令牌已失效, 请重新登录");
        }

        return userMapper.findById(us.getUserId());
    }

    /**
     * 获取当前用户
     *
     * @return UserDO
     */
    @Override
    public UserDO getCurrentUser() {
        return SessionThreadLocal.getSession();
    }

    /**
     * 根据用户名查询用户信息
     *
     * @param username String
     * @return UserDO
     */
    @Override
    public UserDO getByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            return null;
        }

        return userMapper.getByUsername(username);
    }
}
