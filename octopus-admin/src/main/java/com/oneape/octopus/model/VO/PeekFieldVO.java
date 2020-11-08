package com.oneape.octopus.model.VO;

import com.oneape.octopus.domain.peekdata.PeekFieldDO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
public class PeekFieldVO implements Serializable {
    private Long id;
    //取数id
    private Long peekId;
    //字段id
    private Long metaId;
    // 类型; 0 -维度; 1-指标
    private Integer type;
    // 聚合函数
    private String aggExpression;
    // 数据类型
    private String dataType;
    // 格式
    private String format;

    public static PeekFieldVO ofDO(PeekFieldDO pdo) {
        if (pdo == null) {
            return null;
        }
        PeekFieldVO vo = new PeekFieldVO();
        BeanUtils.copyProperties(pdo, vo);
        return vo;
    }

    public PeekFieldDO toDO() {
        PeekFieldDO pdo = new PeekFieldDO();
        BeanUtils.copyProperties(this, pdo);
        return pdo;
    }
}
