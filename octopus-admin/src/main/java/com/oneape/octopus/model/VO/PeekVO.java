package com.oneape.octopus.model.VO;

import com.oneape.octopus.model.domain.peekdata.PeekDO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.List;

@Data
public class PeekVO implements Serializable {
    private Long id;
    // 模型Id
    private Long modelId;
    // 取数实例名称
    private String name;
    // 取数次数
    private Integer peekTime;
    // 取数字段
    private List<PeekFieldVO> fields;
    // 取数规则
    private List<PeekRuleVO> rules;


    public static PeekVO ofDO(PeekDO mdo) {
        if (mdo == null) {
            return null;
        }
        PeekVO vo = new PeekVO();
        BeanUtils.copyProperties(mdo, vo);
        return vo;
    }

    public PeekDO toDO() {
        PeekDO pdo = new PeekDO();
        BeanUtils.copyProperties(this, pdo);
        return pdo;
    }
}
