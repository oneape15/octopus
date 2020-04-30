package com.oneape.octopus.service.report;

import com.oneape.octopus.model.DO.report.SqlLogDO;
import com.oneape.octopus.model.VO.SqlLogVO;
import com.oneape.octopus.service.BaseService;

import java.util.List;

public interface SqlLogService extends BaseService<SqlLogDO> {

    /**
     * 根据条件查询
     *
     * @param model ReportSqlDO
     * @return List
     */
    List<SqlLogVO> find(SqlLogDO model);

    /**
     * 根据id获取详情
     *
     * @param sqlLogId Long
     * @return SqlLogVO
     */
    SqlLogVO getById(Long sqlLogId);
}
