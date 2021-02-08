package com.oneape.octopus.admin.service.system.impl;

import com.google.common.base.Preconditions;
import com.oneape.octopus.admin.config.I18nMsgConfig;
import com.oneape.octopus.commons.cause.BizException;
import com.oneape.octopus.commons.enums.FixServeGroupType;
import com.oneape.octopus.commons.value.TypeValueUtils;
import com.oneape.octopus.commons.vo.TreeNodeVO;
import com.oneape.octopus.domain.system.OrganizationDO;
import com.oneape.octopus.domain.system.UserDO;
import com.oneape.octopus.dto.system.OrgUserSizeDTO;
import com.oneape.octopus.mapper.system.OrganizationMapper;
import com.oneape.octopus.mapper.system.UserRlOrgMapper;
import com.oneape.octopus.admin.service.DefaultTreeService;
import com.oneape.octopus.admin.service.system.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2021-01-25 14:45.
 * Modify:
 */
@Slf4j
@Service
public class OrganizationServiceImpl extends DefaultTreeService implements OrganizationService {

    @Resource
    private OrganizationMapper orgMapper;
    @Resource
    private UserRlOrgMapper    userRlOrgMapper;

    /**
     * save data to table.
     * <p>
     * If the Model property ID is not null, the update operation is performed, or the insert operation is performed。
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int save(OrganizationDO model) {
        Preconditions.checkNotNull(model, I18nMsgConfig.getMessage("org.info.null"));
        Preconditions.checkArgument(
                StringUtils.isNoneBlank(model.getName(), model.getCode()),
                I18nMsgConfig.getMessage("org.nameOrCode.empty"));

        // set the default parent node id.
        if (model.getParentId() == null || model.getParentId() <= 0) {
            model.setParentId(0L);
        } else {
            Preconditions.checkNotNull(findById(model.getParentId()), I18nMsgConfig.getMessage("org.parent.invalid"));
        }

        boolean isEdit = false;
        if (model.getId() != null) {
            Preconditions.checkNotNull(findById(model.getId()), I18nMsgConfig.getMessage("org.id.invalid"));
            isEdit = true;
        }

        // Determine if the code or name is repeated.
        int count = orgMapper.getSameNameOrCodeRole(model.getName(), model.getCode(), model.getId());
        if (count > 0) {
            throw new BizException(I18nMsgConfig.getMessage("org.nameOrCode.exist"));
        }

        if (isEdit) {
            return orgMapper.update(model);
        }
        return orgMapper.insert(model);
    }

    /**
     * Delete by primary key Id.
     *
     * @param id Long
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int deleteById(Long id) {
        Preconditions.checkArgument(userRlOrgMapper.getUseSize(id) <= 0,
                I18nMsgConfig.getMessage("org.del.restrict"));
        return orgMapper.deleteById(new OrganizationDO(id));
    }

    /**
     * Get the model information by the primary key.
     *
     * @param id Long
     * @return T
     */
    @Override
    public OrganizationDO findById(Long id) {
        return orgMapper.findById(id);
    }

    /**
     * Query against an object.
     *
     * @param model T
     * @return List
     */
    @Override
    public List<OrganizationDO> find(OrganizationDO model) {
        return orgMapper.list(model);
    }

    /**
     * Building a organization tree.
     *
     * @param addNodeSize  boolean
     * @param addRootNode  boolean
     * @param disabledKeys List
     * @return List
     */
    @Override
    public List<TreeNodeVO> genTree(boolean addNodeSize, boolean addRootNode, List<Long> disabledKeys) {

        // Set sort mode.
        List<String> orders = new ArrayList<>();
        orders.add("id ASC");
        List<OrganizationDO> orgList = orgMapper.listWithOrder(new OrganizationDO(), orders);

        List<TreeNodeVO> nodes = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(orgList)) {
            Map<Long, Long> id2parentIdMap = new HashMap<>();

            // the org with user size
            Map<Long, Integer> groupWithSizeMap = new HashMap<>();

            List<Long> orgIds = new ArrayList<>();
            orgList.forEach(o -> orgIds.add(o.getId()));

            // get all user size
            if (addNodeSize) {
                List<OrgUserSizeDTO> data = userRlOrgMapper.getOrgLinkUserSize(orgIds);
                if (CollectionUtils.isNotEmpty(data)) {
                    data.forEach(d -> groupWithSizeMap.put(d.getOrgId(), d.getSize()));
                }
            }

            List<Long> parentIds = new ArrayList<>();
            LinkedHashMap<Long, TreeNodeVO> id2NodeMap = new LinkedHashMap<>();
            orgList.forEach(sg -> {
                Long parentId = TypeValueUtils.getOrDefault(sg.getParentId(), FixServeGroupType.ROOT.getId());
                id2parentIdMap.put(sg.getId(), parentId);

                // build org tree node
                TreeNodeVO node = new TreeNodeVO(sg.getId() + "", sg.getName());
                node.setDisabled(disabledKeys != null && disabledKeys.contains(sg.getId()));
                node.setLeafSize(groupWithSizeMap.containsKey(sg.getId()) ? groupWithSizeMap.get(sg.getId()) : 0);
                id2NodeMap.put(sg.getId(), node);

                // record parent id
                if (!parentIds.contains(parentId)) {
                    parentIds.add(parentId);
                }
            });

            // build tree
            List<TreeNodeVO> rootNodes = buildTree(parentIds, orgIds, id2parentIdMap, id2NodeMap);

            nodes.addAll(rootNodes);
            nodes.addAll(id2NodeMap.values());
        }

        // root node deal.
        if (addRootNode) {
            wrapRootNode(nodes);
        }

        return nodes;
    }

    /**
     * Query the list of users based on org id.
     *
     * @param orgId Long
     * @return List
     */
    @Override
    public List<UserDO> getUserListByOrgId(Long orgId) {
        Preconditions.checkNotNull(orgMapper.findById(orgId), I18nMsgConfig.getMessage("org.id.invalid"));
        return userRlOrgMapper.getUserByOrgId(orgId);
    }
}
