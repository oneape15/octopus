package com.oneape.octopus.model.DO;

import com.oneape.octopus.annotation.Creator;
import com.oneape.octopus.annotation.SortId;
import com.oneape.octopus.common.GlobalConstant;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

/**
 * 报表信息
 */
@Data
@Builder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ReportDO extends BaseDO {
    /**
     * 报表编码全局唯一
     */
    @Column(name = "code", unique = true)
    private String code;
    /**
     * 名称
     */
    @Column(name = "name", nullable = false)
    private String name;
    /**
     * 图标
     */
    private String icon;
    /**
     * 报表类型
     */
    @Column(name = "report_type", nullable = false)
    private String reportType;
    /**
     * 图表显示时，x轴列名，多个以";"隔开
     */
    @Column(name = "x_axis")
    private String xAxis;
    /**
     * 图表显示时，y轴列表史，多个以";"隔开
     */
    @Column(name = "y_axis")
    private String yAxis;
    /**
     * 报表Sql Id
     */
    @Column(name = "report_sql_id")
    private Long reportSqlId;
    /**
     * 查询字段标签显示长度
     */
    @Column(name = "param_label_len")
    private Integer paramLabelLen;
    /**
     * 查询字段控件显示长度
     */
    @Column(name = "param_media_len")
    private Integer paramMediaLen;
    /**
     * 是否为lov； 0 - 普通报表; 1 - LOV报表
     */
    private Integer lov;
    /**
     * 流量开关 0 - 关； 1 - 开
     */
    @Column(name = "flow_switch")
    private Integer flowSwitch;
    /**
     * 拥有者
     */
    @Creator
    private Long owner = GlobalConstant.SYS_USER;
    /**
     * 排序Id
     */
    @SortId
    @Column(name = "sort_id")
    private Long sortId = GlobalConstant.DEFAULT_VALUE;
    /**
     * 描述信息
     */
    private String comment;
}
