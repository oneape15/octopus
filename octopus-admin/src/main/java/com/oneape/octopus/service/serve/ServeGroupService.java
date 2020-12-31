package com.oneape.octopus.service.serve;

import com.oneape.octopus.commons.enums.ServeType;
import com.oneape.octopus.commons.vo.TreeNodeVO;
import com.oneape.octopus.domain.serve.ServeGroupDO;
import com.oneape.octopus.service.BaseService;

import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-11-16 16:15.
 * Modify:
 */
public interface ServeGroupService extends BaseService<ServeGroupDO> {

    /**
     * Move the group.
     *
     * @param groupId     Long
     * @param newParentId Long
     * @return int 1 - success; 0 - fail.
     */
    int moveGroup(Long groupId, Long newParentId);

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
    List<TreeNodeVO> genServeGroupTree(ServeType serveType,
                                       boolean addNodeSize,
                                       boolean addRootNode,
                                       boolean addArchiveNode,
                                       boolean addPersonalNode);
}
