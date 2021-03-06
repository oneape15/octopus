package com.oneape.octopus.admin.controller.system.form;

import com.oneape.octopus.admin.controller.BaseForm;
import com.oneape.octopus.domain.system.ResourceDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class ResForm extends BaseForm implements Serializable {
    @NotNull(message = "{ResForm.NotNull.id}", groups = {EditCheck.class, KeyCheck.class})
    private Long    id;
    // The parent node id.
    @NotNull(message = "{ResForm.NotNull.parentId}", groups = {EditCheck.class, AddCheck.class})
    private Long    parentId;
    @NotBlank(message = "{ResForm.NotBlank.name}", groups = {EditCheck.class, AddCheck.class})
    private String  name;
    // The resource icon
    private String  icon;
    @NotNull(message = "{ResForm.NotNull.type}", groups = {EditCheck.class, AddCheck.class})
    private Integer type;
    @NotBlank(message = "{ResForm.NotBlank.path}", groups = {EditCheck.class, AddCheck.class})
    private String  path;
    private Long    sortId;
    private String  comment;


    public interface AddCheck {
    }

    public interface EditCheck {
    }

    public interface KeyCheck {
    }

    public ResourceDO toDO() {
        ResourceDO rdo = new ResourceDO();
        BeanUtils.copyProperties(this, rdo);
        return rdo;
    }
}
