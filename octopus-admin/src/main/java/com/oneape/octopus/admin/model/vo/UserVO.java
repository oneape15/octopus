package com.oneape.octopus.admin.model.vo;

import com.oneape.octopus.domain.system.UserDO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class UserVO implements Serializable {
    // 用户Id
    private Long id;
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
    // 角色Id列表
    private List<Long> roleIds;
    // 角色名称列表
    private List<String> roleNames = new ArrayList<>();
    // 资源操作列表
    private Map<String, List<Integer>> optPermission;

    public static UserVO ofDO(UserDO udo) {
        if (udo == null) {
            return null;
        }
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(udo, vo);
        return vo;
    }

    public UserDO toDO() {
        UserDO udo = new UserDO();
        BeanUtils.copyProperties(this, udo);
        return udo;
    }
}
