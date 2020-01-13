package com.oneape.octopus.controller.system.form;

import com.oneape.octopus.controller.BaseForm;
import com.oneape.octopus.model.DO.system.UserDO;
import com.oneape.octopus.model.VO.UserVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserForm extends BaseForm implements Serializable {
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
        BeanUtils.copyProperties(this, vo);
        vo.setId(userId);

        return vo;
    }

    public UserDO toDO() {
        UserDO udo = new UserDO();
        BeanUtils.copyProperties(this, udo);
        udo.setId(userId);

        return udo;
    }
}
