package com.oneape.octopus.admin.service.system;

import com.oneape.octopus.commons.dto.TreeNodeDTO;
import com.oneape.octopus.domain.system.OrganizationDO;
import com.oneape.octopus.domain.system.UserDO;
import com.oneape.octopus.admin.service.BaseService;

import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2021-01-25 14:45.
 * Modify:
 */
public interface OrganizationService extends BaseService<OrganizationDO> {

    /**
     * Building a organization tree.
     *
     * @param addNodeSize  boolean
     * @param addRootNode  boolean
     * @param disabledKeys List
     * @return List
     */
    List<TreeNodeDTO> genTree(boolean addNodeSize, boolean addRootNode, List<Long> disabledKeys);

    /**
     * Query the list of users based on org id.
     *
     * @param orgId Long
     * @return List
     */
    List<UserDO> getUserListByOrgId(Long orgId);

}
