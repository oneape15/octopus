package com.oneape.octopus.service.schema;

import com.oneape.octopus.datasource.DatasourceInfo;
import com.oneape.octopus.model.domain.schema.DatasourceDO;
import com.oneape.octopus.service.BaseService;

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
}
