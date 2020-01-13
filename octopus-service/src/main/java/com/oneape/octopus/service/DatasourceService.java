package com.oneape.octopus.service;

import com.oneape.octopus.model.DO.report.DatasourceDO;
import com.oneape.octopus.model.VO.DatasourceVO;

import java.util.List;

public interface DatasourceService extends BaseService<DatasourceDO> {

    /**
     * 根据对象进行查询
     *
     * @param datasource DatasourceDO
     * @return List
     */
    List<DatasourceVO> find(DatasourceDO datasource);

    /**
     * 根据Id获取数据源信息
     * @param dsId Long
     * @return DatasourceDO
     */
    DatasourceDO findById(Long dsId);
}
