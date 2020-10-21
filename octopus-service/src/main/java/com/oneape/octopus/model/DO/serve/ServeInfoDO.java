package com.oneape.octopus.model.DO.serve;

import com.oneape.octopus.annotation.SortId;
import com.oneape.octopus.model.DO.BaseDO;
import com.oneape.octopus.model.enums.ReportType;
import com.oneape.octopus.model.enums.VisualType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

/**
 * Serve information table DO.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ServeInfoDO extends BaseDO {
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
     * report type , 1 - table; 2 - interface; 3 - lov ; eg.
     */
    @Column(name = "report_type", nullable = false)
    private Integer reportType;
    /**
     * {@link VisualType}
     * the report visual type , 1 - table; 2 - line; 4 - bar; eg.
     */
    @Column(name = "visual_type", nullable = false)
    private Integer visualType;
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
     * The serve config text information.
     */
    @Column(name = "config_text")
    private String  configText;
    /**
     * description
     */
    private String  comment;

    public ServeInfoDO(String name) {
        this.name = name;
    }


    public ServeInfoDO(Long id) {
        this.setId(id);
    }

    public ServeInfoDO(Long id, String name) {
        setId(id);
        this.name = name;
    }
}
