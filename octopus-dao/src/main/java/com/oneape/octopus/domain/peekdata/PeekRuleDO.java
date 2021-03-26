package com.oneape.octopus.domain.peekdata;

import com.oneape.octopus.commons.annotation.EntityColumn;
import com.oneape.octopus.domain.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PeekRuleDO extends BaseDO implements Serializable {
    // 取数id
    @EntityColumn(name = "peek_id")
    private Long   peekId;
    // 字段id
    @EntityColumn(name = "meta_id")
    private Long   metaId;
    // 字段名称
    @EntityColumn(name = "field_name")
    private String fieldName;
    // 规则名称
    private String rule;
    // 代入值
    @EntityColumn(name = "input_value")
    private String inputValue;

    public PeekRuleDO(Long peekId) {
        this.peekId = peekId;
    }
}
