package com.oneape.octopus.service.serve;

import com.oneape.octopus.model.domain.serve.ServeInfoDO;
import com.oneape.octopus.model.dto.serve.ServeParamDTO;
import com.oneape.octopus.service.BaseService;

import java.util.List;

public interface ServeInfoService extends BaseService<ServeInfoDO> {

    /**
     * Whether the serveId Id is valid.
     *
     * @param serveId Long
     * @return boolean true - valid. false - invalid.
     */
    boolean checkReportId(Long serveId);

    /**
     * Judge the correctness of the serve query parameter information.
     *
     * @param params List
     */
    void checkServeParams(List<ServeParamDTO> params);
}
