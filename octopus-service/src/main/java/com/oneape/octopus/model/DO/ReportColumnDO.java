package com.oneape.octopus.model.DO;

import com.oneape.octopus.annotation.SortId;
import com.oneape.octopus.common.GlobalConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReportColumnDO extends BaseDO {
    /**
     * 报表Id
     */
    @Column(name = "report_id")
    private Long reportId;
    /**
     * 是否为原生的列; 0 - 原生的; 1 - 加工过(分裂新生成的)
     */
    private Integer raw;
    /**
     * 列名
     */
    private String name;
    /**
     * 显示名
     */
    @Column(name = "show_name")
    private String showName;
    /**
     * 数据类型
     */
    @Column(name = "data_type")
    private String dataType;
    /**
     * 数据单位
     */
    private String unit;
    /**
     * 是否为隐藏列; 0 - 正常显示; 1 - 隐藏列
     */
    private Integer hidden;
    /**
     * 下钻查询详细报表Id
     */
    @Column(name = "drill_report_id")
    private Long drillReportId;
    /**
     * 下钻列时,需要的参数; kv1= column_name1; kv2 = column_name2;
     */
    @Column(name = "drill_params")
    private String drillParams;
    /**
     * 是否为冻结列; 0 - 否; 1 - 冻结列
     */
    private Integer frozen;
    /**
     * 支持排序
     */
    @Column(name = "support_sort")
    private Integer supportSort;
    /**
     * 是否需要分裂; 0 - 否; 1 - 需要
     */
    private Integer split;
    /**
     * 分裂分隔字符串
     */
    @Column(name = "split_char")
    private String splitChar;
    /**
     * 分裂后kv的分隔字符串
     */
    @Column(name = "split_kv_char")
    private String splitKvChar;
    /**
     * 格式化宏
     */
    @Column(name = "format_macro")
    private String formatMacro;
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
