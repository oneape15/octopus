package com.oneape.octopus.service.system.impl;

import com.google.common.base.Preconditions;
import com.oneape.octopus.common.*;
import com.oneape.octopus.commons.security.MD5Utils;
import com.oneape.octopus.commons.value.CodeBuilderUtils;
import com.oneape.octopus.mapper.system.ResourceMapper;
import com.oneape.octopus.mapper.system.UserMapper;
import com.oneape.octopus.mapper.system.UserRlRoleMapper;
import com.oneape.octopus.mapper.system.UserSessionMapper;
import com.oneape.octopus.model.DO.system.ResourceDO;
import com.oneape.octopus.model.DO.system.UserDO;
import com.oneape.octopus.model.DO.system.UserRlRoleDO;
import com.oneape.octopus.model.DO.system.UserSessionDO;
import com.oneape.octopus.model.VO.MenuVO;
import com.oneape.octopus.model.VO.UserVO;
import com.oneape.octopus.service.system.AccountService;
import com.oneape.octopus.service.system.MailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private UserMapper        userMapper;
    @Resource
    private UserRlRoleMapper  userRlRoleMapper;
    @Resource
    private UserSessionMapper userSessionMapper;
    @Resource
    private ResourceMapper    resourceMapper;

    @Resource
    private MailService mailService;

    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.；
     */
    @Override
    public int insert(UserDO model) {
        Preconditions.checkArgument(StringUtils.isNotBlank(model.getUsername()), "用户名为空");
        List<UserDO> users = userMapper.list(new UserDO(model.getUsername()));
        if (CollectionUtils.isNotEmpty(users)) {
            throw new BizException("用户名已存在");
        }

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
     * 修改数据
     *
     * @param model T
     * @return int 1 - success; 0 - fail.；
     */
    @Override
    public int edit(UserDO model) {
        Preconditions.checkNotNull(model.getId(), "用户ID为空");
        // 用户登录名不能修改
        model.setUsername(null);
        model.setPassword(null);

        return userMapper.update(model);
    }

    /**
     * 根据主键Id删除
     *
     * @param model T
     * @return int 1 - success; 0 - fail.；
     */
    @Override
    public int deleteById(UserDO model) {
        Preconditions.checkNotNull(model.getId(), "用户ID为空");

        int status = userMapper.delete(new UserDO(model.getId()));
        if (status > 0) {
            // 删除角色关系
            userRlRoleMapper.delete(new UserRlRoleDO(model.getId(), null));
        }
        return status;
    }

    /**
     * 根据token获取用户信息
     *
     * @param token String
     * @return UserVO
     */
    @Override
    public UserVO getUserInfoByToken(String token) {
        Preconditions.checkArgument(StringUtils.isNotBlank(token), "会话Token为空");
        // 根据token获取用户信息
        UserSessionDO us = userSessionMapper.findByToken(token);

        // 验证token
        if (us == null) {
            throw new UnauthorizedException("无效的Token");
        }

        Long loginTime = us.getLoginTime();

        int timeout = us.getTimeout();
        if (timeout <= 0) {
            timeout = TOKEN_TIMEOUT; // 默认60分钟失效
        }
        if (loginTime == null || loginTime + timeout * ONE_MINUTE < System.currentTimeMillis()) {
            throw new UnauthorizedException("登录令牌已失效, 请重新登录");
        }

        UserDO udo = userMapper.findById(us.getUserId());
        return UserVO.ofDO(udo);
    }

    /**
     * 获取当前用户
     *
     * @return UserVO
     */
    @Override
    public UserVO getCurrentUser() {
        UserVO uvo = SessionThreadLocal.getSession();
        if (uvo != null) {
            // 获取资源操作权限
            Map<String, List<Integer>> optPermission = getResOptPermission(uvo.getId());
            uvo.setOptPermission(optPermission);
        }
        return uvo;
    }

    /**
     * 获取当前用户Id
     *
     * @return Long
     */
    @Override
    public Long getCurrentUserId() {
        UserVO user = SessionThreadLocal.getSession();
        if (user == null) {
            return GlobalConstant.SYS_USER;
        }
        return user.getId();
    }

    /**
     * 获取当前用户的前端菜单列表
     *
     * @return List
     */
    @Override
    public List<MenuVO> getCurrentMenus() {
        ResourceDO qrd = new ResourceDO();
        qrd.setType(0); // 只查询类型为菜单的资料

        // 设置排序方式
        List<String> orders = new ArrayList<>();
        orders.add("level");
        orders.add("sort_id DESC");
        List<ResourceDO> resources = resourceMapper.listWithOrder(qrd, orders);

        Map<Integer, List<ResourceDO>> levelMap = new HashMap<>();
        for (ResourceDO r : resources) {
            if (!levelMap.containsKey(r.getLevel())) {
                levelMap.put(r.getLevel(), new ArrayList<>());
            }
            levelMap.get(r.getLevel()).add(r);
        }

        List<Integer> levels = levelMap.keySet()
                .stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        // 从下往上遍历
        Map<Long, List<MenuVO>> preLevelMap = new LinkedHashMap<>();
        for (Integer level : levels) {
            Map<Long, List<MenuVO>> curLevelMap = new LinkedHashMap<>();
            for (ResourceDO r : levelMap.get(level)) {
                Long id = r.getId();
                Long pId = r.getParentId();
                MenuVO menu = new MenuVO(r.getName(), r.getPath(), r.getIcon());
                if (preLevelMap.containsKey(id)) {
                    menu.setChildren(preLevelMap.get(id));
                }
                if (!curLevelMap.containsKey(pId)) {
                    curLevelMap.put(pId, new ArrayList<>());
                }
                curLevelMap.get(pId).add(menu);
            }
            preLevelMap = curLevelMap;
        }

        List<MenuVO> menus = new ArrayList<>();
        preLevelMap.values().forEach(menus::addAll);

        return menus;
    }

    /**
     * 根据用户名查询用户信息
     *
     * @param username String
     * @return UserDO
     */
    @Override
    public UserVO getByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            return null;
        }

        UserDO udo = userMapper.getByUsername(username);
        return UserVO.ofDO(udo);
    }

    /**
     * 用户登录操作
     *
     * @param username String
     * @param password String
     * @return UserVO
     */
    @Override
    public UserVO login(String username, String password) {
        UserVO uvo = getByUsername(username);
        if (uvo == null || !StringUtils.equals(password, uvo.getPassword())) {
            throw new BizException("用户名或密码错误~");
        }

        // 生成登录成功token
        String token = createUserSessionToken(uvo.getId());
        if (StringUtils.isBlank(token)) {
            throw new BizException("登录令牌生成失败，请重试~");
        }
        uvo.setToken(token);

        // 获取资源操作权限
        Map<String, List<Integer>> optPermission = getResOptPermission(uvo.getId());
        uvo.setOptPermission(optPermission);
        return uvo;
    }

    /**
     * 重置用户密码
     *
     * @param userId Long
     * @return int
     */
    @Override
    public int resetPwd(Long userId) {
        Preconditions.checkNotNull(userId, "用户Id为空");
        UserDO user = Preconditions.checkNotNull(userMapper.findById(userId), "用户信息不存在");
        // 随机生成一个密码
        String pwd = CodeBuilderUtils.RandmonStr(6);
        String pwdMd5 = MD5Utils.saltUserPassword(user.getUsername(), pwd, null);
        user.setPassword(pwdMd5);
        int status = userMapper.update(user);
        if (status > 0) {
            // 密码被修改了,必须发送一封邮件给相应的用户

            try {
                String template_name = "templates/email/user-reset-pwd.html";
                String content = getFileContent(template_name);

                // 这里不使用MessageFormat.format的原因,在于 style中会存在{dispaly:none}这种字符串存在
                content = StringUtils.replace(content, "{0}", user.getUsername());
                content = StringUtils.replace(content, "{1}", "");
                content = StringUtils.replace(content, "{2}", pwd);

                mailService.sendSimpleMail(user.getEmail(), "OCTOPUS-数据平台密码重置", content);
            } catch (Exception e) {
                log.error("发送邮件失败~", e);
                throw new BizException("发送邮件失败, 邮箱地址:" + user.getEmail());
            }
        }
        return status;
    }

    /**
     * 获取用户资源操作权限
     *
     * @param userId Long
     * @return Map
     */
    @Override
    public Map<String, List<Integer>> getResOptPermission(Long userId) {
        List<ResourceDO> list = resourceMapper.list(new ResourceDO());

        Map<String, List<Integer>> ret = new HashMap<>();

        List<Integer> masks = MaskUtils.getAllList();
        list.forEach(resource -> {
            if (StringUtils.isNotBlank(resource.getPath())) {
                ret.put(resource.getPath(), masks);
            }
        });

        return ret;
    }

    /**
     * 创建用户
     *
     * @param user UserVO
     * @return int
     */
    @Override
    public int addUser(UserVO user) {
        UserDO tmp = user.toDO();
        int status = insert(tmp);
        if (status < GlobalConstant.SUCCESS) {
            return GlobalConstant.FAIL;
        }

        // 保存用户与角色信息
        if (CollectionUtils.isNotEmpty(user.getRoleIds())) {
            user.getRoleIds().forEach(rId -> userRlRoleMapper.insert(new UserRlRoleDO(user.getId(), rId)));
        }

        try {
            String template_name = "templates/email/sys-reg-success.html";
            String content = getFileContent(template_name);

            // 这里不使用MessageFormat.format的原因,在于 style中会存在{dispaly:none}这种字符串存在
            content = StringUtils.replace(content, "{0}", user.getNickname());
            content = StringUtils.replace(content, "{1}", "");
            content = StringUtils.replace(content, "{2}", user.getUsername());
            content = StringUtils.replace(content, "{3}", tmp.getPassword());

            mailService.sendSimpleMail(user.getEmail(), "OCTOPUS-数据平台密码重置", content);
        } catch (Exception e) {
            log.error("发送邮件失败~", e);
            throw new BizException("发送邮件失败, 邮箱地址:" + user.getEmail());
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
     * 创建会话token
     *
     * @param userId Long
     * @return String
     */
    private synchronized String createUserSessionToken(Long userId) {
        String randomStr = UUID.randomUUID().toString().replaceAll("-", "");

        String rawStr = randomStr + TOKEN_INFO_SPLIT + userId;
        String token = "";
        try {
            token = MD5Utils.getMD5(rawStr);
        } catch (Exception e) {
            //
            log.error("生成token失败： {}", e.getMessage());
        }

        UserSessionDO us = new UserSessionDO(userId, token);
        us.setLoginTime(System.currentTimeMillis());
        us.setTimeout(TOKEN_TIMEOUT * 2); // 二小时失效
        int status = userSessionMapper.insert(us);
        if (status > 0) {
            return token;
        }
        return null;
    }

    /**
     * 获取用户列表
     *
     * @param user UserDO
     * @return List
     */
    @Override
    public List<UserVO> find(UserDO user) {
        List<UserDO> users = userMapper.list(user);
        List<UserVO> vos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(users)) {
            users.forEach(u -> vos.add(UserVO.ofDO(u)));
        }
        return vos;
    }

    /**
     * 删除用户列表
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
            throw new BizException("无权限的操作");
        }

        if (userIds.contains(curUserId)) {
            throw new BizException("不能将自己删除~");
        }
        return userMapper.delByIds(userIds, curUserId);
    }
}
