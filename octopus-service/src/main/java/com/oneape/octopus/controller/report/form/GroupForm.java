package com.oneape.octopus.controller.report.form;

import com.oneape.octopus.controller.BaseForm;
import com.oneape.octopus.model.DO.report.ReportGroupDO;
import com.oneape.octopus.model.VO.ReportGroupVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class GroupForm extends BaseForm implements Serializable {
    @NotNull(message = "报表组Id为空", groups = {EditCheck.class, KeyCheck.class})
    private Long groupId;
    // 父节点Id
    @NotNull(message = "父节点Id为空", groups = {AddCheck.class, EditCheck.class})
    private Long parentId;
    // 报表组名称
    @NotBlank(message = "报表组名称为空", groups = {AddCheck.class, EditCheck.class})
    private String name;
    // 报表组图标
    private String icon;
    // 状态 0 - 正常； 1 - 上线中
    private Integer status;
    // 拥有者
    private Long owner;

    public interface AddCheck {
    }

    public interface EditCheck {

    }

    public interface KeyCheck {
    }

    public ReportGroupVO toVO() {
        ReportGroupVO vo = new ReportGroupVO();
        BeanUtils.copyProperties(this, vo);
        vo.setId(groupId);
        return vo;
    }

    public ReportGroupDO toDO() {
        ReportGroupDO gdo = new ReportGroupDO();
        BeanUtils.copyProperties(this, gdo);
        gdo.setId(groupId);
        return gdo;
    }
}
