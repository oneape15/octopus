package com.oneape.octopus.model.DO.report;

import com.oneape.octopus.model.DO.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ReportDslDO extends BaseDO {
    /**
     * the owner Id of report.
     */
    private Long    reportId;
    /**
     * dependency on the data source id
     */
    @Column(name = "datasource_id")
    private Long    datasourceId;
    /**
     * Cache time (seconds)
     */
    @Column(name = "cached_time")
    private Integer cachedTime;
    /**
     * timeout time (seconds)
     */
    private Integer timeout;
    /**
     * dsl sql content
     */
    @Column(name = "text")
    private String  text;
    /**
     * description
     */
    private String  comment;

    public ReportDslDO(Long reportId) {
        this.reportId = reportId;
    }
}
