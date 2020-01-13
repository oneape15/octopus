package com.oneape.octopus.service;

import com.oneape.octopus.model.DO.peekdata.ModelTagDO;
import com.oneape.octopus.model.VO.ModelTagVO;

import java.util.List;

public interface ModelTagService extends BaseService<ModelTagDO> {

    /**
     * 根据对象进行查询
     *
     * @param modelTag ModelTagDO
     * @return List
     */
    List<ModelTagVO> find(ModelTagDO modelTag);
}
