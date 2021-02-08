package com.oneape.octopus.admin.controller.datawarehouse.form;

import com.oneape.octopus.admin.controller.BaseForm;
import com.oneape.octopus.domain.schema.DatasourceDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class DatasourceForm extends BaseForm implements Serializable {
    @NotNull(message = "{DatasourceForm.NotNull.id}", groups = {EditCheck.class, KeyCheck.class})
    private Long    id;
    @NotBlank(message = "{DatasourceForm.NotBlank.name}", groups = {AddCheck.class, EditCheck.class})
    private String  name;
    @NotBlank(message = "{DatasourceForm.NotBlank.type}", groups = {AddCheck.class, EditCheck.class})
    private String  type;
    private Integer status;
    @NotBlank(message = "{DatasourceForm.NotBlank.jdbcUrl}", groups = {AddCheck.class, EditCheck.class})
    private String  jdbcUrl;
    @NotBlank(message = "{DatasourceForm.NotBlank.username}", groups = {AddCheck.class, EditCheck.class})
    private String  username;
    private String  password;
    private Integer sync;
    private String cron;
    private Integer timeout;
    private Integer maxPoolSize;
    private Integer minIdle;
    private Integer readOnly;
    private Integer canDdl;
    private String  testSql;
    private String  comment;

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
