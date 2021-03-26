package com.oneape.octopus.admin.service.serve;

import com.oneape.octopus.domain.serve.ServeInfoDO;
import com.oneape.octopus.admin.service.BaseService;

public interface ServeInfoService extends BaseService<ServeInfoDO> {

    /**
     * The serve code length.
     */
    Integer CODE_LEN = 16;
    String COPY_TAG = "_copy_";
    Integer COPY_TAG_RANDOM_LEN = 6;

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
     * Copy a new service with the specified service as the template.
     * If the version code is not empty, a version is used as a template.
     *
     * @param serveId Long
     * @param verCode String
     * @return int 1 - success; 0 - fail.
     */
    int copyById(Long serveId, String verCode);

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
     * Rolls back the specified version of the service.
     *
     * @param serveId     Long
     * @param versionCode String
     * @return int 1 - success, 0 - fail
     */
    int rollbackServe(Long serveId, String versionCode);

    /**
     * Move serve to another group.
     *
     * @param serveId Long
     * @param groupId Long
     * @return int 1 - success, 0 - fail
     */
    int moveServe(Long serveId, Long groupId);

    /**
     * Change the serve owner id.
     *
     * @param serveId Long
     * @param ownerId Long
     * @return int 1 - success, 0 - fail
     */
    int changeServeOwner(Long serveId, Long ownerId);
}
