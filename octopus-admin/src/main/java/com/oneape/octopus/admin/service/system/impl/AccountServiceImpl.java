package com.oneape.octopus.admin.service.system.impl;

import com.google.common.base.Preconditions;
import com.oneape.octopus.commons.cause.BizException;
import com.oneape.octopus.commons.cause.UnauthorizedException;
import com.oneape.octopus.commons.constant.OctopusConstant;
import com.oneape.octopus.commons.security.MD5Utils;
import com.oneape.octopus.commons.value.CodeBuilderUtils;
import com.oneape.octopus.commons.value.MaskUtils;
import com.oneape.octopus.admin.config.I18nMsgConfig;
import com.oneape.octopus.admin.config.SessionThreadLocal;
import com.oneape.octopus.domain.system.RoleDO;
import com.oneape.octopus.domain.system.UserDO;
import com.oneape.octopus.domain.system.UserRlRoleDO;
import com.oneape.octopus.domain.system.UserSessionDO;
import com.oneape.octopus.dto.system.AppType;
import com.oneape.octopus.dto.system.ResourceDTO;
import com.oneape.octopus.dto.system.UserDTO;
import com.oneape.octopus.dto.system.UserStatus;
import com.oneape.octopus.mapper.system.UserMapper;
import com.oneape.octopus.mapper.system.UserRlRoleMapper;
import com.oneape.octopus.mapper.system.UserSessionMapper;
import com.oneape.octopus.admin.service.system.AccountService;
import com.oneape.octopus.admin.service.system.MailService;
import com.oneape.octopus.admin.service.system.ResourceService;
import com.oneape.octopus.admin.service.system.RoleService;
import com.oneape.octopus.admin.service.uid.UIDGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private UserMapper          userMapper;
    @Resource
    private UserSessionMapper   userSessionMapper;
    @Resource
    private SqlSessionFactory   sqlSessionFactory;
    @Resource
    private UIDGeneratorService uidGeneratorService;
    @Resource
    private RoleService         roleService;
    @Resource
    private ResourceService     resourceService;
    @Resource
    private MailService         mailService;
    @Resource
    private RedissonClient      redissonClient;

    /**
     * save data to table.
     * <p>
     * If the Model property ID is not null, the update operation is performed, or the insert operation is performed。
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @Transactional
    @Override
    public int save(UserDO model) {
        Preconditions.checkNotNull(model, I18nMsgConfig.getMessage("account.user.null"));
        Preconditions.checkArgument(
                StringUtils.isNotBlank(model.getUsername()),
                I18nMsgConfig.getMessage("account.username.empty"));

        if (model.getId() != null) {
            // the username and password can't edit.
            model.setUsername(null);
            model.setPassword(null);

            return userMapper.update(model);
        }

        Preconditions.checkArgument(
                userMapper.sameNameCheck(model.getUsername(), null) == 0,
                I18nMsgConfig.getMessage("account.username.exist"));
        String rawPwd = model.getPassword();
        if (StringUtils.isBlank(rawPwd)) {
            rawPwd = CodeBuilderUtils.randomStr(6);
        }
        String pwdMd5 = MD5Utils.saltUserPassword(model.getUsername(), rawPwd, null);
        model.setPassword(pwdMd5);
        int status = userMapper.insert(model);
        if (status > 0 && StringUtils.isNotBlank(model.getEmail())) {
            model.setPassword(rawPwd);

            // send the success email.
            try {
                String template_name = "templates/email/sys-reg-success.html";
                String content = getFileContent(template_name);

                // The reason for not using {@link MessageFormat.format} here is that the string {display:none} exists in style.
                content = StringUtils.replace(content, "{0}", model.getNickname());
                content = StringUtils.replace(content, "{1}", "");
                content = StringUtils.replace(content, "{2}", model.getUsername());
                content = StringUtils.replace(content, "{3}", model.getPassword());

                mailService.sendSimpleMail(model.getEmail(), "OCTOPUS", content);
            } catch (Exception e) {
                log.error("Failed to send email~", e);
                throw new BizException(I18nMsgConfig.getMessage("global.send.email.fail", model.getEmail()));
            }
        }

        return status;
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
        Preconditions.checkNotNull(id, I18nMsgConfig.getMessage("account.userId.null"));

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
        Preconditions.checkArgument(StringUtils.isNotBlank(token), I18nMsgConfig.getMessage("account.token.invalid"));
        // Get user information according to token
        UserSessionDO us = userSessionMapper.findByToken(token);

        // Authentication token
        if (us == null) {
            throw new UnauthorizedException(I18nMsgConfig.getMessage("account.token.invalid"));
        }

        Long expireAt = us.getExpireAt();

        if (expireAt == null || expireAt <= System.currentTimeMillis()) {
            throw new UnauthorizedException(I18nMsgConfig.getMessage("account.login.timeout"));
        }

        UserDO udo = Preconditions.checkNotNull(
                findById(us.getUserId()),
                I18nMsgConfig.getMessage("account.user.nullOrLock"));

        // get user full information.
        return getFullInformationById(udo.getId());
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
        UserDO udo = Preconditions.checkNotNull(
                findById(userId),
                I18nMsgConfig.getMessage("account.user.nullOrLock"));

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
     * @param appType  AppType
     * @return String login token value.
     */
    @Transactional
    @Override
    public String login(String username, String password, AppType appType) {
        UserDO udo = getByUsername(username);
        if (udo == null) {
            throw new BizException(I18nMsgConfig.getMessage("account.login.info.error"));
        }
        if (udo.getStatus() != UserStatus.NORMAL) {
            throw new BizException(I18nMsgConfig.getMessage("account.login.inactive"));
        }
        if (!StringUtils.equalsIgnoreCase(password, udo.getPassword())) {
            throw new BizException(I18nMsgConfig.getMessage("account.login.info.error"));
        }

        // crate a new token.
        String randomStr = UUID.randomUUID().toString().replaceAll("-", "");
        String rawStr = randomStr + TOKEN_INFO_SPLIT + udo.getId();
        String token;
        try {
            token = MD5Utils.getMD5(rawStr);
        } catch (Exception e) {
            log.error(I18nMsgConfig.getMessage("account.login.token.fail"), e);
            return null;
        }

        RLock lock = redissonClient.getLock("USER_LOGIN_" + udo.getId() + "_" + appType);
        if (lock.isLocked()) {
            throw new BizException(I18nMsgConfig.getMessage("global.get.lock.fail"));
        }

        int status;
        try {
            lock.lock(1, TimeUnit.MINUTES);
            UserSessionDO oldDo = userSessionMapper.findByUserId(udo.getId(), appType.getCode());
            Long expireAt = System.currentTimeMillis() + TOKEN_TIMEOUT * ONE_MINUTE * 2;
            if (oldDo != null) {
                oldDo.setLoginTime(System.currentTimeMillis());
                // It expires in two hours
                oldDo.setExpireAt(expireAt);
                oldDo.setToken(token);

                status = userSessionMapper.update(oldDo);
            } else {
                UserSessionDO us = new UserSessionDO(udo.getId(), token);
                us.setAppType(appType.getCode());
                us.setLoginTime(System.currentTimeMillis());
                // It expires in two hours
                us.setExpireAt(expireAt);

                status = userSessionMapper.insert(us);
            }
        } finally {
            lock.unlock();
        }

        return status > 0 ? token : null;
    }

    /**
     * the user login out option.
     *
     * @param userId  Long
     * @param appType AppType
     * @return int 0 - fail; 1 - success;
     */
    @Override
    public int logout(Long userId, AppType appType) {
        RLock lock = redissonClient.getLock("USER_LOGOUT_" + userId + "_" + appType);
        if (lock.isLocked()) {
            throw new BizException(I18nMsgConfig.getMessage("global.get.lock.fail"));
        }

        int status;
        try {
            lock.lock(1, TimeUnit.MINUTES);
            UserSessionDO usDo = Preconditions.checkNotNull(userSessionMapper.findByUserId(userId, appType.getCode()), "");
            usDo.setLogoutTime(System.currentTimeMillis());
            usDo.setExpireAt(System.currentTimeMillis());
            status = userSessionMapper.update(usDo);
        } finally {
            lock.unlock();
        }

        return status;
    }

    /**
     * Reset user password.
     *
     * @param userId Long
     * @return int
     */
    @Override
    public int resetPwd(Long userId) {
        Preconditions.checkNotNull(userId, I18nMsgConfig.getMessage("account.userId.null"));
        UserDO user = Preconditions.checkNotNull(userMapper.findById(userId), I18nMsgConfig.getMessage("account.user.null"));

        // Randomly generate a password.
        String pwd = CodeBuilderUtils.randomStr(6);
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
                String msg = I18nMsgConfig.getMessage("global.send.email.fail", user.getEmail());
                log.error(msg, e);
                throw new BizException(msg);
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
                Set<Integer> maskList = new HashSet<>(MaskUtils.getList(resource.getMask()));
                if (ret.containsKey(key)) {
                    maskList.addAll(ret.get(key));
                }
                ret.put(key, new ArrayList<>(maskList));
            }
        });

        return ret;
    }

    private String getFileContent(String template_name) throws Exception {
        InputStream is = ClassLoader.getSystemResourceAsStream(template_name);
        assert is != null;
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
     * Change the user status
     *
     * @param userId Long userId
     * @param status UserStatus
     * @return 1 - success; 0 - fail.
     */
    @Transactional
    @Override
    public int changeUserStatus(Long userId, UserStatus status) {
        Preconditions.checkNotNull(userMapper.findById(userId), I18nMsgConfig.getMessage("account.user.null"));
        if (userId.equals(SessionThreadLocal.getUserId())) {
            throw new BizException(I18nMsgConfig.getMessage("account.user.option.self"));
        }
        UserDO userDO = new UserDO();
        userDO.setStatus(status);
        userDO.setId(userId);

        int ret = userMapper.update(userDO);

        // Check if the user is logged in and, if so, set the token to expire.
        if (ret > 0) {
            userSessionMapper.setToken2expire(userId);
        }
        return ret;
    }

    /**
     * save user role.
     *
     * @param userId  Long
     * @param roleIds List
     * @return 1 - success; 0 - fail.
     */
    @Override
    public int saveUserRole(Long userId, List<Long> roleIds) {
        Preconditions.checkNotNull(userMapper.findById(userId), I18nMsgConfig.getMessage("account.user.null"));
        List<Long> needInsertRoleIds;
        List<Long> needDeleteRoleIds = new ArrayList<>();

        List<RoleDO> roleDOs = roleService.findRoleByUserId(userId);
        if (CollectionUtils.isNotEmpty(roleDOs)) {
            // Gets the existing role ID
            List<Long> existRoleIds = roleDOs.stream().map(RoleDO::getId).collect(Collectors.toList());

            needDeleteRoleIds = existRoleIds.stream().filter(rId -> !roleIds.contains(rId)).collect(Collectors.toList());
            needInsertRoleIds = roleIds.stream().filter(rId -> !existRoleIds.contains(rId)).collect(Collectors.toList());
        } else {
            needInsertRoleIds = new ArrayList<>(roleIds);
        }

        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
        int count = 0;
        try {
            UserRlRoleMapper mapper = session.getMapper(UserRlRoleMapper.class);
            // add option
            for (Long roleId : needInsertRoleIds) {
                UserRlRoleDO u2rDo = new UserRlRoleDO(userId, roleId);
                u2rDo.setId(uidGeneratorService.getUid());
                mapper.insert(u2rDo);
                count++;
            }

            // delete option
            for (Long roleId : needDeleteRoleIds) {
                UserRlRoleDO u2rDo = new UserRlRoleDO(userId, roleId);
                mapper.delete(u2rDo);
            }

            session.commit();
        } catch (Exception e) {
            log.error("Batch insert data table information exception", e);
            session.rollback();
            throw new BizException(I18nMsgConfig.getMessage("global.batch.insert.error"));
        } finally {
            log.debug("Batch insert data table information: {} rows.", count);
            session.close();
        }

        return 1;
    }
}
