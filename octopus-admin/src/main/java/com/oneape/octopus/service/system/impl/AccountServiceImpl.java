package com.oneape.octopus.service.system.impl;

import com.google.common.base.Preconditions;
import com.oneape.octopus.commons.cause.BizException;
import com.oneape.octopus.commons.cause.UnauthorizedException;
import com.oneape.octopus.commons.constant.OctopusConstant;
import com.oneape.octopus.commons.security.MD5Utils;
import com.oneape.octopus.commons.value.CodeBuilderUtils;
import com.oneape.octopus.commons.value.MaskUtils;
import com.oneape.octopus.controller.SessionThreadLocal;
import com.oneape.octopus.domain.system.RoleDO;
import com.oneape.octopus.domain.system.UserDO;
import com.oneape.octopus.domain.system.UserSessionDO;
import com.oneape.octopus.dto.system.ResourceDTO;
import com.oneape.octopus.dto.system.UserDTO;
import com.oneape.octopus.mapper.system.UserMapper;
import com.oneape.octopus.mapper.system.UserSessionMapper;
import com.oneape.octopus.service.system.AccountService;
import com.oneape.octopus.service.system.MailService;
import com.oneape.octopus.service.system.ResourceService;
import com.oneape.octopus.service.system.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private UserMapper        userMapper;
    @Resource
    private UserSessionMapper userSessionMapper;

    @Resource
    private RoleService     roleService;
    @Resource
    private ResourceService resourceService;
    @Resource
    private MailService     mailService;

    /**
     * save data to table.
     * <p>
     * If the Model property ID is not null, the update operation is performed, or the insert operation is performed。
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int save(UserDO model) {
        Preconditions.checkNotNull(model, "The user information is null.");
        Preconditions.checkArgument(StringUtils.isNotBlank(model.getUsername()), "The username is empty.");

        if (model.getId() != null) {
            // the username and password can't edit.
            model.setUsername(null);
            model.setPassword(null);

            return userMapper.update(model);
        }

        Preconditions.checkArgument(userMapper.sameNameCheck(model.getUsername(), null) == 0, "The username already exists.");
        String rawPwd = model.getPassword();
        if (StringUtils.isBlank(rawPwd)) {
            rawPwd = CodeBuilderUtils.RandmonStr(6);
        }
        String pwdMd5 = MD5Utils.saltUserPassword(model.getUsername(), rawPwd, null);
        model.setPassword(pwdMd5);
        int status = userMapper.insert(model);
        if (status > 0) {
            model.setPassword(rawPwd);
        }

        return status;
    }

    /**
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int edit(UserDO model) {
        return userMapper.update(model);
    }

    /**
     * Get the model information by the primary key.
     *
     * @param id Long
     * @return T
     */
    @Override
    public UserDO findById(Long id) {
        return userMapper.findById(id);
    }

    /**
     * Delete by primary key Id.
     *
     * @param id Long
     * @return int 1 - success; 0 - fail.
     */
    @Transactional
    @Override
    public int deleteById(Long id) {
        Preconditions.checkNotNull(id, "The user id is empty.");

        int status = userMapper.delete(new UserDO(id));
        // Delete the relationship between the user and the role
        if (status > 0) {
            roleService.deleteRelationshipWithUserId(id);
        }

        return status;
    }

    /**
     * Get user information according to token
     *
     * @param token String
     * @return UserDTO
     */
    @Override
    public UserDTO getUserInfoByToken(String token) {
        Preconditions.checkArgument(StringUtils.isNotBlank(token), "Session Token is null.");
        // Get user information according to token
        UserSessionDO us = userSessionMapper.findByToken(token);

        // Authentication token
        if (us == null) {
            throw new UnauthorizedException("Invalid Token");
        }

        Long loginTime = us.getLoginTime();

        int timeout = us.getTimeout();
        if (timeout <= 0) {
            // The default is 60 minutes
            timeout = TOKEN_TIMEOUT;
        }
        if (loginTime == null || loginTime + timeout * ONE_MINUTE < System.currentTimeMillis()) {
            throw new UnauthorizedException("Login timeout, please login again");
        }

        UserDO udo = userMapper.findById(us.getUserId());
        if (udo == null) {
            throw new UnauthorizedException("The user does not exist or is locked.");
        }

        // get user full information.
        UserDTO dto = getFullInformationById(udo.getId());
        dto.setToken(token);

        return dto;
    }

    /**
     * Get the current user
     *
     * @return UserDTO
     */
    @Override
    public UserDTO getCurrentUser() {
        return SessionThreadLocal.getSession();
    }

    /**
     * Get full user information.
     *
     * @param userId Long
     * @return UserDTO
     */
    @Override
    public UserDTO getFullInformationById(Long userId) {
        UserDO udo = userMapper.findById(userId);
        if (udo == null) {
            throw new UnauthorizedException("The user does not exist or is locked.");
        }

        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(udo, dto);

        // Get the own role.
        List<RoleDO> roles = roleService.findRoleByUserId(userId);
        dto.setRoles(roles);

        List<Long> roleIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(roles)) {
            roles.forEach(r -> roleIds.add(r.getId()));
        }

        // Gets the resource action permission.
        Map<String, List<Integer>> optPermission = getResOptPermission(roleIds);
        dto.setPermissions(optPermission);

        return dto;
    }

    /**
     * Get the current user id.
     *
     * @return Long
     */
    @Override
    public Long getCurrentUserId() {
        UserDTO user = SessionThreadLocal.getSession();
        if (user == null) {
            return OctopusConstant.SYS_USER;
        }
        return user.getId();
    }

    /**
     * Query user information by user name.
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

    /**
     * The user login option.
     *
     * @param username String
     * @param password String
     * @return UserDTO
     */
    @Transactional
    @Override
    public UserDTO login(String username, String password) {
        UserDO udo = getByUsername(username);
        if (udo == null || !StringUtils.equals(password, udo.getPassword())) {
            throw new BizException("Error username or password.");
        }

        // crate a new token.
        String token = createUserSessionToken(udo.getId());
        if (StringUtils.isBlank(token)) {
            throw new BizException("Login token generation failed. Please try again");
        }

        // Get user full information.
        UserDTO dto = getFullInformationById(udo.getId());
        dto.setToken(token);

        return dto;
    }

    /**
     * Reset user password.
     *
     * @param userId Long
     * @return int
     */
    @Override
    public int resetPwd(Long userId) {
        Preconditions.checkNotNull(userId, "The user id is empty.");
        UserDO user = Preconditions.checkNotNull(userMapper.findById(userId), "The user information is null.");

        // Randomly generate a password.
        String pwd = CodeBuilderUtils.RandmonStr(6);
        String pwdMd5 = MD5Utils.saltUserPassword(user.getUsername(), pwd, null);
        user.setPassword(pwdMd5);
        int status = userMapper.update(user);
        if (status > 0) {

            // The password has been changed and an email must be sent to the appropriate user.
            try {
                String template_name = "templates/email/user-reset-pwd.html";
                String content = getFileContent(template_name);

                // The reason for not using {@link MessageFormat.format} here is that the string {display:none} exists in style.
                content = StringUtils.replace(content, "{0}", user.getUsername());
                content = StringUtils.replace(content, "{1}", "");
                content = StringUtils.replace(content, "{2}", pwd);

                mailService.sendSimpleMail(user.getEmail(), "OCTOPUS", content);
            } catch (Exception e) {
                log.error("Failed to send email~", e);
                throw new BizException("Failed to send email:" + user.getEmail());
            }
        }
        return status;
    }

    /**
     * Gets user resource action permissions.
     *
     * @param roleIds List
     * @return Map
     */
    @Override
    public Map<String, List<Integer>> getResOptPermission(List<Long> roleIds) {
        List<ResourceDTO> list = resourceService.findByRoleIds(roleIds);

        Map<String, List<Integer>> ret = new HashMap<>();

        list.forEach(resource -> {
            String key = resource.getPath();
            if (StringUtils.isNotBlank(key)) {
                Set<Integer> maskList = new HashSet<>();
                maskList.addAll(MaskUtils.getList(resource.getMask()));
                if (ret.containsKey(key)) {
                    maskList.addAll(ret.get(key));
                }
                ret.put(key, new ArrayList<>(maskList));
            }
        });

        return ret;
    }

    /**
     * Create a new User.
     *
     * @param user UserDO
     * @return int
     */
    @Override
    public int addUser(UserDO user) {
        int status = save(user);
        if (status < OctopusConstant.SUCCESS) {
            return OctopusConstant.FAIL;
        }

        try {
            String template_name = "templates/email/sys-reg-success.html";
            String content = getFileContent(template_name);

            // The reason for not using {@link MessageFormat.format} here is that the string {display:none} exists in style.
            content = StringUtils.replace(content, "{0}", user.getNickname());
            content = StringUtils.replace(content, "{1}", "");
            content = StringUtils.replace(content, "{2}", user.getUsername());
            content = StringUtils.replace(content, "{3}", user.getPassword());

            mailService.sendSimpleMail(user.getEmail(), "OCTOPUS", content);
        } catch (Exception e) {
            log.error("Failed to send email~", e);
            throw new BizException("Failed to send email:" + user.getEmail());
        }
        return status;
    }

    private String getFileContent(String template_name) throws Exception {
        InputStream is = ClassLoader.getSystemResourceAsStream(template_name);
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = fileReader.readLine()) != null) {
                sb.append(line);
            }
        } finally {
            is.close();
            fileReader.close();
        }

        return sb.toString();
    }

    /**
     * Create session Token
     *
     * @param userId Long
     * @return String
     */
    private synchronized String createUserSessionToken(Long userId) {
        String randomStr = UUID.randomUUID().toString().replaceAll("-", "");

        String rawStr = randomStr + TOKEN_INFO_SPLIT + userId;
        String token;
        try {
            token = MD5Utils.getMD5(rawStr);
        } catch (Exception e) {
            log.error("Create session Token fail.", e);
            return null;
        }

        UserSessionDO us = new UserSessionDO(userId, token);
        us.setLoginTime(System.currentTimeMillis());
        // It expires in two hours
        us.setTimeout(TOKEN_TIMEOUT * 2);
        int status = userSessionMapper.insert(us);
        if (status > 0) {
            return token;
        }
        return null;
    }

    /**
     * Get user list.
     *
     * @param user UserDO
     * @return List
     */
    @Override
    public List<UserDO> find(UserDO user) {
        if (user == null) {
            user = new UserDO();
        }
        return userMapper.list(user);
    }

    /**
     * Delete user list.
     *
     * @param userIds List
     * @return int
     */
    @Override
    public int removeUsers(List<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return 1;
        }
        Long curUserId = SessionThreadLocal.getUserId();
        if (curUserId == null) {
            throw new BizException("The operation without permission.");
        }

        if (userIds.contains(curUserId)) {
            throw new BizException("You cannot delete yourself~");
        }
        return userMapper.delByIds(userIds, curUserId);
    }

    /**
     * Change the user status
     *
     * @param userId Long userId
     * @param status Integer
     * @return 1 - success; 0 - fail.
     */
    @Override
    public int changeUserStatus(Long userId, Integer status) {
        Preconditions.checkNotNull(userMapper.findById(userId), "The user is not exist.");

        UserDO userDO = new UserDO();
        userDO.setStatus(status);
        userDO.setId(userId);

        return userMapper.update(userDO);
    }
}
