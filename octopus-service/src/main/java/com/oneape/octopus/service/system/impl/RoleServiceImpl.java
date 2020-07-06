package com.oneape.octopus.service.system.impl;

import com.google.common.base.Preconditions;
import com.oneape.octopus.common.BizException;
import com.oneape.octopus.common.MaskUtils;
import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.mapper.system.RoleMapper;
import com.oneape.octopus.mapper.system.RoleRlResourceMapper;
import com.oneape.octopus.mapper.system.UserRlRoleMapper;
import com.oneape.octopus.model.DO.system.RoleDO;
import com.oneape.octopus.model.DO.system.RoleRlResourceDO;
import com.oneape.octopus.service.system.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleMapper           roleMapper;
    @Resource
    private UserRlRoleMapper     userRlRoleMapper;
    @Resource
    private RoleRlResourceMapper roleRlResourceMapper;

    /**
     * save data to table.
     * <p>
     * If the Model property ID is not null, the update operation is performed, or the insert operation is performed。
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int save(RoleDO model) {
        Preconditions.checkNotNull(model, "The role information is null.");
        Preconditions.checkArgument(StringUtils.isNoneBlank(model.getName(), model.getCode()), "The role name or code is empty.");
        Preconditions.checkArgument(StringUtils.isNoneBlank(model.getName(), model.getCode()), "The role name or code is empty.");

        // Determine if the code or name is repeated.
        int count = roleMapper.getSameNameOrCodeRole(model.getName(), model.getCode(), model.getId());
        if (count > 0) {
            throw new BizException("The name or code for the role already exists.");
        }

        if (model.getId() != null) {
            return roleMapper.update(model);
        }
        return roleMapper.insert(model);
    }

    /**
     * Delete by primary key Id.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int deleteById(RoleDO model) {
        Preconditions.checkNotNull(model.getId(), "The role primary key is empty.");

        int size = userRlRoleMapper.getUseSize(model.getId());
        if (size > 0) {
            throw new BizException("The current role is still in use and cannot be deleted.");
        }
        return roleMapper.delete(model);
    }

    /**
     * Get the user role list.
     *
     * @param userId Long
     * @return List
     */
    @Override
    public List<RoleDO> findRoleByUserId(Long userId) {
        if (userId == null || userId < 1L) {
            return new ArrayList<>();
        }
        return roleMapper.findRoleByUserId(userId);
    }

    /**
     * Query resources by condition.
     *
     * @param role RoleDO
     * @return List
     */
    @Override
    public List<RoleDO> find(RoleDO role) {
        List<String> orders = new ArrayList<>();
        orders.add(BaseSqlProvider.FIELD_CREATED + " DESC");
        List<RoleDO> list = roleMapper.listWithOrder(role, orders);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }

        return list;
    }


    /**
     * Gets resource permissions based on the list of role ids.
     *
     * @param roleIds List
     * @return Map
     */
    @Override
    public Map<Long, Set<Integer>> getRoleRes(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return new HashMap<>();
        }
        List<RoleRlResourceDO> list = roleRlResourceMapper.getResIdByRoleIds(roleIds);
        Map<Long, Set<Integer>> retMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (RoleRlResourceDO rdo : list) {
                Long resId = rdo.getResourceId();
                if (!retMap.containsKey(resId)) {
                    retMap.put(resId, new HashSet<>());
                }
                List<Integer> maskList = MaskUtils.getList(rdo.getMask());
                retMap.get(resId).addAll(maskList);
            }
        }
        return retMap;
    }

    /**
     * Delete the relationship between the user and the role.
     *
     * @param userId Long
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int deleteRelationshipWithUserId(Long userId) {
        return userRlRoleMapper.deleteByUserId(userId);
    }
}
