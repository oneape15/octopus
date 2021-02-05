package com.oneape.octopus.admin.service.system;

import com.oneape.octopus.domain.system.CommonInfoDO;
import com.oneape.octopus.admin.service.BaseService;

import java.util.List;

public interface CommonInfoService extends BaseService<CommonInfoDO> {

    /**
     * Get all classified information.
     *
     * @return List
     */
    List<String> getAllClassify();

}
