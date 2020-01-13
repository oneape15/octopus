package com.oneape.octopus.model.VO;

import com.oneape.octopus.model.DO.peekdata.PeekDO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
public class PeekVO implements Serializable {
    private Long id;
    // 模型Id
    private Long modelId;
    // 取数实例名称
    private String name;
    // 返回的数据字段名列表, 多个以","隔开
    private String fieldList;
    // 取数次数
    private Integer peekTime;


    public static PeekVO ofDO(PeekDO mdo) {
        if (mdo == null) {
            return null;
        }
        PeekVO vo = new PeekVO();
        BeanUtils.copyProperties(mdo, vo);
        return vo;
    }

    public PeekDO toDO() {
        PeekDO pdo = new PeekDO();
        BeanUtils.copyProperties(this, pdo);
        return pdo;
    }
}
