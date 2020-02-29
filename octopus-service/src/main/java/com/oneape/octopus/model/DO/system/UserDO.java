package com.oneape.octopus.model.DO.system;

import com.oneape.octopus.model.DO.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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

    /**
     * 性别 0 - 女; 1 - 男
     */
    private Integer gender;

    public UserDO(String username) {
        this.username = username;
    }

    public UserDO(Long id) {
        setId(id);
    }
}
