package com.oneape.octopus.service.impl;

import com.oneape.octopus.common.BizException;
import com.oneape.octopus.common.GlobalConstant;
import com.oneape.octopus.mapper.ResourceMapper;
import com.oneape.octopus.mapper.RoleRlResourceMapper;
import com.oneape.octopus.model.DO.ResourceDO;
import com.oneape.octopus.model.DO.RoleRlResourceDO;
import com.oneape.octopus.service.ResourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class ResourceServiceImpl implements ResourceService {

    @Resource
    private ResourceMapper resourceMapper;
    @Resource
    private RoleRlResourceMapper roleRlResourceMapper;

    /**
     * 新增数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    @Override
    public int insert(ResourceDO model) {
        Assert.isTrue(StringUtils.isNotBlank(model.getName()), "资源名称为空");

        Long parentId = model.getParentId();
        if (parentId == null) {
            parentId = GlobalConstant.DEFAULT_VALUE;
        }
        List<ResourceDO> resources = resourceMapper.list(new ResourceDO(parentId, model.getName()));
        if (CollectionUtils.isNotEmpty(resources)) {
            throw new BizException("存在相同名称的资源~");
        }

        // 设置level值
        if (parentId <= GlobalConstant.DEFAULT_VALUE) {
            model.setLevel(1);
            model.setParentId(GlobalConstant.DEFAULT_VALUE);
        } else {
            ResourceDO resource = resourceMapper.findById(parentId);
            if (resource == null) {
                throw new BizException("上级资源不存在~");
            }
            model.setLevel(resource.getLevel() + 1);
        }

        return resourceMapper.insert(model);
    }

    /**
     * 修改数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    @Override
    public int edit(ResourceDO model) {
        Assert.isTrue(model.getId() != null, "主键Key为空");

        Long parentId = model.getParentId();
        if (parentId == null) {
            parentId = GlobalConstant.DEFAULT_VALUE;
        }
        if (parentId <= GlobalConstant.DEFAULT_VALUE) {
            model.setLevel(1);
            model.setParentId(GlobalConstant.DEFAULT_VALUE);
        } else {
            ResourceDO resource = resourceMapper.findById(model.getParentId());
            if (resource == null) {
                throw new BizException("上级资源不存在~");
            }
            model.setLevel(resource.getLevel() + 1);

            List<ResourceDO> resources = resourceMapper.list(new ResourceDO(parentId, model.getName()));
            if (CollectionUtils.isNotEmpty(resources)) {
                long size = resources.stream().filter(tmp -> !model.getId().equals(tmp.getId())).count();
                if (size > 0) {
                    throw new BizException("存在相同名称的资源~");
                }
            }
        }

        return resourceMapper.update(model);
    }

    /**
     * 根据主键Id删除
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    @Override
    public int deleteById(ResourceDO model) {
        Assert.isTrue(model.getId() != null, "主键Key为空");
        int status = resourceMapper.delete(new ResourceDO(model.getId(), null));
        if (status > GlobalConstant.FAIL) {
            roleRlResourceMapper.delete(new RoleRlResourceDO(null, model.getId()));
        }
        return status;
    }
}
