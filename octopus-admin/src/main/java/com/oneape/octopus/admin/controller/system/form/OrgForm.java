package com.oneape.octopus.admin.controller.system.form;

import com.oneape.octopus.admin.controller.BaseForm;
import com.oneape.octopus.domain.system.OrganizationDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrgForm extends BaseForm implements Serializable {
    @NotNull(message = "The primary key is null.", groups = {KeyCheck.class})
    private Long   id;
    @NotNull(message = "The parent node is is empty.", groups = {SaveCheck.class})
    private Long   parentId;
    @NotBlank(message = "The org code is empty.", groups = {SaveCheck.class})
    private String code;
    @NotBlank(message = "The org name is empty.", groups = {SaveCheck.class})
    private String name;
    // The role description.
    private String comment;


    // add children count size.
    private boolean    addChildrenSize = false;
    // add root node.
    private boolean    addRootNode     = false;
    // disabled key list
    private List<Long> disabledKeys    = new ArrayList<>();

    public interface SaveCheck {
    }

    public interface KeyCheck {
    }

    public OrganizationDO toDO() {
        OrganizationDO odo = new OrganizationDO();
        BeanUtils.copyProperties(this, odo);
        return odo;
    }
}
