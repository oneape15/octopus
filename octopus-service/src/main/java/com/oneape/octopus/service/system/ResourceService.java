package com.oneape.octopus.service.system;

import com.oneape.octopus.model.DO.system.ResourceDO;
import com.oneape.octopus.model.DTO.system.ResourceDTO;
import com.oneape.octopus.model.VO.ResourceVO;
import com.oneape.octopus.model.VO.TreeNodeVO;
import com.oneape.octopus.service.BaseService;

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
