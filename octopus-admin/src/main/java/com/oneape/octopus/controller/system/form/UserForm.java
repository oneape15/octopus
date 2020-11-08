package com.oneape.octopus.controller.system.form;

import com.oneape.octopus.controller.BaseForm;
import com.oneape.octopus.domain.system.UserDO;
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
    @NotNull(message = "The user id is empty.", groups = {KeyCheck.class, UpdateCheck.class})
    private Long   id;
    @NotBlank(message = "The username is empty.", groups = {LoginCheck.class, RegCheck.class, AddCheck.class, UpdateCheck.class})
    private String username;
    @NotBlank(message = "The password is empty.", groups = {LoginCheck.class, RegCheck.class})
    private String password;
    @NotBlank(message = "The user email address is empty.", groups = {AddCheck.class, UpdateCheck.class})
    private String email;

    private String  phone;
    private String  nickname;
    private String  avatar;
    private Integer status;
    private Integer gender;

    @NotNull(message = "The userId List is empty.", groups = {DelCheck.class})
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

    public UserDO toDO() {
        UserDO udo = new UserDO();
        BeanUtils.copyProperties(this, udo);

        return udo;
    }
}
