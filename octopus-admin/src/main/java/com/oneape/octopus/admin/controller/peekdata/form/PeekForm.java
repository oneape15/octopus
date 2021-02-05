package com.oneape.octopus.admin.controller.peekdata.form;

import com.oneape.octopus.admin.controller.BaseForm;
import com.oneape.octopus.domain.peekdata.PeekDO;
import com.oneape.octopus.admin.model.vo.PeekFieldVO;
import com.oneape.octopus.admin.model.vo.PeekRuleVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class PeekForm extends BaseForm implements Serializable {
    // 取数Id
    @NotNull(message = "取数Id不能为空", groups = {EditCheck.class, KeyCheck.class})
    private Long peekId;
    // 模型Id
    @NotNull(message = "模型Id不能为空", groups = {AddCheck.class, EditCheck.class, PreviewCheck.class})
    private Long modelId;
    // 取数实例名称
    @NotBlank(message = "取数名称不能为空", groups = {AddCheck.class, EditCheck.class})
    private String name;
    // 返回的数据字段名列表
    @NotEmpty(message = "返回字段不能为空", groups = {PreviewCheck.class})
    private List<PeekFieldVO> fields;
    // 取数规则列表
    private List<PeekRuleVO> rules;

    public interface AddCheck {
    }

    public interface EditCheck {
    }

    public interface KeyCheck {
    }

    public interface PreviewCheck {
    }

    public PeekDO toDO() {
        PeekDO mdo = new PeekDO();
        BeanUtils.copyProperties(this, mdo);
        mdo.setId(peekId);
        return mdo;
    }
}
