package com.oneape.octopus.admin.service.serve.impl;

import com.google.common.base.Preconditions;
import com.oneape.octopus.admin.config.I18nMsgConfig;
import com.oneape.octopus.admin.config.SessionThreadLocal;
import com.oneape.octopus.admin.service.DefaultTreeService;
import com.oneape.octopus.admin.service.serve.ServeGroupService;
import com.oneape.octopus.commons.cause.BizException;
import com.oneape.octopus.commons.cause.UnauthorizedException;
import com.oneape.octopus.commons.enums.FixServeGroupType;
import com.oneape.octopus.commons.enums.ServeType;
import com.oneape.octopus.commons.value.DataUtils;
import com.oneape.octopus.commons.dto.TreeNodeDTO;
import com.oneape.octopus.domain.serve.ServeGroupDO;
import com.oneape.octopus.dto.serve.ServeGroupSizeDTO;
import com.oneape.octopus.mapper.serve.ServeGroupMapper;
import com.oneape.octopus.mapper.serve.ServeInfoMapper;
import com.oneape.octopus.mapper.serve.ServeRlGroupMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-11-16 16:39.
 * Modify:
 */
@Slf4j
@Service
public class ServeGroupServiceImpl extends DefaultTreeService implements ServeGroupService {
    @Resource
    private ServeInfoMapper serveInfoMapper;
    @Resource
    private ServeGroupMapper serveGroupMapper;
    @Resource
    private ServeRlGroupMapper serveRlGroupMapper;

    /**
     * save data to table.
     * <p>
     * If the Model property ID is not null, the update operation is performed, or the insert operation is performed。
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int save(ServeGroupDO model) {
        Preconditions.checkNotNull(model, I18nMsgConfig.getMessage("serve.group.null"));
        Preconditions.checkArgument(StringUtils.isNotBlank(model.getName()),
                I18nMsgConfig.getMessage("serve.group.name.empty"));
        Preconditions.checkArgument(StringUtils.isNotBlank(model.getServeType()),
                I18nMsgConfig.getMessage("serve.group.type.invalid"));

        // whether is updating.
        boolean isUpdate = false;
        if (model.getId() != null && model.getId() > 0) {
            Preconditions.checkNotNull(findById(model.getId()),
                    I18nMsgConfig.getMessage("serve.group.id.invalid"));
            isUpdate = true;
        }

        Preconditions.checkArgument(!hasSameName(model.getName(), isUpdate ? model.getId() : null),
                I18nMsgConfig.getMessage("serve.group.name.exist"));

        if (isUpdate) {
            // not allow edit the parent id in here.
            model.setParentId(null);
            // not allow edit the serve type in here.
            model.setServeType(null);
            return serveGroupMapper.update(model);
        } else {
            // check the parent id.
            Long parentId = DataUtils.getOrDefault(model.getParentId(), FixServeGroupType.ROOT.getId());
            if (!FixServeGroupType.ROOT.getId().equals(parentId)) {
                Preconditions.checkNotNull(serveGroupMapper.findById(parentId),
                        I18nMsgConfig.getMessage("serve.group.parentId.invalid"));
                Preconditions.checkArgument(calcTreeDepth(parentId) <= GROUP_MAX_DEPTH,
                        I18nMsgConfig.getMessage("serve.group.treeMaxDepth", GROUP_MAX_DEPTH));
            }

            return serveGroupMapper.insert(model);
        }
    }

    /**
     * Delete by primary key Id.
     *
     * @param id Long
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int deleteById(Long id) {
        if (id == null || id < 0) return 1;
        Preconditions.checkArgument(serveRlGroupMapper.countGroupLinkServeSize(id) <= 0,
                I18nMsgConfig.getMessage("serve.group.del.restrict"));
        return serveGroupMapper.delete(new ServeGroupDO(id));
    }

    /**
     * Get the model information by the primary key.
     *
     * @param id Long
     * @return T
     */
    @Override
    public ServeGroupDO findById(Long id) {
        return serveGroupMapper.findById(id);
    }

    /**
     * Query against an object.
     *
     * @param model T
     * @return List
     */
    @Override
    public List<ServeGroupDO> find(ServeGroupDO model) {
        return serveGroupMapper.list(model);
    }

    /**
     * Move the group.
     *
     * @param groupId     Long
     * @param newParentId Long
     * @return int 1 - success; 0 - fail.
     */
    @Transactional
    @Override
    public int moveGroup(Long groupId, Long newParentId) {
        Preconditions.checkNotNull(
                serveGroupMapper.findById(groupId),
                I18nMsgConfig.getMessage("serve.group.id.invalid"));
        if (!FixServeGroupType.ROOT.getId().equals(newParentId)) {
            Preconditions.checkNotNull(
                    serveGroupMapper.findById(newParentId),
                    I18nMsgConfig.getMessage("serve.group.parentId.invalid"));
        }

        Integer parentDepth = calcTreeDepth(newParentId);
        Integer selfDepth = calcChildrenTreeMaxDepth(groupId);
        if (parentDepth + selfDepth > GROUP_MAX_DEPTH) {
            throw new BizException(I18nMsgConfig.getMessage("serve.group.treeMaxDepth", GROUP_MAX_DEPTH));
        }

        return serveGroupMapper.changeParentId(groupId, newParentId);
    }

