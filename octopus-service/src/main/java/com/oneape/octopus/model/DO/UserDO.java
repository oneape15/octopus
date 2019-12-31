package com.oneape.octopus.model.DO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserDO extends BaseDO {

    /**
     * 登录名（唯一）
     */
    private String username;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 密码
     */
    private transient String password;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 个性签名
     */
    private String signature;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱地址
     */
    private String email;
    /**
     * 联系地址
     */
    private String address;
    /**
     * 账号状态
     */
    private Integer status;
}
