package com.oneape.octopus.service;

import com.oneape.octopus.model.DO.report.ReportGroupDO;
import com.oneape.octopus.model.VO.ReportGroupVO;

import java.util.List;

public interface ReportGroupService extends BaseService<ReportGroupDO> {

    /**
     * 根据报表组信息查询
     *
     * @param group ReportGroupDO
     * @return List
     */
    List<ReportGroupVO> listBy(ReportGroupDO group);
}