    /**
     * Calculate the depth of a branch of the tree.
     *
     * @param groupId Long
     * @return int the depth size.
     */
    @Override
    public int calcTreeDepth(Long groupId) {
        List<ServeGroupDO> list = serveGroupMapper.listAllOfIdAndParentId();
        Map<Long, Long> id2parentIdMap = new HashMap<>();
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }

        list.forEach(l -> id2parentIdMap.put(l.getId(), l.getParentId()));

        int depth = 0;
        Long parentId = groupId;
        while (!FixServeGroupType.ROOT.getId().equals(parentId)) {
            parentId = id2parentIdMap.get(parentId);
            if (parentId == null) {
                parentId = FixServeGroupType.ROOT.getId();
            }
            depth++;
        }

        return depth;
    }

    /**
     * Calculate the maximum depth of the tree.
     *
     * @param groupId Long
     * @return int the max depth size of children tree.
     */
    @Override
    public int calcChildrenTreeMaxDepth(Long groupId) {
        return 0;
    }

    /**
     * check has same name.
     *
     * @param name     String
     * @param filterId Long
     * @return boolean true - has, false - no
     */
    @Override
    public boolean hasSameName(String name, Long filterId) {
        int count = serveGroupMapper.checkHasTheSameName(name, filterId);
        return count > 0;
    }

    /**
     * Generate a service resource tree.
     *
     * @param serveType       ServeType
     * @param addNodeSize     boolean
     * @param addRootNode     boolean
     * @param addArchiveNode  boolean
     * @param addPersonalNode boolean
     * @return List
     */
    @Override
    public List<TreeNodeDTO> genServeGroupTree(ServeType serveType, boolean addNodeSize, boolean addRootNode, boolean addArchiveNode, boolean addPersonalNode) {
        Preconditions.checkNotNull(serveType, I18nMsgConfig.getMessage("serve.type.invalid"));
        Long userId = SessionThreadLocal.getUserId();
        if (userId == null) {
            throw new UnauthorizedException();
        }

        // Set sort mode.
        List<String> orders = new ArrayList<>();
        orders.add("sort_id DESC");

        ServeGroupDO query = new ServeGroupDO();
        query.setServeType(serveType.getCode());

        List<TreeNodeDTO> nodes = new ArrayList<>();
        List<ServeGroupDO> groupList = serveGroupMapper.listWithOrder(query, orders);
        if (CollectionUtils.isNotEmpty(groupList)) {
            Map<Long, Long> id2parentIdMap = new HashMap<>();
            // the group with serve size
            Map<Long, Integer> groupWithSizeMap = new HashMap<>();

            List<Long> groupIds = new ArrayList<>();
            groupList.forEach(g -> groupIds.add(g.getId()));

            // get all serve size
            if (addNodeSize) {
                List<ServeGroupSizeDTO> data = serveRlGroupMapper.getGroupLinkServeSize(groupIds);
                if (CollectionUtils.isNotEmpty(data)) {
                    data.forEach(d -> groupWithSizeMap.put(d.getGroupId(), d.getSize()));
                }
            }

            List<Long> parentIds = new ArrayList<>();
            LinkedHashMap<Long, TreeNodeDTO> id2NodeMap = new LinkedHashMap<>();
            groupList.forEach(sg -> {
                Long parentId = DataUtils.getOrDefault(sg.getParentId(), FixServeGroupType.ROOT.getId());
                id2parentIdMap.put(sg.getId(), parentId);

                // build group tree node
                TreeNodeDTO node = new TreeNodeDTO(sg.getId() + "", sg.getName());
                node.setPKey(parentId + "");
                node.setIcon(sg.getIcon());
                node.setLeafSize(groupWithSizeMap.getOrDefault(sg.getId(), 0));
                id2NodeMap.put(sg.getId(), node);

                // record parent id
                if (!parentIds.contains(parentId)) {
                    parentIds.add(parentId);
                }
            });

            // build tree
            List<TreeNodeDTO> rootNodes = buildTree(parentIds, groupIds, id2parentIdMap, id2NodeMap);

            nodes.addAll(rootNodes);
            nodes.addAll(id2NodeMap.values());
        }

        // personal node deal.
        if (addPersonalNode) {
            TreeNodeDTO personalNode = new TreeNodeDTO(FixServeGroupType.PERSONAL);
            if (addNodeSize) {
                int size = serveInfoMapper.countPersonalServe(serveType.getCode(), userId);
                personalNode.setLeafSize(size);
            }
            nodes.add(0, personalNode);
        }

        // archive node deal.
        if (addArchiveNode) {
            TreeNodeDTO archiveNode = new TreeNodeDTO(FixServeGroupType.ARCHIVE);
            if (addNodeSize) {
                int size = serveInfoMapper.countArchiveServe(serveType.getCode());
                archiveNode.setLeafSize(size);
            }
            nodes.add(0, archiveNode);
        }

        // root node deal.
        if (addRootNode) {
            wrapRootNode(nodes);
        }

        return nodes;
    }
}
