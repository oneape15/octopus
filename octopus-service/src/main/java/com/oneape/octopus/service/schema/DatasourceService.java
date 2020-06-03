package com.oneape.octopus.service.schema;

import com.oneape.octopus.datasource.DatasourceInfo;
import com.oneape.octopus.model.DO.schema.DatasourceDO;
import com.oneape.octopus.service.BaseService;

import java.util.List;

public interface DatasourceService extends BaseService<DatasourceDO> {

    /**
     * Query by object.
     *
     * @param datasource DatasourceDO
     * @return List
     */
    List<DatasourceDO> find(DatasourceDO datasource);

    /**
     * Query by data source information  based on the primary key.
     *
     * @param dsId Long
     * @return DatasourceDO
     */
    DatasourceDO findById(Long dsId);

    /**
     * Get the data source information based on the Id.
     *
     * @param dsId Long
     * @return DatasourceInfo
     */
    DatasourceInfo getDatasourceInfoById(Long dsId);
}
