package com.oneape.octopus.controller.report.form;

import com.oneape.octopus.controller.BaseForm;
import com.oneape.octopus.model.DO.report.ReportDO;
import com.oneape.octopus.model.VO.ReportColumnVO;
import com.oneape.octopus.model.VO.ReportParamVO;
import com.oneape.octopus.model.VO.ReportVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReportForm extends BaseForm implements Serializable {
    @NotNull(message = "报表主键不能为空", groups = {EditCheck.class, KeyCheck.class})
    private Long reportId;
    // 报表编码全局唯一
    private String code;
    // 名称
    @NotBlank(message = "报表名称不能为空", groups = {AddCheck.class, EditCheck.class})
    private String name;
    // 图标
    private String icon;
    // 报表类型
    @NotBlank(message = "报表类型不能为空", groups = {AddCheck.class, EditCheck.class})
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
    // 需要过滤的报表Id列表
    private List<Long> filterIds;
    // 携带特定项
    private String fixOption;
    // 查询参数列表
    private List<ReportParamVO> reportParams;
    // 报表字段列表
    private List<ReportColumnVO> reportColumns;

    public interface AddCheck {
    }

    public interface EditCheck {

    }

    public interface KeyCheck {
    }


    public ReportVO toVO() {
        ReportVO vo = new ReportVO();
        BeanUtils.copyProperties(this, vo);
        vo.setId(reportId);
        return vo;
    }

    public ReportDO toDO() {
        ReportDO rdo = new ReportDO();
        BeanUtils.copyProperties(this, rdo);
        rdo.setId(reportId);
        return rdo;
    }
}
