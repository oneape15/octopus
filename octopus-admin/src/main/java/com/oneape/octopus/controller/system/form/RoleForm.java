package com.oneape.octopus.controller.system.form;

import com.oneape.octopus.controller.BaseForm;
import com.oneape.octopus.domain.system.RoleDO;
import com.oneape.octopus.domain.system.RoleRlSchemaDO;
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
    @NotNull(message = "{RoleForm.NotNull.id}", groups = {KeyCheck.class})
    private Long    id;
    @NotBlank(message = "{RoleForm.NotBlank.name}", groups = {SaveCheck.class})
    private String  name;
    @NotBlank(message = "{RoleForm.NotBlank.code}", groups = {SaveCheck.class})
    private String  code;
    // Role type: 0-normal; 1 - Default role; 3 - System role.
    @NotNull(message = "{RoleForm.NotBlank.type}", groups = {SaveCheck.class})
    private Integer type;
    // The role description.
    private String  comment;

    private List<RoleRlSchemaDO> schemaDOList;

    public interface SaveCheck {
    }

    public interface KeyCheck {
    }

    public RoleDO toDO() {
        RoleDO rdo = new RoleDO();
        BeanUtils.copyProperties(this, rdo);
        return rdo;
    }
}
