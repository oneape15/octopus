package com.oneape.octopus.model.DO.report;

import com.oneape.octopus.annotation.SortId;
import com.oneape.octopus.model.enums.ReportParamType;
import com.oneape.octopus.model.DO.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

/**
 * Report query parameter information table DO.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ReportParamDO extends BaseDO {
    /**
     * The report Id.
     */
    @Column(name = "report_id")
    private Long    reportId;
    /**
     * The report param name.
     */
    private String  name;
    /**
     * The report param alias name.
     */
    private String  alias;
    /**
     * The report param data type.
     */
    @Column(name = "data_type")
    private String  dataType;
    /**
     * default value
     */
    @Column(name = "val_default")
    private String  valDefault;
    /**
     * The maximum value
     */
    @Column(name = "val_max")
    private String  valMax;
    /**
     * The minimum value.
     */
    @Column(name = "val_min")
    private String  valMin;
    /**
     * Prohibited value.
     */
    @Column(name = "val_forbidden")
    private String  valForbidden;
    /**
     * Is a required field 0 - no; 1 - yes.
     */
    private Integer required;
    /**
     * Parameters briefly describe information.
     */
    private String  placeholder;
    /**
     * Dependency, taking the name value, multiple separated by commas.
     */
    @Column(name = "depend_on")
    private String  dependOn;
    /**
     * The parameter types; 0 - normal; 1 - inline; 2 - between; 4 - multi;
     * {@link ReportParamType}
     */
    private Integer type;
    /**
     * When the field value content depends on another SQL query result (LOV), fill in.
     */
    @Column(name = "lov_report_id")
    private Long    lovReportId;
    /**
     * The query parameter LOV is the specified KV name.
     * eg: kname,vname
     */
    @Column(name = "lov_kv_name")
    private String  lovKvName;
    /**
     * Sort field.
     */
    @SortId
    @Column(name = "sort_id")
    private Long    sortId;

    public ReportParamDO(Long reportId) {
        this.reportId = reportId;
    }
}
