package com.oneape.octopus.controller.system.form;

import com.oneape.octopus.controller.BaseForm;
import com.oneape.octopus.model.DO.system.UserDO;
import com.oneape.octopus.model.VO.UserVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserForm extends BaseForm implements Serializable {
    @NotNull(message = "用户Id不能为空", groups = {KeyCheck.class, UpdateCheck.class})
    private Long   id;
    @NotBlank(message = "用户名不能为空", groups = {LoginCheck.class, RegCheck.class, AddCheck.class, UpdateCheck.class})
    private String username;
    @NotBlank(message = "密码不能为空", groups = {LoginCheck.class, RegCheck.class})
    private String password;
    @NotBlank(message = "用户邮箱不能为空", groups = {AddCheck.class, UpdateCheck.class})
    private String email;

    private String phone; // 手机号
    private String nickname; // 昵称
    private String avatar;   // 头像
    private String signature; // 个性签名
    private String address; // 联系地址
    private Integer status; // 账号状态
    private Integer gender; // 性别 0 - 女; 1 - 男

    @NotNull(message = "用户id列表不能为空", groups = {DelCheck.class})
    private List<Long> userIds;

    public interface AddCheck {
    }

    public interface UpdateCheck {
    }

    public interface DelCheck {
    }

    public interface LoginCheck {
    }

    public interface RegCheck {
    }

    public interface KeyCheck {
    }

    public UserVO toVO() {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(this, vo);

        return vo;
    }

    public UserDO toDO() {
        UserDO udo = new UserDO();
        BeanUtils.copyProperties(this, udo);

        return udo;
    }
}
