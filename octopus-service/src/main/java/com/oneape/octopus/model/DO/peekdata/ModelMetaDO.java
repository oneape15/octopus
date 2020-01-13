package com.oneape.octopus.model.DO.peekdata;

import com.oneape.octopus.model.DO.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ModelMetaDO extends BaseDO {
    /**
     * 模型Id
     */
    @Column(name = "model_id")
    private Long modelId;
    /**
     * 元素分组名
     */
    @Column(name = "group_name")
    private String groupName;
    /**
     * 元素名称(表字段名称)
     */
    private String name;
    /**
     * 显示名称
     */
    @Column(name = "show_name")
    private String showName;
    /**
     * 数据类型
     */
    @Column(name = "data_type")
    private String dataType;
    /**
     * 原始数据类型
     */
    @Column(name = "origin_data_type")
    private String originDataType;
    /**
     * 是否显示, 1-显示; 0 - 不显示
     */
    private Integer display;
    /**
     * 标签id
     */
    @Column(name = "tag_id")
    private Long tagId;
    /**
     * 字段详细描述
     */
    private String comment;

    public ModelMetaDO(Long modelId) {
        this.modelId = modelId;
    }
}
