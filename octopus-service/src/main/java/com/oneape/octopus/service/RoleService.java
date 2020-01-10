package com.oneape.octopus.service;

import com.oneape.octopus.model.DO.system.RoleDO;
import com.oneape.octopus.model.VO.RoleVO;

import java.util.List;

public interface RoleService extends BaseService<RoleDO> {

    /**
     * 根据条件查询资源
     *
     * @param role RoleDO
     * @return List
     */
    List<RoleVO> find(RoleDO role);
}
