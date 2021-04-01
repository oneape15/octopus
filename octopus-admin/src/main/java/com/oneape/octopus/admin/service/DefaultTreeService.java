package com.oneape.octopus.admin.service;

import com.oneape.octopus.commons.enums.FixServeGroupType;
import com.oneape.octopus.commons.dto.TreeNodeDTO;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2021-01-25 16:38.
 * Modify:
 */
public class DefaultTreeService {

    /**
     * build a tree
     *
     * @param parentIds      List
     * @param nodeIds        List
     * @param id2parentIdMap Map
     * @param id2NodeMap     Map
     * @return List
     */
    protected List<TreeNodeDTO> buildTree(List<Long> parentIds,
                                          List<Long> nodeIds,
                                          Map<Long, Long> id2parentIdMap,
                                          LinkedHashMap<Long, TreeNodeDTO> id2NodeMap) {
        List<TreeNodeDTO> rootNodes = new ArrayList<>();

        // build tree
        while (CollectionUtils.isNotEmpty(parentIds)) {
            List<Long> leafNodeIds = new ArrayList<>();
            List<Long> needRemoveParentIds = new ArrayList<>();
            nodeIds.stream().filter(id -> !parentIds.contains(id)).forEach(id -> {
                Long parentId = id2parentIdMap.get(id);

                leafNodeIds.add(id);

                TreeNodeDTO node = id2NodeMap.remove(id);
                if (node != null) {
                    TreeNodeDTO parentNode = id2NodeMap.get(parentId);
                    if (FixServeGroupType.ROOT.getId().equals(parentId)) {
                        rootNodes.add(node);
                    } else if (parentNode != null) {
                        if (parentNode.isDisabled()) {
                            setChildrenDisable(node, true);
                        }
                        Integer size = parentNode.getLeafSize() + node.getLeafSize();
                        parentNode.setLeafSize(size);
                        parentNode.addChild(node);
                        id2NodeMap.put(parentId, parentNode);
                    }
                    id2parentIdMap.remove(id);
                }

                if (!id2parentIdMap.containsValue(parentId)) {
                    needRemoveParentIds.add(parentId);
                }
            });

            // remove the children info.
            nodeIds.removeAll(leafNodeIds);
            parentIds.removeAll(needRemoveParentIds);
        }

        return rootNodes;
    }

    /**
     * DISABLED is contagious to all child nodes.
     *
     * @param nodeVO TreeNodeDTO
     */
    protected void setChildrenDisable(TreeNodeDTO nodeVO, boolean disabled) {
        nodeVO.setDisabled(disabled);
        if (CollectionUtils.isNotEmpty(nodeVO.getChildren())) {
            nodeVO.getChildren().forEach(c -> setChildrenDisable(c, disabled));
        }
    }

    /**
     * wrap root node
     *
     * @param nodes List
     */
    protected void wrapRootNode(List<TreeNodeDTO> nodes) {
        if (nodes == null) {
            nodes = new ArrayList<>();
        }

        TreeNodeDTO rootNode = new TreeNodeDTO(FixServeGroupType.ROOT);
        rootNode.addChildren(nodes);
        nodes.clear();
        nodes.add(rootNode);
    }

}
