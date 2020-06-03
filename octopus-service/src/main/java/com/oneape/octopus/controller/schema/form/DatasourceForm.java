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
    @NotNull(message = "The data source primary key id is empty.", groups = {EditCheck.class, KeyCheck.class})
    private Long    id;
    @NotBlank(message = "The data source name is empty.", groups = {AddCheck.class, EditCheck.class})
    private String  name;
    @NotBlank(message = "The data source URL is empty.", groups = {AddCheck.class, EditCheck.class})
    private String  jdbcUrl;
    @NotBlank(message = "The data source login name is empty.", groups = {AddCheck.class, EditCheck.class})
    private String  username;
    private Integer status;
    private Integer timeout;
    private String  testSql;
    private String  password;
    private String  comment;
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
