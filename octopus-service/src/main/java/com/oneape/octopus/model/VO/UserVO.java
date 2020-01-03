package com.oneape.octopus.model.VO;

import com.oneape.octopus.model.DO.UserDO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
public class UserVO implements Serializable {
    // 用户Id
    private Long userId;
    // 登录名（唯一）
    private String username;
    // 昵称
    private String nickname;
    // 密码
    private transient String password;
    // 头像
    private String avatar;
    // 个性签名
    private String signature;
    // 手机号
    private String phone;
    // 邮箱地址
    private String email;
    // 联系地址
    private String address;
    // 账号状态
    private Integer status;

    // 会话Token
    private String token;


    public static UserVO ofDO(UserDO udo) {
        if (udo == null) {
            return null;
        }
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(udo, vo);
        vo.setUserId(udo.getId());
        return vo;
    }

    public UserDO toDO() {
        UserDO udo = new UserDO();
        BeanUtils.copyProperties(this, udo);
        udo.setId(this.userId);
        return udo;
    }
}
