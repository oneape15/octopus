package com.oneape.octopus.service;

import com.oneape.octopus.model.DO.report.DatasourceDO;
import com.oneape.octopus.model.VO.DatasourceVO;

import java.util.List;

public interface DatasourceService extends BaseService<DatasourceDO> {

    /**
     * 根据对象进行查询
     *
     * @param datasource DatasourceVO
     * @return List
     */
    List<DatasourceVO> find(DatasourceDO datasource);
}
