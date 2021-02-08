package com.oneape.octopus.admin.service.system.impl;

import com.google.common.base.Preconditions;
import com.oneape.octopus.admin.config.I18nMsgConfig;
import com.oneape.octopus.commons.value.MaskUtils;
import com.oneape.octopus.commons.cause.BizException;
import com.oneape.octopus.commons.constant.OctopusConstant;
import com.oneape.octopus.mapper.system.ResourceMapper;
import com.oneape.octopus.mapper.system.RoleRlResourceMapper;
import com.oneape.octopus.domain.system.ResourceDO;
import com.oneape.octopus.domain.system.RoleRlResourceDO;
import com.oneape.octopus.dto.system.ResourceDTO;
import com.oneape.octopus.admin.service.system.ResourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service
public class ResourceServiceImpl implements ResourceService {

    @Resource
    private ResourceMapper       resourceMapper;
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
    @Transactional
    @Override
    public int save(ResourceDO model) {
        Preconditions.checkNotNull(model, I18nMsgConfig.getMessage("resource.info.null"));
        Preconditions.checkArgument(
                StringUtils.isNotBlank(model.getName()),
                I18nMsgConfig.getMessage("resource.name.empty"));
        if (model.getId() != null) {
            Preconditions.checkNotNull(
                    resourceMapper.findById(model.getId()),
                    I18nMsgConfig.getMessage("resource.id.invalid"));
        }

        Long parentId = model.getParentId();
        if (parentId == null) {
            parentId = OctopusConstant.DEFAULT_VALUE;
        }

        if (parentId > OctopusConstant.DEFAULT_VALUE) {
            Preconditions.checkNotNull(
                    resourceMapper.findById(model.getParentId()),
                    I18nMsgConfig.getMessage("resource.parent.null"));

            Preconditions.checkArgument(
                    resourceMapper.getSameBy(parentId, model.getName(), model.getId()) == 0,
                    I18nMsgConfig.getMessage("resource.name.exist"));
        }

        if (model.getId() != null) {
            return resourceMapper.update(model);
        }
        return resourceMapper.insert(model);
    }

    /**
     * Get the model information by the primary key.
     *
     * @param id Long
     * @return T
     */
    @Override
    public ResourceDO findById(Long id) {
        return resourceMapper.findById(id);
    }

    /**
     * Query against an object.
     *
     * @param model T
     * @return List
     */
    @Override
    public List<ResourceDO> find(ResourceDO model) {
        return resourceMapper.list(model);
    }

    /**
     * Delete by primary key Id.
     *
     * @param id Long
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int deleteById(Long id) {
        Preconditions.checkNotNull(id, I18nMsgConfig.getMessage("resource.id.invalid"));

        int status = resourceMapper.delete(new ResourceDO(id));
        if (status > OctopusConstant.FAIL) {
            roleRlResourceMapper.delete(new RoleRlResourceDO(null, id));
        }
        return status;
    }

    /**
     * Gets the specified role resource permission.
     *
     * @param roleId Long
     * @return Map
     */
    @Override
    public Map<Long, List<Integer>> getByRoleId(Long roleId) {
        Map<Long, List<Integer>> ret = new HashMap<>();
        List<RoleRlResourceDO> list = roleRlResourceMapper.getResIdByRoleIds(Collections.singletonList(roleId));
        if (CollectionUtils.isEmpty(list)) {
            return ret;
        }

        list.forEach(r -> ret.put(r.getResourceId(), MaskUtils.getList(r.getMask())));
        return ret;
    }

    /**
     * Gets the list of resources based on the role ID
     *
     * @param roleIds List
     * @return List
     */
    @Override
    public List<ResourceDTO> findByRoleIds(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return new ArrayList<>();
        }
        return resourceMapper.listByRoleIds(roleIds);
    }
}
