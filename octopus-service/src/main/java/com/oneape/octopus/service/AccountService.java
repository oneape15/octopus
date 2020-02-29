package com.oneape.octopus.service;

import com.oneape.octopus.model.DO.system.UserDO;
import com.oneape.octopus.model.VO.MenuVO;
import com.oneape.octopus.model.VO.UserVO;

import java.util.List;
import java.util.Map;

public interface AccountService extends BaseService<UserDO> {

    // 一分钟的毫秒数
    static final long   ONE_MINUTE       = 60 * 1000;
    // token 失效时间 一个小时(60分钟)
    static final int    TOKEN_TIMEOUT    = 60;
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
     * 获取当前用户的前端菜单列表
     *
     * @return List
     */
    List<MenuVO> getCurrentMenus();

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
     * 重置用户密码
     *
     * @param userId Long
     * @return int
     */
    int resetPwd(Long userId);

    /**
     * 获取用户资源操作权限
     *
     * @param userId Long
     * @return Map
     */
    Map<String, List<Integer>> getResOptPermission(Long userId);

    /**
     * 创建用户
     *
     * @param user UserVO
     * @return int
     */
    int addUser(UserVO user);

    /**
     * 获取用户列表
     *
     * @param user UserDO
     * @return List
     */
    List<UserVO> find(UserDO user);

    /**
     * 删除用户列表
     *
     * @param userIds List
     * @return int
     */
    int removeUsers(List<Long> userIds);
}
