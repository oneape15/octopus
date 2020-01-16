package com.oneape.octopus.model.VO;

import com.oneape.octopus.common.GlobalConstant;
import com.oneape.octopus.model.DO.report.ReportColumnDO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
public class ReportColumnVO implements Serializable {
    private Long id;
    //  报表Id
    private Long reportId;
    // 是否为原生的列; 0 - 原生的; 1 - 加工过(分裂新生成的)
    private Integer raw;
    // 列名
    private String name;
    // 显示名
    private String showName;
    // 数据类型
    private String dataType;
    // 数据单位
    private String unit;
    // 是否为隐藏列; 0 - 正常显示; 1 - 隐藏列
    private Integer hidden;
    // 下钻查询详细报表Id
    private Long drillReportId;
    // 下钻列时,需要的参数; kv1= column_name1; kv2 = column_name2;
    private String drillParams;
    // 是否为冻结列; 0 - 否; 1 - 冻结列
    private Integer frozen;
    // 支持排序
    private Integer supportSort;
    // 是否需要分裂; 0 - 否; 1 - 需要
    private Integer split;
    // 分裂分隔字符串
    private String splitChar;
    // 分裂后kv的分隔字符串
    private String splitKvChar;
    // 格式化宏
    private String formatMacro;
    // 排序Id
    private Long sortId;
    // 描述信息
    private String comment;

    public static ReportColumnVO ofDO(ReportColumnDO cdo) {
        ReportColumnVO vo = new ReportColumnVO();
        BeanUtils.copyProperties(cdo, vo);
        return vo;
    }

    public ReportColumnDO toDO() {
        ReportColumnDO cdo = new ReportColumnDO();
        BeanUtils.copyProperties(this, cdo);
        return cdo;
    }
}
