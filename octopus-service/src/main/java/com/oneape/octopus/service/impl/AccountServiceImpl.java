package com.oneape.octopus.service.impl;

import com.oneape.octopus.common.BizException;
import com.oneape.octopus.common.SessionThreadLocal;
import com.oneape.octopus.common.StateCode;
import com.oneape.octopus.commons.value.CodeBuilderUtils;
import com.oneape.octopus.commons.value.MD5Utils;
import com.oneape.octopus.mapper.UserMapper;
import com.oneape.octopus.mapper.UserSessionMapper;
import com.oneape.octopus.model.DO.UserDO;
import com.oneape.octopus.model.DO.UserSessionDO;
import com.oneape.octopus.model.VO.UserVO;
import com.oneape.octopus.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserSessionMapper userSessionMapper;

    /**
     * 根据token获取用户信息
     *
     * @param token String
     * @return UserVO
     */
    @Override
    public UserVO getUserInfoByToken(String token) {
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
        return SessionThreadLocal.getSession();
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
            throw new BizException(StateCode.OK.getCode(), "用户名或密码错误~");
        }

        // 生成登录成功token
        String token = createUserSessionToken(uvo.getUserId());
        if (StringUtils.isNotBlank(token)) {
            uvo.setToken(token);
            return uvo;
        }

        throw new BizException(StateCode.BadGateway.getCode(), "用户名或密码错误~");
    }

    /**
     * 创建用户
     *
     * @param user UserVO
     * @return int
     */
    @Override
    public int addUser(UserVO user) {
        Assert.isTrue(StringUtils.isNotBlank(user.getUsername()), "用户名为空");
        UserVO sameNameUser = getByUsername(user.getUsername());
        if (sameNameUser != null) {
            throw new BizException("用户名已存在");
        }

        String rawPwd = user.getPassword();
        if (StringUtils.isBlank(rawPwd)) {
            rawPwd = CodeBuilderUtils.RadmonStr(6);
        }
        String pwdMd5 = MD5Utils.saltUserPassword(user.getUsername(), rawPwd, null);
        user.setPassword(pwdMd5);
        int status = userMapper.insert(user.toDO());
        if (status > 0) {
            try {
                String template_name = "templates/email/sys-reg-success.html";
                String content = getFileContent(template_name);

                // 这里不使用MessageFormat.format的原因,在于 style中会存在{dispaly:none}这种字符串存在
                content = StringUtils.replace(content, "{0}", user.getNickname());
                content = StringUtils.replace(content, "{1}", "");
                content = StringUtils.replace(content, "{2}", user.getUsername());
                content = StringUtils.replace(content, "{3}", rawPwd);

//                iMailService.sendSimpleMail(user.getEmail(), "拉冬-数据平台密码重置", content);
            } catch (Exception e) {
                log.error("发送邮件失败~", e);
                throw new BizException("发送邮件失败, 邮箱地址:" + user.getEmail());
            }
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
        int status =  userSessionMapper.insert(us);
        if (status > 0) {
            return token;
        }
        return null;
    }
}
