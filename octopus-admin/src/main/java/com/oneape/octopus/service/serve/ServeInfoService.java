package com.oneape.octopus.service.serve;

import com.oneape.octopus.domain.serve.ServeInfoDO;
import com.oneape.octopus.service.BaseService;

public interface ServeInfoService extends BaseService<ServeInfoDO> {

    /**
     * save data to table.
     * <p>
     * If the Model property ID is not null, the update operation is performed, or the insert operation is performed。
     *
     * @param model ServeInfoDO
     * @return int 1 - success; 0 - fail.
     */
    int save(ServeInfoDO model, Long groupId);

    /**
     * Whether the serveId Id is valid.
     *
     * @param serveId Long
     * @return boolean true - valid. false - invalid.
     */
    boolean checkServeId(Long serveId);

    /**
     * 1. change the serve status to PUBLISH;
     * 2. Save one version to the version list.
     *
     * @param serveId Long
     * @return int 1 - success, 0 - fail
     */
    int publishServe(Long serveId);

    /**
     * Move serve to another group.
     *
     * @param serveId Long
     * @param groupId Long
     * @return int 1 - success, 0 - fail
     */
    int moveServe(Long serveId, Long groupId);
}
