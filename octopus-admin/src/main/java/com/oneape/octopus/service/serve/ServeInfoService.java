package com.oneape.octopus.service.serve;

import com.oneape.octopus.domain.serve.ServeInfoDO;
import com.oneape.octopus.service.BaseService;

public interface ServeInfoService extends BaseService<ServeInfoDO> {

    /**
     * Whether the serveId Id is valid.
     *
     * @param serveId Long
     * @return boolean true - valid. false - invalid.
     */
    boolean checkReportId(Long serveId);

    /**
     * 1. change the serve status to PUBLISH;
     * 2. Save one version to the version list.
     *
     * @param serveId Long
     * @return int 1 - success, 0 - fail
     */
    int publishServe(Long serveId);
}
