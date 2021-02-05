package com.oneape.octopus.admin.controller.system.form;

import com.oneape.octopus.admin.controller.BaseForm;
import com.oneape.octopus.domain.system.UserDO;
import com.oneape.octopus.dto.system.UserStatus;
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
    @NotNull(message = "{UserForm.NotNull.id}", groups = {KeyCheck.class})
    private Long   id;
    @NotBlank(message = "{UserForm.NotBlank.username}", groups = {LoginCheck.class, RegCheck.class, SaveCheck.class})
    private String username;
    @NotBlank(message = " {UserForm.NotBlank.password}", groups = {LoginCheck.class, RegCheck.class})
    private String password;
    @NotBlank(message = "{UserForm.NotBlank.email}", groups = {SaveCheck.class})
    private String email;

    private String phone;
    private String nickname;
    private String avatar;

    private UserStatus status;
    private Integer    gender;
    private String     type;

    @NotNull(message = "{UserForm.NotNull.userIds}", groups = {DelCheck.class})
    private List<Long> userIds;

    private List<Long> roleIds;

    public interface SaveCheck {
    }

    public interface DelCheck {
    }

    public interface LoginCheck {
    }

    public interface RegCheck {
    }

    public interface KeyCheck {
    }

    public UserDO toDO() {
        UserDO udo = new UserDO();
        BeanUtils.copyProperties(this, udo);

        return udo;
    }
}
