package com.oneape.octopus.controller.system.form;

import com.oneape.octopus.controller.BaseForm;
import com.oneape.octopus.model.DO.system.CommonInfoDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class CommonInfoForm extends BaseForm implements Serializable {
    @NotNull(message = "The primary key is null.", groups = {EditCheck.class, KeyCheck.class})
    private Long   id;
    private Long   parentId;
    // Basic information classification
    @NotBlank(message = "The common information classify is empty.", groups = {AddCheck.class, EditCheck.class})
    private String classify;
    // The common information key.
    @NotBlank(message = "The common information key is empty.", groups = {AddCheck.class, EditCheck.class})
    private String key;
    // The common information value.
    @NotBlank(message = "The common information value is empty.", groups = {AddCheck.class, EditCheck.class})
    private String value;

    public interface AddCheck {
    }

    public interface EditCheck {
    }

    public interface KeyCheck {
    }

    public CommonInfoDO toDO() {
        CommonInfoDO cdo = new CommonInfoDO();
        BeanUtils.copyProperties(this, cdo);
        return cdo;
    }
}
