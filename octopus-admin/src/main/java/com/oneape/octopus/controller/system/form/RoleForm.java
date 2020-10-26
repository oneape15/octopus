package com.oneape.octopus.controller.system.form;

import com.oneape.octopus.controller.BaseForm;
import com.oneape.octopus.model.domain.system.RoleDO;
import com.oneape.octopus.model.domain.system.RoleRlSchemaDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class RoleForm extends BaseForm implements Serializable {
    @NotNull(message = "The primary key is null.", groups = {EditCheck.class, KeyCheck.class})
    private Long    id;
    @NotBlank(message = "The role name is empty.", groups = {AddCheck.class, EditCheck.class})
    private String  name;
    @NotBlank(message = "The role code is empty.", groups = {AddCheck.class, EditCheck.class})
    private String  code;
    // Role type: 0-normal; 1 - Default role; 3 - System role.
    @NotNull(message = "The role type is empty.", groups = {AddCheck.class, EditCheck.class})
    private Integer type;
    // The role description.
    private String  comment;

    private List<RoleRlSchemaDO> schemaDOList;

    public interface AddCheck {
    }

    public interface EditCheck {
    }

    public interface KeyCheck {
    }

    public RoleDO toDO() {
        RoleDO rdo = new RoleDO();
        BeanUtils.copyProperties(this, rdo);
        return rdo;
    }
}
