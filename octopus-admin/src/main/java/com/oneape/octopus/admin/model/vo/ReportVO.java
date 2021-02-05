package com.oneape.octopus.admin.model.vo;

import com.oneape.octopus.domain.serve.ServeInfoDO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.List;

@Data
public class ReportVO implements Serializable {
    private Long id;
    // 报表编码全局唯一
    private String code;
    // 名称
    private String name;
    // 图标
    private String icon;
    // 报表类型
    private String reportType;
    // 图表显示时，x轴列名，多个以";"隔开
    private String xAxis;
    // 图表显示时，y轴列表史，多个以";"隔开
    private String yAxis;
    // 报表Sql Id
    private Long reportSqlId;
    // 查询字段标签显示长度
    private Integer paramLabelLen;
    // 查询字段控件显示长度
    private Integer paramMediaLen;
    // 是否为lov； 0 - 普通报表; 1 - LOV报表
    private Integer lov;
    // 流量开关 0 - 关； 1 - 开
    private Integer flowSwitch;
    // 拥有者
    private Long owner;
    // 排序Id
    private Long sortId;
    // 描述信息
    private String comment;
    // 所在报表组Id
    private List<Long> groupIdList;
    private String groupIds;
    // 查询参数
    private List<ReportParamVO> params;
    // 报表列信息
    private List<ReportColumnVO> columns;

    public static ReportVO ofDO(ServeInfoDO rdo) {
        ReportVO vo = new ReportVO();
        BeanUtils.copyProperties(rdo, vo);
        return vo;
    }

    public ServeInfoDO toDO() {
        ServeInfoDO rdo = new ServeInfoDO();
        BeanUtils.copyProperties(this, rdo);
        return rdo;
    }
}
