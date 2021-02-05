package com.oneape.octopus.admin.service.system;

import com.oneape.octopus.domain.system.UserDO;
import com.oneape.octopus.dto.system.AppType;
import com.oneape.octopus.dto.system.UserDTO;
import com.oneape.octopus.dto.system.UserStatus;
import com.oneape.octopus.admin.service.BaseService;

import java.util.List;
import java.util.Map;

public interface AccountService extends BaseService<UserDO> {

    long   ONE_MINUTE       = 60 * 1000;
    int    TOKEN_TIMEOUT    = 60;
    String TOKEN_INFO_SPLIT = "<@>";

    /**
     * Get user information according to token
     *
     * @param token String
     * @return UserVO
     */
    UserDTO getUserInfoByToken(String token);

    /**
     * Get the current user.
     *
     * @return UserDTO
     */
    UserDTO getCurrentUser();

    /**
     * Get full user information.
     *
     * @param userId Long
     * @return UserDTO
     */
    UserDTO getFullInformationById(Long userId);

    /**
     * Get the current user id.
     *
     * @return Long
     */
    Long getCurrentUserId();

    /**
     * Query user information by user name.
     *
     * @param username String
     * @return UserDO
     */
    UserDO getByUsername(String username);

    /**
     * The user login option.
     *
     * @param username String
     * @param password String
     * @param appType  AppType
     * @return String login token value.
     */
    String login(String username, String password, AppType appType);

    /**
     * the user login out option.
     *
     * @param userId  Long
     * @param appType AppType
     * @return int 0 - fail; 1 - success;
     */
    int logout(Long userId, AppType appType);

    /**
     * Reset user password.
     *
     * @param userId Long
     * @return int
     */
    int resetPwd(Long userId);

    /**
     * Gets user resource action permissions
     *
     * @param roleIds List
     * @return Map
     */
    Map<String, List<Integer>> getResOptPermission(List<Long> roleIds);

    /**
     * Change the user status
     *
     * @param userId Long userId
     * @param status UserStatus
     * @return 1 - success; 0 - fail.
     */
    int changeUserStatus(Long userId, UserStatus status);

    /**
     * save user role.
     *
     * @param userId  Long
     * @param roleIds List
     * @return 1 - success; 0 - fail.
     */
    int saveUserRole(Long userId, List<Long> roleIds);
}
