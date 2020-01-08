package com.oneape.octopus.model.DO.report;

import com.oneape.octopus.model.DO.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GroupRlReportDO extends BaseDO {
    @Column(name = "report_id")
    private Long reportId;
    @Column(name = "group_id")
    private Long groupId;

    public GroupRlReportDO(Long reportId, Long groupId) {
        this.reportId = reportId;
        this.groupId = groupId;
    }
}
