package com.oneape.octopus.controller.peekdata.form;

import com.oneape.octopus.controller.BaseForm;
import com.oneape.octopus.model.DO.peekdata.ModelDO;
import com.oneape.octopus.model.VO.ModelMetaVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ModelForm extends BaseForm implements Serializable {
    @NotNull(message = "主键为空", groups = {EditCheck.class, KeyCheck.class})
    private Long modelId;
    // 模型名称
    @NotBlank(message = "模型名称为空", groups = {AddCheck.class, EditCheck.class})
    private String name;
    // 数据源Id
    @NotNull(message = "数据源Id为空", groups = {AddCheck.class, EditCheck.class, ColumnCheck.class})
    private Long datasourceId;
    // 具体表名
    @NotBlank(message = "具体表名称为空", groups = {AddCheck.class, EditCheck.class, ColumnCheck.class})
    private String tableName;
    // 模型状态 0 - 使用中; 1 - 已停用
    private Integer status;
    // 描述
    private String comment;
    // 模型元素列表
    private List<ModelMetaVO> fields;

    public interface AddCheck {
    }

    public interface EditCheck {
    }

    public interface KeyCheck {
    }

    public interface ColumnCheck {
    }

    public ModelDO toDO() {
        ModelDO mdo = new ModelDO();
        BeanUtils.copyProperties(this, mdo);
        mdo.setId(modelId);
        return mdo;
    }
}
