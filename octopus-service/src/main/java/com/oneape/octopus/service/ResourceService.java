package com.oneape.octopus.service;

import com.oneape.octopus.model.DO.system.ResourceDO;
import com.oneape.octopus.model.VO.ResourceVO;

import java.util.List;

public interface ResourceService extends BaseService<ResourceDO> {

    /**
     * 根据条件查询资源
     *
     * @param resource ResourceDO
     * @return List
     */
    List<ResourceVO> findTree(ResourceDO resource);
}
