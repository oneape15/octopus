package com.oneape.octopus.domain.peekdata;

import com.oneape.octopus.commons.enums.EntityColumn;
import com.oneape.octopus.domain.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PeekDO extends BaseDO {
    /**
     * 模型Id
     */
    @EntityColumn(name = "model_id")
    private Long    modelId;
    /**
     * 取数实例名称
     */
    private String  name;
    /**
     * 取数次数
     */
    @EntityColumn(name = "peek_time")
    private Integer peekTime;

    public PeekDO(Long modelId, String name) {
        this.modelId = modelId;
        this.name = name;
    }

    public PeekDO(Long id) {
        this.setId(id);
    }

    public PeekDO(Long id, Long modelId, String name) {
        this.setId(id);
        this.modelId = modelId;
        this.name = name;
    }
}
