package com.oneape.octopus.service.report;

import com.oneape.octopus.model.DO.report.ReportGroupDO;
import com.oneape.octopus.model.VO.ReportGroupVO;
import com.oneape.octopus.model.VO.TreeNodeVO;
import com.oneape.octopus.service.BaseService;

import java.util.List;

public interface ReportGroupService extends BaseService<ReportGroupDO> {

    /**
     * 根据报表组信息查询
     *
     * @param group ReportGroupDO
     * @return List
     */
    List<ReportGroupVO> find(ReportGroupDO group);

    /**
     * 获取报表组树型结构
     *
     * @return List
     */
    List<TreeNodeVO> getGroupTree();
}
