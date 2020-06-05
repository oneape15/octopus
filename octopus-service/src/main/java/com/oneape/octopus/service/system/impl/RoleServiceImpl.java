package com.oneape.octopus.service.system.impl;

import com.google.common.base.Preconditions;
import com.oneape.octopus.common.BizException;
import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.mapper.system.RoleMapper;
import com.oneape.octopus.mapper.system.RoleRlResourceMapper;
import com.oneape.octopus.mapper.system.UserRlRoleMapper;
import com.oneape.octopus.model.DO.system.RoleDO;
import com.oneape.octopus.model.DO.system.UserRlRoleDO;
import com.oneape.octopus.model.VO.RoleVO;
import com.oneape.octopus.service.system.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int insert(RoleDO model) {
        Preconditions.checkArgument(StringUtils.isNoneBlank(model.getName(), model.getCode()), "角色名称或编码为空");

        RoleDO tmp = new RoleDO();
        // 判断code或name是否重复
        tmp.setName(model.getName());
        tmp.setCode(model.getCode());
        List<RoleDO> roleDOS = roleMapper.listOrLink(tmp);
        if (CollectionUtils.isNotEmpty(roleDOS)) {
            throw new BizException("角色的名称或code已存在");
        }

        return roleMapper.insert(model);
    }

    /**
     * Modify the data.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int edit(RoleDO model) {
        Preconditions.checkNotNull(model.getId(), "主键为空");
        Preconditions.checkArgument(StringUtils.isNoneBlank(model.getName(), model.getCode()), "角色名称或编码为空");

        RoleDO tmp = new RoleDO();
        // 判断code或name是否重复
        tmp.setName(model.getName());
        tmp.setCode(model.getCode());
        List<RoleDO> roleDOS = roleMapper.listOrLink(tmp);
        roleDOS = roleDOS.stream().filter((r) -> !r.getId().equals(model.getId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(roleDOS)) {
            long size = roleDOS.stream().filter(roleDO -> model.getId().equals(roleDO.getId())).count();
            if (size > 0) {
                throw new BizException("角色的名称或code已存在");
            }
        }

        return roleMapper.update(model);
    }

    /**
     * Delete by primary key Id.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int deleteById(RoleDO model) {
        Preconditions.checkNotNull(model.getId(), "主键为空");
        RoleDO rdo = roleMapper.findById(model.getId());
        if (rdo == null) {
            throw new BizException("角色信息不存在");
        }
        List<UserRlRoleDO> rlList = userRlRoleMapper.list(new UserRlRoleDO(null, rdo.getId()));
        if (CollectionUtils.isNotEmpty(rlList)) {
            throw new BizException("当前角色还在使用中，不能被删除~");
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
     * 根据条件查询资源
     *
     * @param role RoleDO
     * @return List
     */
    @Override
    public List<RoleVO> find(RoleDO role) {
        List<String> orders = new ArrayList<>();
        orders.add(BaseSqlProvider.FIELD_CREATED + " DESC");
        List<RoleDO> list = roleMapper.listWithOrder(role, orders);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }

        List<RoleVO> vos = new ArrayList<>();
        list.forEach(rdo -> vos.add(RoleVO.ofDO(rdo)));

        return vos;
    }


    /**
     * 根据角色Id列表,获取资源
     *
     * @param roleIds List
     * @return Map
     */
    @Override
    public Map<Long, List<Integer>> getRoleRes(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return new HashMap<>();
        }
        roleRlResourceMapper.getResIdByRoleIds(roleIds);
        return null;
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
