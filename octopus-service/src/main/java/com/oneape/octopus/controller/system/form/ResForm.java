package com.oneape.octopus.controller.system.form;

import com.oneape.octopus.controller.BaseForm;
import com.oneape.octopus.model.DO.system.ResourceDO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class ResForm extends BaseForm implements Serializable {
    // 主键
    @NotNull(message = "主键不能为空", groups = {EditCheck.class, KeyCheck.class})
    private Long resId;
    // 父节点Id
    @NotNull(message = "父节点不能为空", groups = {EditCheck.class, AddCheck.class})
    private Long parentId;
    // 资源名称
    @NotBlank(message = "资源名为空", groups = {EditCheck.class, AddCheck.class})
    private String name;
    // 资源图标
    private String icon;
    // 0 - 菜单; 1 - 资源项
    @NotNull(message = "资源类型不能为空", groups = {EditCheck.class, AddCheck.class})
    private Integer type;
    // 资源路径
    @NotBlank(message = "资源路径名为空", groups = {EditCheck.class, AddCheck.class})
    private String path;
    // 权限编码
    private String authCode;
    // 排序Id
    private Long sortId;
    // 描述
    private String comment;


    public interface AddCheck {
    }

    public interface EditCheck {
    }

    public interface KeyCheck {
    }

    public ResourceDO toDO() {
        ResourceDO rdo = new ResourceDO();
        BeanUtils.copyProperties(this, rdo);
        rdo.setId(resId);
        return rdo;
    }
}
