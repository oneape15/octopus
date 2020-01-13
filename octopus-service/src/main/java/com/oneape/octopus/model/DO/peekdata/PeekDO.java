package com.oneape.octopus.model.DO.peekdata;

import com.oneape.octopus.model.DO.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;

@Data
@EqualsAndHashCode(callSuper = true)
public class PeekDO extends BaseDO {
    /**
     * 模型Id
     */
    @Column(name = "model_id")
    private Long modelId;
    /**
     * 取数实例名称
     */
    private String name;
    /**
     * 返回的数据字段名列表, 多个以","隔开
     */
    @Column(name = "field_list")
    private String fieldList;
    /**
     * 取数次数
     */
    @Column(name = "peek_time")
    private Integer peekTime;
}
