package com.oneape.octopus.model.DO.peekdata;

import com.oneape.octopus.model.DO.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PeekDO extends BaseDO {
    /**
     * 模型Id
     */
    @Column(name = "model_id")
    private Long    modelId;
    /**
     * 取数实例名称
     */
    private String  name;
    /**
     * 取数次数
     */
    @Column(name = "peek_time")
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
