package com.oneape.octopus.controller.peekdata.form;

import com.oneape.octopus.controller.BaseForm;
import com.oneape.octopus.model.DO.peekdata.ModelTagDO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class TagForm extends BaseForm implements Serializable {
    @NotNull(message = "主键为空", groups = {EditCheck.class, KeyCheck.class})
    private Long tagId;
    // 标签名
    @NotBlank(message = "标签名为空", groups = {AddCheck.class, EditCheck.class})
    private String name;
    // 匹配规则
    @NotBlank(message = "标签规则为空", groups = {AddCheck.class, EditCheck.class})
    private String rule;
    // 是否默认标签,0为非默认,1为默认
    private Integer defaulted;

    public interface AddCheck {
    }

    public interface EditCheck {
    }

    public interface KeyCheck {
    }

    public ModelTagDO toDO() {
        ModelTagDO mdo = new ModelTagDO();
        BeanUtils.copyProperties(this, mdo);
        mdo.setId(tagId);
        return mdo;
    }
}
