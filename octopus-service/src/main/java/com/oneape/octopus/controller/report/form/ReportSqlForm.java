package com.oneape.octopus.controller.report.form;

import com.oneape.octopus.controller.BaseForm;
import com.oneape.octopus.model.DO.report.ReportDslDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReportSqlForm extends BaseForm implements Serializable {
    @NotNull(message = "the primary key is empty.", groups = {IdCheck.class})
    private Long    sqlId;
    @NotNull(message = "The report id is empty.", groups = {SaveCheck.class})
    private Long    reportId;
    @NotNull(message = "The data source id is empty.", groups = {SaveCheck.class})
    private Long    datasourceId;
    private Integer cachedTime;
    private Integer timeout;
    @NotBlank(message = "The SQL is empty.", groups = {SaveCheck.class})
    private String  text;
    private String  comment;

    public interface IdCheck {
    }

    public interface SaveCheck {
    }

    public ReportDslDO toDO() {
        ReportDslDO rs = new ReportDslDO();
        BeanUtils.copyProperties(this, rs);
        rs.setId(sqlId);
        return rs;
    }
}
