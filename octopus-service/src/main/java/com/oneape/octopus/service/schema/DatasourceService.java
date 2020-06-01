package com.oneape.octopus.service.schema;

import com.oneape.octopus.datasource.DatasourceInfo;
import com.oneape.octopus.model.DO.schema.DatasourceDO;
import com.oneape.octopus.service.BaseService;

import java.util.List;

public interface DatasourceService extends BaseService<DatasourceDO> {

    /**
     * 根据对象进行查询
     *
     * @param datasource DatasourceDO
     * @return List
     */
    List<DatasourceDO> find(DatasourceDO datasource);

    /**
     * 根据Id获取数据源信息
     *
     * @param dsId Long
     * @return DatasourceDO
     */
    DatasourceDO findById(Long dsId);

    /**
     * 根据Id获取数据源信息
     *
     * @param dsId Long
     * @return DatasourceInfo
     */
    DatasourceInfo getDatasourceInfoById(Long dsId);
}
