package com.oneape.octopus.domain.peekdata;

import com.oneape.octopus.commons.annotation.EntityColumn;
import com.oneape.octopus.domain.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
    @EntityColumn(name = "datasource_id")
    private Long    datasourceId;
    /**
     * 具体表名
     */
    @EntityColumn(name = "table_name")
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
