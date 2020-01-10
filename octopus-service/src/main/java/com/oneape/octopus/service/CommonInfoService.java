package com.oneape.octopus.service;

import com.oneape.octopus.model.DO.system.CommonInfoDO;
import com.oneape.octopus.model.VO.CommonInfoVO;

import java.util.List;

public interface CommonInfoService extends BaseService<CommonInfoDO> {

    /**
     * 根据对象进行查询
     *
     * @param commonInfo CommonInfoDO
     * @return List
     */
    List<CommonInfoVO> find(CommonInfoDO commonInfo);

    /**
     * 获取所有分类信息
     *
     * @return List
     */
    List<String> getAllClassify();
}
