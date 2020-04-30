package com.oneape.octopus.controller.schema.form;

import com.oneape.octopus.controller.BaseForm;
import com.oneape.octopus.model.DO.schema.DatasourceDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class DatasourceForm extends BaseForm implements Serializable {
    @NotNull(message = "主键不能为空", groups = {EditCheck.class, KeyCheck.class})
    private Long    id;
    // 数据库别名
    @NotBlank(message = "数据源名称不能为空", groups = {AddCheck.class, EditCheck.class})
    private String  name;
    // 数据源地址
    @NotBlank(message = "数据源URL不能为空", groups = {AddCheck.class, EditCheck.class})
    private String  jdbcUrl;
    // 驱动class名称
    @NotBlank(message = "数据源驱动不能为空", groups = {AddCheck.class, EditCheck.class})
    private String  jdbcDriver;
    // 数据源用户名
    @NotBlank(message = "数据源登录名不能为空", groups = {AddCheck.class, EditCheck.class})
    private String  username;
    // 数据源状态 0 - 可用; 1 - 不可用;
    private Integer status;
    // 超时时间
    private Integer timeout;
    // 测试SQL
    private String  testSql;
    // 数据源登录密码
    private String  password;
    // 描述
    private String  comment;
    // 数据源类型
    private String  type;

    public interface AddCheck {
    }

    public interface EditCheck {
    }

    public interface KeyCheck {
    }

    public DatasourceDO toDO() {
        DatasourceDO ddo = new DatasourceDO();
        BeanUtils.copyProperties(this, ddo);
        return ddo;
    }

}
