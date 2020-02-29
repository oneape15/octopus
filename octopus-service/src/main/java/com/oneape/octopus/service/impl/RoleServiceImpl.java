package com.oneape.octopus.service.impl;

import com.oneape.octopus.common.BizException;
import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.mapper.system.RoleMapper;
import com.oneape.octopus.mapper.system.UserRlRoleMapper;
import com.oneape.octopus.model.DO.system.ResourceDO;
import com.oneape.octopus.model.DO.system.RoleDO;
import com.oneape.octopus.model.DO.system.UserRlRoleDO;
import com.oneape.octopus.model.VO.ResourceVO;
import com.oneape.octopus.model.VO.RoleVO;
import com.oneape.octopus.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleMapper roleMapper;
    @Resource
    private UserRlRoleMapper userRlRoleMapper;

    /**
     * 新增数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    @Override
    public int insert(RoleDO model) {
        Assert.isTrue(StringUtils.isNoneBlank(model.getName(), model.getCode()), "角色名称或编码为空");

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
     * 修改数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    @Override
    public int edit(RoleDO model) {
        Assert.isTrue(model.getId() != null, "主键为空");
        Assert.isTrue(StringUtils.isNoneBlank(model.getName(), model.getCode()), "角色名称或编码为空");

        RoleDO tmp = new RoleDO();
        // 判断code或name是否重复
        tmp.setName(model.getName());
        tmp.setCode(model.getCode());
        List<RoleDO> roleDOS = roleMapper.listOrLink(tmp);
        roleDOS = roleDOS.stream().filter((r)-> !r.getId().equals(model.getId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(roleDOS)) {
            long size = roleDOS.stream().filter(roleDO -> model.getId().equals(roleDO.getId())).count();
            if (size > 0) {
                throw new BizException("角色的名称或code已存在");
            }
        }

        return roleMapper.update(model);
    }

    /**
     * 根据主键Id删除
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    @Override
    public int deleteById(RoleDO model) {
        Assert.isTrue(model.getId() != null, "主键为空");
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
}
