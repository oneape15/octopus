package com.oneape.octopus.service.system;

import com.oneape.octopus.model.domain.system.UserDO;
import com.oneape.octopus.model.dto.system.UserDTO;
import com.oneape.octopus.service.BaseService;

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
     * @return UserDTO
     */
    UserDTO login(String username, String password);

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
     * Add new User
     *
     * @param user UserDO
     * @return int
     */
    int addUser(UserDO user);

    /**
     * Delete user list.
     *
     * @param userIds List
     * @return int
     */
    int removeUsers(List<Long> userIds);
}
