package com.oneape.octopus.service.schema;

import com.oneape.octopus.datasource.data.DatasourceInfo;
import com.oneape.octopus.domain.schema.DatasourceDO;
import com.oneape.octopus.service.BaseService;

import java.util.List;

public interface DatasourceService extends BaseService<DatasourceDO> {

    /**
     * Get the data source information based on the Id.
     *
     * @param dsId Long
     * @return DatasourceInfo
     */
    DatasourceInfo getDatasourceInfoById(Long dsId);

    /**
     * Checks if the dsId is valid.
     *
     * @param dsId Long
     * @return boolean true - valid ; false - invalid.
     */
    boolean isExistDsId(Long dsId);

    /**
     * Gets data source information by name.
     *
     * @param name String
     * @return List
     */
    List<DatasourceDO> findByName(String name);

}
