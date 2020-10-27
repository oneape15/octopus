package com.oneape.octopus.service.serve;

import com.oneape.octopus.model.domain.serve.ServeInfoDO;
import com.oneape.octopus.service.BaseService;

public interface ServeInfoService extends BaseService<ServeInfoDO> {

    /**
     * Whether the serveId Id is valid.
     *
     * @param serveId Long
     * @return boolean true - valid. false - invalid.
     */
    boolean checkReportId(Long serveId);
}
