package com.oneape.octopus.service;

import com.oneape.octopus.model.DO.system.RoleDO;
import com.oneape.octopus.model.VO.RoleVO;

import java.util.List;
import java.util.Map;

public interface RoleService extends BaseService<RoleDO> {

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
}
