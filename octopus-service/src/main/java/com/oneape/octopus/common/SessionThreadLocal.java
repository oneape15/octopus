package com.oneape.octopus.common;

import com.oneape.octopus.model.VO.UserVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class SessionThreadLocal {

    private static ThreadLocal<UserVO> local = new ThreadLocal<>();

    /**
     * 设置用户Session
     *
     * @param session UserDO
     */
    public static void setSession(UserVO session) {
        local.set(session);
    }

    /**
     * 获取用户Session
     *
     * @return UserDO
     */
    public static UserVO getSession() {
        return local.get();
    }

    /**
     * 获取用户id
     *
     * @return Long
     */
    public static Long getUserId() {
        UserVO user = local.get();
        return user == null ? null : user.getId();
    }

    public static Long getUserIdOfDefault(Long defaultVal) {
        Long useId = getUserId();
        return useId == null ? defaultVal : useId;
    }

    /**
     * 删除Session
     * PS: 业务层面不要单独调用该方法
     */
    public static void removeSession() {
        local.remove();
    }
}
