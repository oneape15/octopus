package com.oneape.octopus.admin.controller.datawarehouse.form;

import com.oneape.octopus.admin.controller.BaseForm;
import com.oneape.octopus.commons.enums.DdlAuditType;
import com.oneape.octopus.domain.warehouse.DdlAuditInfoDO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2021-02-25 16:21.
 * Modify:
 */
@Data
public class DdlAuditInfoForm extends BaseForm implements Serializable {
    @NotNull(message = "{global.pKey.empty}", groups = {AuditCheck.class})
    private Long         id;
    @NotBlank(message = "{DdlAuditInfoForm.NotBlank.name}", groups = {SaveCheck.class})
    private String       name;
    @NotNull(message = "{DdlAuditInfoForm.NotNull.datasourceId}", groups = {SaveCheck.class})
    private Long         datasourceId;
    @NotBlank(message = "{DdlAuditInfoForm.NotBlank.ddlText}", groups = {SaveCheck.class})
    private String       ddlText;
    private DdlAuditType status;
    private Long         auditor;
    @NotBlank(message = "{{DdlAuditInfoForm.NotBlank.opinion}", groups = {AuditCheck.class})
    private String       opinion;
    private String       comment;

    public interface SaveCheck {
    }

    public interface AuditCheck {
    }

    public DdlAuditInfoDO toDO() {
        DdlAuditInfoDO ddo = new DdlAuditInfoDO();
        BeanUtils.copyProperties(this, ddo);
        return ddo;
    }
}
