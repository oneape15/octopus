package com.oneape.octopus.model.DTO.report;

import com.oneape.octopus.model.DO.report.HelpDocumentDO;
import com.oneape.octopus.model.DO.report.ReportColumnDO;
import com.oneape.octopus.model.DO.report.ReportDO;
import com.oneape.octopus.model.DO.report.ReportDslDO;
import lombok.Data;

import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-06-04 14:56.
 * Modify:
 */
@Data
public class ReportDTO extends ReportDO {
    /**
     * the report column information.
     */
    private List<ReportColumnDO> columns;
    /**
     * the report param information.
     */
    private List<ReportParamDTO> params;
    /**
     * the report dsl sql information.
     */
    private ReportDslDO          dsl;
    /**
     * the report help document.
     */
    private HelpDocumentDO       helpDoc;
}
