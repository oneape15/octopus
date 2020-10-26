package com.oneape.octopus.model.VO;

import com.oneape.octopus.model.domain.system.CommonInfoDO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
public class CommonInfoVO implements Serializable {
    private Long id;
    // 父分类
    private Long parentId;
    // 基础信息分类
    private String classify;
    // 编码信息
    private String code;
    // 名称
    private String name;

    public static CommonInfoVO ofDO(CommonInfoDO cdo) {
        if (cdo == null) {
            return null;
        }
        CommonInfoVO vo = new CommonInfoVO();
        BeanUtils.copyProperties(cdo, vo);
        return vo;
    }

    public CommonInfoDO toDO() {
        CommonInfoDO cdo = new CommonInfoDO();
        BeanUtils.copyProperties(this, cdo);
        return cdo;
    }
}
