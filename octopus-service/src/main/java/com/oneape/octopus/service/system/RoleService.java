package com.oneape.octopus.service.system;

import com.oneape.octopus.model.DO.system.RoleDO;
import com.oneape.octopus.service.BaseService;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RoleService extends BaseService<RoleDO> {
    /**
     * Get the user role list.
     *
     * @param userId Long
     * @return List
     */
    List<RoleDO> findRoleByUserId(Long userId);

    /**
     * Query resources by condition.
     *
     * @param role RoleDO
     * @return List
     */
    List<RoleDO> find(RoleDO role);

    /**
     * Gets resource permissions based on the list of role ids.
     *
     * @param roleIds List
     * @return Map
     */
    Map<Long, Set<Integer>> getRoleRes(List<Long> roleIds);

    /**
     * Delete the relationship between the user and the role.
     *
     * @param userId Long
     * @return int 1 - success; 0 - fail.
     */
    int deleteRelationshipWithUserId(Long userId);
}
