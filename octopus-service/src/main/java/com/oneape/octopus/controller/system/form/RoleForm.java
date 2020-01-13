package com.oneape.octopus.controller.system.form;

import com.oneape.octopus.controller.BaseForm;
import com.oneape.octopus.model.DO.system.RoleDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class RoleForm extends BaseForm implements Serializable {
    @NotNull(message = "主键不能为空", groups = {EditCheck.class, KeyCheck.class})
    private Long roleId;
    // 角色名称
    @NotBlank(message = "角色名称不能为空", groups = {AddCheck.class, EditCheck.class})
    private String name;
    // 角色编码
    @NotBlank(message = "角色编码不能为空", groups = {AddCheck.class, EditCheck.class})
    private String code;
    // 类型, 角色类型: 0 - 普通; 1 - 默认角色
    @NotNull(message = "角色类型不能为空", groups = {AddCheck.class, EditCheck.class})
    private Integer type;
    // 部门
    private String department;
    // 描述
    private String comment;

    public interface AddCheck {
    }

    public interface EditCheck {
    }

    public interface KeyCheck {
    }

    public RoleDO toDO() {
        RoleDO rdo = new RoleDO();
        BeanUtils.copyProperties(this, rdo);
        rdo.setId(roleId);
        return rdo;
    }
}
