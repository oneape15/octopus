package com.oneape.octopus.service;

import com.oneape.octopus.model.DO.report.ReportDO;
import com.oneape.octopus.model.DO.report.ReportSqlDO;
import com.oneape.octopus.model.VO.*;

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
     * 根据对象进行查询
     *
     * @param model ReportDO
     * @return List
     */
    List<ReportVO> find(ReportDO model);

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
    int saveReportParams(Long reportId, List<ReportParamVO> params);

    /**
     * 保存报表列信息
     *
     * @param reportId Long
     * @param columns  List
     * @return int 0 - 失败; 1 - 成功;
     */
    int saveReportColumns(Long reportId, List<ReportColumnVO> columns);

    /**
     * 保存报表查询SQL
     *
     * @param reportId  Long
     * @param reportSql ReportSqlDO
     * @return 0 - 失败; 1 - 成功;
     */
    int saveReportSql(Long reportId, ReportSqlDO reportSql);

    /**
     * 根据报表Sql主键获取信息
     *
     * @param sqlId Long
     * @return ReportSqlVO
     */
    ReportSqlVO getReportSql(Long sqlId);

    /**
     * 根据指定报表Id复制一张报表
     *
     * @param templateReportId Long
     * @return int
     */
    int copyReport(Long templateReportId);

    /**
     * 获取报表树型结构(报表组与报表结合)
     *
     * @param lovReport null - 查询所有; 0 - 普通报表; 1 - 简单的报表(LOV);
     * @param filterIds List 过滤掉的报表Id
     * @param fixOption String 携带特定值, NULL, ALL
     * @return List
     */
    List<TreeNodeVO> getReportTree(Integer lovReport, List<Long> filterIds, String fixOption);

    /**
     * 根据报表Id获取查询参数列表
     *
     * @param reportId Long
     * @return List
     */
    List<ReportParamVO> getParamByReportId(Long reportId);

    /**
     * 根据报表Id获取字段列表
     *
     * @param reportId Long
     * @return List
     */
    List<ReportColumnVO> getColumnByReportId(Long reportId);

}
