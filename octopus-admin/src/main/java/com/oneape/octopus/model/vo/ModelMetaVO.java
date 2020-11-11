package com.oneape.octopus.model.vo;

import com.oneape.octopus.domain.peekdata.ModelMetaDO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
public class ModelMetaVO implements Serializable {
    private Long id;
    // 模型Id
    private Long modelId;
    // 元素名称(表字段名称)
    private String name;
    // 显示名称
    private String showName;
    // 数据类型
    private String dataType;
    // 原始数据类型
    private String originDataType;
    // 是否显示, 1-显示; 0 - 不显示
    private Integer display;
    // 标签id
    private Long tagId;
    // 字段详细描述
    private String comment;

    public static ModelMetaVO ofDO(ModelMetaDO mdo) {
        if (mdo == null) {
            return null;
        }
        ModelMetaVO vo = new ModelMetaVO();
        BeanUtils.copyProperties(mdo, vo);

        return vo;
    }

    public ModelMetaDO toDO() {
        ModelMetaDO mdo = new ModelMetaDO();
        BeanUtils.copyProperties(this, mdo);
        return mdo;
    }
}
