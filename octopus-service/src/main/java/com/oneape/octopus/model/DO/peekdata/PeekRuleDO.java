package com.oneape.octopus.model.DO.peekdata;

import com.oneape.octopus.model.DO.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PeekRuleDO extends BaseDO implements Serializable {
    // 取数id
    @Column(name = "peek_id")
    private Long peekId;
    // 字段id
    @Column(name = "meta_id")
    private Long metaId;
    // 字段名称
    @Column(name = "field_name")
    private String fieldName;
    // 规则名称
    private String rule;
    // 代入值
    @Column(name = "input_value")
    private String inputValue;

    public PeekRuleDO(Long peekId) {
        this.peekId = peekId;
    }
}
