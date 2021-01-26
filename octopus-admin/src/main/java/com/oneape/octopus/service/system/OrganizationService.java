package com.oneape.octopus.service.system;

import com.oneape.octopus.commons.vo.TreeNodeVO;
import com.oneape.octopus.domain.system.OrganizationDO;
import com.oneape.octopus.service.BaseService;

import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2021-01-25 14:45.
 * Modify:
 */
public interface OrganizationService extends BaseService<OrganizationDO> {

    /**
     * Building a organization tree.
     *
     * @param addNodeSize  boolean
     * @param addRootNode  boolean
     * @param disabledKeys List
     * @return List
     */
    List<TreeNodeVO> genTree(boolean addNodeSize, boolean addRootNode, List<Long> disabledKeys);

}
