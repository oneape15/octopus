package com.oneape.octopus.model.VO;

import com.oneape.octopus.domain.peekdata.PeekRuleDO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
public class PeekRuleVO implements Serializable {
    private Long id;
    // 取数id
    private Long peekId;
    // 字段id
    private Long metaId;
    // 字段名称
    private String fieldName;
    // 规则名称
    private String rule;
    // 代入值
    private String inputValue;

    public static PeekRuleVO ofDO(PeekRuleDO pdo) {
        if (pdo == null) {
            return null;
        }
        PeekRuleVO vo = new PeekRuleVO();
        BeanUtils.copyProperties(pdo, vo);
        return vo;
    }

    public PeekRuleDO toDO() {
        PeekRuleDO pdo = new PeekRuleDO();
        BeanUtils.copyProperties(this, pdo);
        return pdo;
    }
}
