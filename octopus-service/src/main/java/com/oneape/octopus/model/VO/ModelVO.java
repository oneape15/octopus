package com.oneape.octopus.model.VO;

import com.oneape.octopus.model.DO.peekdata.ModelDO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.List;

@Data
public class ModelVO implements Serializable {
    private Long id;
    // 模型名称
    private String name;
    // 数据源Id
    private Long datasourceId;
    // 具体表名
    private String tableName;
    // 模型状态 0 - 使用中; 1 - 已停用
    private Integer status;
    // 描述
    private String comment;
    // 模型元素信息列表
    private List<ModelMetaVO> fields;

    public static ModelVO ofDO(ModelDO mdo) {
        if (mdo == null) {
            return null;
        }
        ModelVO vo = new ModelVO();
        BeanUtils.copyProperties(mdo, vo);
        return vo;
    }

    public ModelDO toDO() {
        ModelDO mdo = new ModelDO();
        BeanUtils.copyProperties(this, mdo);
        return mdo;
    }
}
