package com.oneape.octopus.model.VO;

import com.oneape.octopus.model.domain.peekdata.ModelTagDO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
public class ModelTagVO implements Serializable {
    private Long id;
    // 标签名
    private String name;
    // 匹配规则
    private String rule;
    // 是否默认标签,0为非默认,1为默认
    private Integer defaulted;

    public static ModelTagVO ofDO(ModelTagDO mdo) {
        if (mdo == null) {
            return null;
        }
        ModelTagVO vo = new ModelTagVO();
        BeanUtils.copyProperties(mdo, vo);
        return vo;
    }

    public ModelTagDO toDO() {
        ModelTagDO mdo = new ModelTagDO();
        BeanUtils.copyProperties(this, mdo);
        return mdo;
    }
}
