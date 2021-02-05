package com.oneape.octopus.admin.service.system;

import com.oneape.octopus.domain.system.ResourceDO;
import com.oneape.octopus.dto.system.ResourceDTO;
import com.oneape.octopus.admin.service.BaseService;

import java.util.List;
import java.util.Map;

public interface ResourceService extends BaseService<ResourceDO> {

    /**
     * Gets the specified role resource permission.
     *
     * @param roleId Long
     * @return Map
     */
    Map<Long, List<Integer>> getByRoleId(Long roleId);

    /**
     * Gets the list of resources based on the role ID
     *
     * @param roleIds List
     * @return List
     */
    List<ResourceDTO> findByRoleIds(List<Long> roleIds);
}
