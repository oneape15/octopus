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
public class PeekFieldDO extends BaseDO implements Serializable {
    //取数id
    @Column(name = "peek_id")
    private Long peekId;
    //字段id
    @Column(name = "meta_id")
    private Long metaId;
    // 类型; 0 -维度; 1-指标
    private Integer type;
    // 聚合函数
    @Column(name = "agg_expression")
    private String aggExpression;
    // 数据类型
    @Column(name = "data_type")
    private String dataType;
    // 格式
    private String format;

    public PeekFieldDO(Long peekId) {
        this.peekId = peekId;
    }
}
