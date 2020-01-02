package com.oneape.octopus.service.impl;

import com.oneape.octopus.mapper.RoleMapper;
import com.oneape.octopus.model.DO.RoleDO;
import com.oneape.octopus.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    /**
     * 新增数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    @Override
    public int insert(RoleDO model) {
        Assert.isTrue(StringUtils.isBlank(model.getName()), "角色名称为空");
        Assert.isTrue(StringUtils.isBlank(model.getCode()), "角色编码为空");

        List<RoleDO> roleDOS = roleMapper.list(new RoleDO(model.getName()));


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
        return roleMapper.delete(model);
    }
}
