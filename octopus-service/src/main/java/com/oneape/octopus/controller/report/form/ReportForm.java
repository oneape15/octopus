package com.oneape.octopus.controller.report.form;

import com.oneape.octopus.controller.BaseForm;
import com.oneape.octopus.model.DO.report.*;
import com.oneape.octopus.model.DTO.ReportDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReportForm extends BaseForm implements Serializable {
    @NotNull(message = "The report Id is empty.", groups = {EditCheck.class, KeyCheck.class})
    private Long                 reportId;
    @NotBlank(message = "The Report name is empty.", groups = {AddCheck.class, EditCheck.class})
    private String               name;
    @NotBlank(message = "The report type is empty.", groups = {AddCheck.class, EditCheck.class})
    private String               reportType;
    private String               xAxis;
    private String               yAxis;
    private Integer              paramLabelLen;
    private Integer              paramMediaLen;
    private Long                 sortId;
    private String               comment;
    private List<ReportParamDO>  params;
    private List<ReportColumnDO> columns;
    private ReportDslDO          dsl;
    private HelpDocumentDO       helpDoc;

    public interface AddCheck {
    }

    public interface EditCheck {

    }

    public interface KeyCheck {
    }

    public ReportDTO toDTO() {
        ReportDTO rdo = new ReportDTO();
        BeanUtils.copyProperties(this, rdo);
        rdo.setId(reportId);
        rdo.setParams(params);
        rdo.setColumns(columns);
        rdo.setHelpDoc(helpDoc);

        return rdo;
    }

    public ReportDO toDO() {
        ReportDO rdo = new ReportDO();
        BeanUtils.copyProperties(this, rdo);
        rdo.setId(reportId);
        return rdo;
    }
}
