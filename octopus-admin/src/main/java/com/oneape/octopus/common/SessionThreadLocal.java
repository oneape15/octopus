package com.oneape.octopus.common;

import com.oneape.octopus.model.dto.system.UserDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class SessionThreadLocal {

    private static ThreadLocal<UserDTO> local = new ThreadLocal<>();

    /**
     * Set user Session.
     *
     * @param session UserDTO
     */
    public static void setSession(UserDTO session) {
        local.set(session);
    }

    /**
     * Get user Session.
     *
     * @return UserDTO
     */
    public static UserDTO getSession() {
        return local.get();
    }

    /**
     * Get current user id.
     *
     * @return Long
     */
    public static Long getUserId() {
        UserDTO user = local.get();
        return user == null ? null : user.getId();
    }

    public static Long getUserIdOfDefault(Long defaultVal) {
        Long useId = getUserId();
        return useId == null ? defaultVal : useId;
    }

    /**
     * Deleted the user Session.
     * PS: Do not call this method on its own at the business level.
     */
    public static void removeSession() {
        local.remove();
    }
}
