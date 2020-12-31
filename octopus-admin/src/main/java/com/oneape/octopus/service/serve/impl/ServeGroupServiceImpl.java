package com.oneape.octopus.service.serve.impl;

import com.google.common.base.Preconditions;
import com.oneape.octopus.commons.cause.UnauthorizedException;
import com.oneape.octopus.commons.enums.FixServeGroupType;
import com.oneape.octopus.commons.enums.ServeType;
import com.oneape.octopus.commons.value.TypeValueUtils;
import com.oneape.octopus.commons.vo.TreeNodeVO;
import com.oneape.octopus.controller.SessionThreadLocal;
import com.oneape.octopus.domain.serve.ServeGroupDO;
import com.oneape.octopus.dto.serve.ServeGroupSizeDTO;
import com.oneape.octopus.mapper.serve.ServeGroupMapper;
import com.oneape.octopus.mapper.serve.ServeInfoMapper;
import com.oneape.octopus.mapper.serve.ServeRlGroupMapper;
import com.oneape.octopus.service.serve.ServeGroupService;
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
public class ServeGroupServiceImpl implements ServeGroupService {
    @Resource
    private ServeInfoMapper    serveInfoMapper;
    @Resource
    private ServeGroupMapper   serveGroupMapper;
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
        Preconditions.checkNotNull(model, "The group object is null.");
        Preconditions.checkArgument(StringUtils.isNotBlank(model.getName()), "The group name is empty.");

        // whether is updating.
        boolean isUpdate = false;
        if (model.getId() != null && model.getId() > 0) {
            Preconditions.checkNotNull(findById(model.getId()), "The group id is invalid.");
            isUpdate = true;
        }

        Preconditions.checkArgument(hasSameName(model.getName(), isUpdate ? model.getId() : null), "The group name has exist.");

        if (isUpdate) {
            // not allow edit the parent id in here.
            model.setParentId(null);
            return serveGroupMapper.update(model);
        } else {
            // check the parent id.
            Long parentId = TypeValueUtils.getOrDefault(model.getParentId(), FixServeGroupType.ROOT.getId());
            if (!FixServeGroupType.ROOT.getId().equals(parentId)) {
                Preconditions.checkNotNull(serveGroupMapper.findById(parentId), "The parent group id is invalid.");
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
        Preconditions.checkArgument(serveRlGroupMapper.countGroupLinkServeSize(id) <= 0, "The group mounted under the serve does not allow deletion.");
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
        Preconditions.checkNotNull(serveGroupMapper.findById(groupId), "The group Id is Invalid.");
        if (!FixServeGroupType.ROOT.getId().equals(newParentId)) {
            Preconditions.checkNotNull(serveGroupMapper.findById(newParentId), "The parent group Id is Invalid.");
        }

        return serveGroupMapper.changeParentId(groupId, newParentId);
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
    public List<TreeNodeVO> genServeGroupTree(ServeType serveType, boolean addNodeSize, boolean addRootNode, boolean addArchiveNode, boolean addPersonalNode) {
        Preconditions.checkNotNull(serveType, "The serveType is invalid.");
        Long userId = SessionThreadLocal.getUserId();
        if (userId == null) {
            throw new UnauthorizedException();
        }

        // Set sort mode.
        List<String> orders = new ArrayList<>();
        orders.add("sort_id DESC");

        ServeGroupDO query = new ServeGroupDO();
        query.setServeType(serveType.getCode());

        List<TreeNodeVO> nodes = new ArrayList<>();
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
            LinkedHashMap<Long, TreeNodeVO> id2NodeMap = new LinkedHashMap<>();
            groupList.forEach(sg -> {
                Long parentId = TypeValueUtils.getOrDefault(sg.getParentId(), -1L);
                id2parentIdMap.put(sg.getId(), parentId);

                // build group tree node
                TreeNodeVO node = new TreeNodeVO(sg.getId() + "", sg.getName());
                node.setIcon(sg.getIcon());
                node.setLeafSize(groupWithSizeMap.containsKey(sg.getId()) ? groupWithSizeMap.get(sg.getId()) : 0);
                id2NodeMap.put(sg.getId(), node);

                // record parent id
                if (!parentIds.contains(parentId)) {
                    parentIds.add(parentId);
                }
            });

            // build tree
            while (CollectionUtils.isNotEmpty(parentIds)) {
                List<Long> leafNodeIds = new ArrayList<>();
                List<Long> needRemoveParentIds = new ArrayList<>();
                groupIds.stream().filter(id -> !parentIds.contains(id)).forEach(id -> {
                    Long parentId = id2parentIdMap.get(id);

                    leafNodeIds.add(id);
                    needRemoveParentIds.add(parentId);

                    TreeNodeVO node = id2NodeMap.remove(id);
                    TreeNodeVO parentNode = id2NodeMap.get(parentId);

                    if (parentNode != null && node != null) {
                        Integer size = parentNode.getLeafSize() + node.getLeafSize();
                        parentNode.setLeafSize(size);
                        parentNode.addChild(node);
                        id2NodeMap.put(parentId, parentNode);
                    }
                });

                // remove the children info.
                groupIds.removeAll(leafNodeIds);
                parentIds.removeAll(needRemoveParentIds);
            }

            nodes.addAll(id2NodeMap.values());
        }

        // personal node deal.
        if (addPersonalNode) {
            TreeNodeVO personalNode = new TreeNodeVO(FixServeGroupType.PERSONAL.getId().toString(), FixServeGroupType.PERSONAL.getGroupName());
            if (addNodeSize) {
                int size = serveInfoMapper.countPersonalServe(serveType.getCode(), userId);
                personalNode.setLeafSize(size);
            }
            nodes.add(0, personalNode);
        }

        // archive node deal.
        if (addArchiveNode) {
            TreeNodeVO archiveNode = new TreeNodeVO(FixServeGroupType.ARCHIVE.getId().toString(), FixServeGroupType.ARCHIVE.getGroupName());
            if (addNodeSize) {
                int size = serveInfoMapper.countArchiveServe(serveType.getCode());
                archiveNode.setLeafSize(size);
            }
            nodes.add(0, archiveNode);
        }

        // root node deal.
        if (addRootNode) {
            TreeNodeVO rootNode = new TreeNodeVO(FixServeGroupType.ROOT.getId().toString(), FixServeGroupType.ROOT.getGroupName());
            rootNode.addChildren(nodes);
            nodes.clear();
            nodes.add(rootNode);
        }

        return nodes;
    }
}
