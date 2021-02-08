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
    @NotNull(message = "{OrgForm.NotNull.id}", groups = {KeyCheck.class})
    private Long   id;
    @NotNull(message = "{OrgForm.NotNull.parentId}", groups = {SaveCheck.class})
    private Long   parentId;
    @NotBlank(message = "{OrgForm.NotBlank.code}", groups = {SaveCheck.class})
    private String code;
    @NotBlank(message = "{OrgForm.NotBlank.name}", groups = {SaveCheck.class})
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
