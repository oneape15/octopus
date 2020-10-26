package com.oneape.octopus.model.domain.peekdata;

import com.oneape.octopus.model.domain.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ModelDO extends BaseDO {
    /**
     * 模型名称
     */
    private String  name;
    /**
     * 数据源Id
     */
    @Column(name = "datasource_id")
    private Long    datasourceId;
    /**
     * 具体表名
     */
    @Column(name = "table_name")
    private String  tableName;
    /**
     * 模型状态 0 - 使用中; 1 - 已停用
     */
    private Integer status;
    /**
     * 描述
     */
    private String  comment;

    public ModelDO(Long id) {
        this.setId(id);
    }
}
