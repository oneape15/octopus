package com.oneape.octopus.service.system;

import com.oneape.octopus.model.DO.system.CommonInfoDO;
import com.oneape.octopus.model.VO.CommonInfoVO;
import com.oneape.octopus.model.VO.TreeNodeVO;
import com.oneape.octopus.service.BaseService;

import java.util.List;

public interface CommonInfoService extends BaseService<CommonInfoDO> {

    /**
     * Query against an object.
     *
     * @param commonInfo CommonInfoDO
     * @return List
     */
    List<CommonInfoDO> find(CommonInfoDO commonInfo);

    /**
     * Get all classified information.
     *
     * @return List
     */
    List<String> getAllClassify();

}
