package com.oneape.octopus.model.DO.report;

import com.oneape.octopus.annotation.SortId;
import com.oneape.octopus.model.DO.BaseDO;
import com.oneape.octopus.model.enums.ReportType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

/**
 * Report information table DO.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ReportDO extends BaseDO {
    /**
     * report name.
     */
    @Column(name = "name", nullable = false)
    private String  name;
    /**
     * Timeliness of report data. 0 - real time, 1 - minutes, 2 - hours, 3 - days, 4 - months;
     */
    @Column(name = "time_based")
    private Integer timeBased;
    /**
     * {@link ReportType}
     * report type , 1 - table; 2 - pie; 3 - bar , Multiple are separated by commas.
     */
    @Column(name = "report_type", nullable = false)
    private String  reportType;
    /**
     * When the chart is displayed, the X-axis column name; Multiple with ";" separated.
     */
    @Column(name = "x_axis")
    private String  xAxis;
    /**
     * When the chart is displayed, the Y-axis column name; Multiple with ";" separated'
     */
    @Column(name = "y_axis")
    private String  yAxis;
    /**
     * Query the field label display length
     */
    @Column(name = "param_label_len")
    private Integer paramLabelLen;
    /**
     * Query the field control display length
     */
    @Column(name = "param_media_len")
    private Integer paramMediaLen;
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

    public ReportDO(String name) {
        this.name = name;
    }

    public ReportDO(Long id, String name) {
        setId(id);
        this.name = name;
    }
}
