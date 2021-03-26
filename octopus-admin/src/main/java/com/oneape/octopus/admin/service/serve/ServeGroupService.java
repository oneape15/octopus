package com.oneape.octopus.admin.service.serve;

import com.oneape.octopus.commons.enums.ServeType;
import com.oneape.octopus.commons.dto.TreeNodeDTO;
import com.oneape.octopus.domain.serve.ServeGroupDO;
import com.oneape.octopus.admin.service.BaseService;

import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-11-16 16:15.
 * Modify:
 */
public interface ServeGroupService extends BaseService<ServeGroupDO> {

    /**
     * The maximum depth of group tree.
     */
    Integer GROUP_MAX_DEPTH = 8;

    /**
     * Move the group.
     *
     * @param groupId     Long
     * @param newParentId Long
     * @return int 1 - success; 0 - fail.
     */
    int moveGroup(Long groupId, Long newParentId);

    /**
     * Calculate the depth of a branch of the tree.
     *
     * @param groupId Long
     * @return int the depth size.
     */
    int calcTreeDepth(Long groupId);

    /**
     * Calculate the maximum depth of the tree.
     *
     * @param groupId Long
     * @return int the max depth size of children tree.
     */
    int calcChildrenTreeMaxDepth(Long groupId);

    /**
     * check has same name.
     *
     * @param name     String
     * @param filterId Long
     * @return boolean true - has, false - no
     */
    boolean hasSameName(String name, Long filterId);

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
    List<TreeNodeDTO> genServeGroupTree(ServeType serveType,
                                        boolean addNodeSize,
                                        boolean addRootNode,
                                        boolean addArchiveNode,
                                        boolean addPersonalNode);
}
