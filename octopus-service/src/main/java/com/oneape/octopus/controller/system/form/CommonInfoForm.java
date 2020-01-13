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
    // 主键
    @NotNull(message = "主键不能为空", groups = {EditCheck.class, KeyCheck.class})
    private Long commonInfoId;
    // 父分类
    private Long parentId;

    // 基础信息分类名称
    @NotBlank(message = "分类名称不能为空", groups = {AddCheck.class, EditCheck.class})
    private String classify;
    // 编码信息
    @NotBlank(message = "编码不能为空", groups = {AddCheck.class, EditCheck.class})
    private String code;
    // 名称
    @NotBlank(message = "名称不能为空", groups = {AddCheck.class, EditCheck.class})
    private String name;

    public interface AddCheck {
    }

    public interface EditCheck {
    }

    public interface KeyCheck {
    }

    public CommonInfoDO toDO() {
        CommonInfoDO cdo = new CommonInfoDO();
        BeanUtils.copyProperties(this, cdo);
        cdo.setId(commonInfoId);
        return cdo;
    }
}
