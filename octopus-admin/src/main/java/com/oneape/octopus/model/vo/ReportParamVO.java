package com.oneape.octopus.model.vo;

import com.oneape.octopus.dto.serve.ServeParamDTO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
public class ReportParamVO implements Serializable {
    private Long id;
    // 报表Id
    private Long reportId;
    // 字段显示名
    private String showName;
    // 查询sql代入名
    private String paramName;
    // 数据类型
    private String dataType;
    // 默认值
    private String valDefault;
    // 最大值
    private String valMax;
    // 最小值
    private String valMin;
    // 禁止使用值
    private String valForbidden;
    // 是否为必填字段 0 - 否; 1 - 必填
    private Integer mustFillIn;
    // 排序方式 ASC, DESC
    private String orderByType;
    // 提示信息
    private String placeholder;
    // 错误提示信息
    private String errMessage;
    // 依赖关系, 取paramName值,多个以逗号分隔
    private String dependOn;
    // 字段类型; 1 - 内部字段; 2 - 多选字段; 4 - lov选择
    private Integer type;
    // 当字段值内容依赖别一个sql查询结果时(LOV), 填入
    private Long lovReportId;
    // 排序字段
    private Long sortId;

    public static ReportParamVO ofDO(ServeParamDTO pdo) {
        ReportParamVO vo = new ReportParamVO();
        BeanUtils.copyProperties(pdo, vo);
        return vo;
    }

    public ServeParamDTO toDO() {
        ServeParamDTO pdo = new ServeParamDTO();
        BeanUtils.copyProperties(this, pdo);
        return pdo;
    }
}
