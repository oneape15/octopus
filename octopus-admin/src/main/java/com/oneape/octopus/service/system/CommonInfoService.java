package com.oneape.octopus.service.system;

import com.oneape.octopus.model.domain.system.CommonInfoDO;
import com.oneape.octopus.service.BaseService;

import java.util.List;

public interface CommonInfoService extends BaseService<CommonInfoDO> {

    /**
     * Get all classified information.
     *
     * @return List
     */
    List<String> getAllClassify();

}
