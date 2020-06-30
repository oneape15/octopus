package com.oneape.octopus.model.DO.report;

import com.oneape.octopus.annotation.SortId;
import com.oneape.octopus.model.DO.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

/**
 * Report field information table DO.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ReportColumnDO extends BaseDO {
    /**
     * The report Id.
     */
    @Column(name = "report_id")
    private Long    reportId;
    /**
     * The report column name.
     */
    private String  name;
    /**
     * The report column alias name.
     */
    private String  alias;
    /**
     * {@link com.oneape.octopus.datasource.DataType}
     * The report column data type.
     */
    @Column(name = "data_type")
    private String  dataType;
    /**
     * data unit
     */
    private String  unit;
    /**
     * Where does the unit symbol apply. 0 - the table head; 1 - each row item.
     */
    @Column(name = "unit_use_where")
    private Integer unitUseWhere;
    /**
     * The default value when the data item is empty.
     */
    @Column(name = "default_value")
    private String  defaultValue;
    /**
     * Is a hidden column; 0 - no; 1 - yes.
     */
    private Integer hidden;
    /**
     * Type of resource to jump when drilling down a column.
     * eg: 1 - report; 2 - dashboard; 3 - External links.
     */
    @Column(name = "drill_source_type")
    private Integer drillSourceType;
    /**
     * Drill down to the resource URI.
     */
    @Column(name = "drill_uri")
    private String  drillUri;
    /**
     * The parameters required when drilling down the column;
     * eg: kv1=column_name1; kv2=column_name2;
     */
    @Column(name = "drill_params")
    private String  drillParams;
    /**
     * When drilling down the column, open mode.
     * 1 - new window; 2 - Current window; 3 - Popover page.
     */
    @Column(name = "drill_open_type")
    private Integer drillOpenType;
    /**
     * Is a frozen column; 0 - no; 1 - yes
     */
    private Integer frozen;
    /**
     * Whether sorting is supported; 0 - no; 1 - yes
     */
    @Column(name = "support_sort")
    private Integer supportSort;
    /**
     * Data formatting macro
     */
    @Column(name = "format_macro")
    private String  formatMacro;
    /**
     * Sort field
     */
    @SortId
    @Column(name = "sort_id")
    private Long    sortId;
    /**
     * description
     */
    private String  comment;

    public ReportColumnDO(Long reportId) {
        this.reportId = reportId;
    }
}
