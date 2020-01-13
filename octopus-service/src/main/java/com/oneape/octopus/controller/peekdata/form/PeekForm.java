package com.oneape.octopus.controller.peekdata.form;

import com.oneape.octopus.controller.BaseForm;
import com.oneape.octopus.model.DO.peekdata.PeekDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class PeekForm extends BaseForm implements Serializable {
    // 取数Id
    @NotNull(message = "取数Id不能为空", groups = {EditCheck.class, KeyCheck.class})
    private Long peekId;
    // 模型Id
    @NotNull(message = "模型Id不能为空", groups = {AddCheck.class, EditCheck.class})
    private Long modelId;
    // 取数实例名称
    @NotBlank(message = "取数名称不能为空", groups = {AddCheck.class, EditCheck.class})
    private String name;
    // 返回的数据字段名列表, 多个以","隔开
    private String fieldList;

    public interface AddCheck {
    }

    public interface EditCheck {
    }

    public interface KeyCheck {
    }

    public PeekDO toDO() {
        PeekDO mdo = new PeekDO();
        BeanUtils.copyProperties(this, mdo);
        mdo.setId(peekId);
        return mdo;
    }
}
