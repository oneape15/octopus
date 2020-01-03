package com.oneape.octopus.model.DO;

import com.oneape.octopus.annotation.SortId;
import com.oneape.octopus.common.GlobalConstant;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@Builder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ReportParamDO extends BaseDO {
    /**
     * 报表Id
     */
    @Column(name = "report_id")
    private Long reportId;
    /**
     * 字段显示名
     */
    @Column(name = "show_name")
    private String showName;
    /**
     * 查询sql代入名
     */
    @Column(name = "param_name")
    private String paramName;
    /**
     * 数据类型
     */
    @Column(name = "data_type")
    private String dataType;
    /**
     * 默认值
     */
    @Column(name = "val_default")
    private String valDefault;
    /**
     * 最大值
     */
    @Column(name = "val_max")
    private String valMax;
    /**
     * 最小值
     */
    @Column(name = "val_min")
    private String valMin;
    /**
     * 禁止使用值
     */
    @Column(name = "val_forbidden")
    private String valForbidden;
    /**
     * 是否为必填字段 0 - 否; 1 - 必填
     */
    @Column(name = "must_fill_in")
    private Integer mustFillIn;
    /**
     * 排序方式 ASC, DESC
     */
    @Column(name = "order_by_type")
    private String orderByType;
    /**
     * 提示信息
     */
    private String placeholder;
    /**
     * 错误提示信息
     */
    @Column(name = "err_message")
    private String errMessage;
    /**
     * 依赖关系, 取paramName值,多个以逗号分隔
     */
    @Column(name = "depend_on")
    private String dependOn;
    /**
     * 字段类型; 1 - 内部字段; 2 - 多选字段; 4 - lov选择;
     * 具体查看类: ReportParamType.java
     * 例: 3 -> 内部字段 且多选字段; 6 -> lov选择 且 多选字段
     */
    private Integer type;

    /**
     * 当字段值内容依赖别一个sql查询结果时(LOV), 填入
     */
    @Column(name = "lov_report_id")
    private Long lovReportId;
    /**
     * 排序字段
     */
    @SortId
    @Column(name = "sort_id")
    private Long sortId = GlobalConstant.DEFAULT_VALUE;
}
