package com.oneape.octopus.service.serve;

import com.oneape.octopus.model.DO.serve.ServeInfoDO;
import com.oneape.octopus.model.DTO.serve.ServeParamDTO;
import com.oneape.octopus.service.BaseService;

import java.util.List;

public interface ServeInfoService extends BaseService<ServeInfoDO> {

    /**
     * Whether the report Id is valid.
     *
     * @param reportId Long
     * @return boolean true - valid. false - invalid.
     */
    boolean checkReportId(Long reportId);

    /**
     * Judge the correctness of the serve query parameter information.
     *
     * @param params List
     */
    void checkServeParams(List<ServeParamDTO> params);
}
