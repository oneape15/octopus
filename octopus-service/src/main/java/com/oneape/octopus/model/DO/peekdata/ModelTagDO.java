package com.oneape.octopus.model.DO.peekdata;

import com.oneape.octopus.model.DO.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ModelTagDO extends BaseDO {
    /**
     * 标签名
     */
    private String  name;
    /**
     * 匹配规则
     */
    private String  rule;
    /**
     * 是否默认标签,0为非默认,1为默认
     */
    private Integer defaulted;

    public ModelTagDO(Long id) {
        this.setId(id);
    }
}
