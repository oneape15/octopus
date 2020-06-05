package com.oneape.octopus.service.system;

import com.oneape.octopus.model.DO.system.RoleDO;
import com.oneape.octopus.model.VO.RoleVO;
import com.oneape.octopus.service.BaseService;

import java.util.List;
import java.util.Map;

public interface RoleService extends BaseService<RoleDO> {

    /**
     * Get the user role list.
     *
     * @param userId Long
     * @return List
     */
    List<RoleDO> findRoleByUserId(Long userId);

    /**
     * 根据条件查询资源
     *
     * @param role RoleDO
     * @return List
     */
    List<RoleVO> find(RoleDO role);

    /**
     * 根据角色Id列表,获取资源
     *
     * @param roleIds List
     * @return Map
     */
    Map<Long, List<Integer>> getRoleRes(List<Long> roleIds);

    /**
     * Delete the relationship between the user and the role.
     *
     * @param userId Long
     * @return int 1 - success; 0 - fail.
     */
    int deleteRelationshipWithUserId(Long userId);
}
