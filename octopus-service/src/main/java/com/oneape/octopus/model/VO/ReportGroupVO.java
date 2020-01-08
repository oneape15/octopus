package com.oneape.octopus.model.VO;

import com.oneape.octopus.model.DO.report.ReportGroupDO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
public class ReportGroupVO implements Serializable {
    private Long id;
    // 父节点Id
    private Long parentId;
    // 报表组名称
    private String name;
    // 报表组图标
    private String icon;
    // 状态 0 - 正常； 1 - 上线中
    private Integer status;
    // 所在层级
    private Integer level;
    // 拥有者
    private Long owner;
    // 排序Id
    private Long sortId;
    // 描述信息
    private String comment;

    public static ReportGroupVO ofDO(ReportGroupDO rgdo) {
        ReportGroupVO vo = new ReportGroupVO();
        BeanUtils.copyProperties(rgdo, vo);
        return vo;
    }

    public ReportGroupDO toDO() {
        ReportGroupDO rgdo = new ReportGroupDO();
        BeanUtils.copyProperties(this, rgdo);
        return rgdo;
    }
}
