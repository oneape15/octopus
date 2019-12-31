package com.oneape.octopus.common;

import com.oneape.octopus.model.DO.UserDO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class SessionThreadLocal {

    private static ThreadLocal<UserDO> local = new ThreadLocal<>();

    /**
     * 设置用户Session
     *
     * @param session UserDO
     */
    public static void setSession(UserDO session) {
        local.set(session);
    }

    /**
     * 获取用户Session
     *
     * @return UserDO
     */
    public static UserDO getSession() {
        return local.get();
    }

    /**
     * 获取用户id
     *
     * @return Long
     */
    public static Long getUserId() {
        UserDO user = local.get();
        return user == null ? null : user.getId();
    }

    /**
     * 删除Session
     * PS: 业务层面不要单独调用该方法
     */
    public static void removeSession() {
        local.remove();
    }
}
