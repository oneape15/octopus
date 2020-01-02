package com.oneape.octopus.controller.system.form;

import com.oneape.octopus.model.VO.UserVO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class UserForm implements Serializable {
    private Long userId;
    @NotBlank(message = "用户名不能为空", groups = {LoginCheck.class, RegCheck.class})
    private String username;
    @NotBlank(message = "密码不能为空", groups = {LoginCheck.class, RegCheck.class})
    private String password;

    public interface LoginCheck {
    }

    public interface RegCheck {
    }

    public UserVO toVO() {
        UserVO vo = new UserVO();
        vo.setUserId(userId);
        vo.setUsername(username);
        vo.setPassword(password);

        return vo;
    }
}
