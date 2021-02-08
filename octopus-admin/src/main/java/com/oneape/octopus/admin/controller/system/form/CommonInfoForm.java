package com.oneape.octopus.admin.controller.system.form;

import com.oneape.octopus.admin.controller.BaseForm;
import com.oneape.octopus.domain.system.CommonInfoDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class CommonInfoForm extends BaseForm implements Serializable {
    private Long   id;
    private Long   parentId;
    // Basic information classification
    @NotBlank(message = "{CommonInfoForm.NotBlank.classify}", groups = {InfoCheck.class})
    private String classify;
    // The common information key.
    @NotBlank(message = "{CommonInfoForm.NotBlank.key}", groups = {InfoCheck.class})
    private String key;
    // The common information value.
    @NotBlank(message = "{CommonInfoForm.NotBlank.value}", groups = {InfoCheck.class})
    private String value;

    public interface InfoCheck {
    }

    public CommonInfoDO toDO() {
        CommonInfoDO cdo = new CommonInfoDO();
        BeanUtils.copyProperties(this, cdo);
        return cdo;
    }
}
