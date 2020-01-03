package com.oneape.octopus.service;

import com.oneape.octopus.model.DO.ReportColumnDO;
import com.oneape.octopus.model.DO.ReportDO;
import com.oneape.octopus.model.DO.ReportParamDO;
import com.oneape.octopus.model.DO.ReportSqlDO;
import com.oneape.octopus.model.VO.ReportVO;

import java.util.List;

public interface ReportService extends BaseService<ReportDO> {
    /***
     * 报表code长度
     */
    public static final int REPORT_CODE_LEN = 10;

    /**
     * 根据报表Id获取全量信息
     *
     * @param reportId Long
     * @return ReportVO
     */
    ReportVO findById(Long reportId);

    /**
     * 添加报表信息
     *
     * @param reportVO ReportVO
     * @return int 0 - 失败; 1 - 成功;
     */
    int addReportInfo(ReportVO reportVO);

    /**
     * 修改报表信息
     *
     * @param reportVO ReportVO
     * @return int 0 - 失败; 1 - 成功;
     */
    int editReportInfo(ReportVO reportVO);

    /**
     * 保存报表查询参数信息
     *
     * @param reportId Long
     * @param params   List
     * @return int 0 - 失败; 1 - 成功;
     */
    int saveReportParams(Long reportId, List<ReportParamDO> params);

    /**
     * 保存报表列信息
     *
     * @param reportId Long
     * @param columns  List
     * @return int 0 - 失败; 1 - 成功;
     */
    int saveReportColumns(Long reportId, List<ReportColumnDO> columns);

    /**
     * 保存报表查询SQL
     *
     * @param reportId  Long
     * @param reportSql ReportSqlDO
     * @return 0 - 失败; 1 - 成功;
     */
    int saveReportSql(Long reportId, ReportSqlDO reportSql);

}
