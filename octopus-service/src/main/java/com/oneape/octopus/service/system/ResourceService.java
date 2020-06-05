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
     * 根据条件查询资源
     *
     * @param resource ResourceDO
     * @return List
     */
    List<ResourceVO> findTree(ResourceDO resource);

    /**
     * 获取整棵资源树
     *
     * @return List
     */
    List<TreeNodeVO> fullTree();

    /**
     * 获取指定角色拥有的资源权限集合
     *
     * @param roleId Long 角色Id
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
