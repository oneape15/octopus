package com.oneape.octopus.service.report;

import com.oneape.octopus.model.DO.report.*;
import com.oneape.octopus.model.DTO.ReportDTO;
import com.oneape.octopus.model.VO.report.ReportConfigVO;
import com.oneape.octopus.service.BaseService;

import java.util.List;

public interface ReportService extends BaseService<ReportDO> {

    /**
     * Whether the report Id is valid.
     *
     * @param reportId Long
     * @return boolean true - valid. false - invalid.
     */
    boolean checkReportId(Long reportId);

    /**
     * Get the full amount of information based on the report Id.
     *
     * @param reportId Long
     * @return ReportDO
     */
    ReportDTO findById(Long reportId);

    /**
     * Query against an object.
     *
     * @param model ReportDO
     * @return List
     */
    List<ReportDO> find(ReportDO model);

    /**
     * Save report information.
     *
     * @param rDto ReportDTO
     * @return int 0 - fail; 1 - success;
     */
    int saveReportInfo(ReportDTO rDto);

    /**
     * Save the report query parameter information.
     *
     * @param reportId Long
     * @param params   List
     * @return int 0 - fail; 1 - success;
     */
    int saveReportParams(Long reportId, List<ReportParamDO> params);

    /**
     * Save the report column information.
     *
     * @param reportId Long
     * @param columns  List
     * @return int 0 - fail; 1 - success;
     */
    int saveReportColumns(Long reportId, List<ReportColumnDO> columns);

    /**
     * Save the report query SQL
     *
     * @param reportSql ReportDslDO
     * @return 0 - fail; 1 - success;
     */
    int saveReportDslSql(ReportDslDO reportSql);

    /**
     * Save the help document information.
     *
     * @param hdDo HelpDocumentDO
     * @return 0 - fail; 1 - success;
     */
    int saveHelpDocument(HelpDocumentDO hdDo);

    /**
     * Get DSL information based on the report Id.
     *
     * @param sqlId Long
     * @return ReportDslDO
     */
    ReportDslDO getReportSql(Long sqlId);

    /**
     * Get the list of query parameters based on the report Id.
     *
     * @param reportId Long
     * @return List
     */
    List<ReportParamDO> getParamByReportId(Long reportId);

    /**
     * Get the list of query columns based on the report Id.
     *
     * @param reportId Long
     * @return List
     */
    List<ReportColumnDO> getColumnByReportId(Long reportId);

    /**
     * Gets report base information for front-end page rendering.
     *
     * @param reportId Long
     * @return ReportConfigVO
     */
    ReportConfigVO getReportConfig(Long reportId);

}
