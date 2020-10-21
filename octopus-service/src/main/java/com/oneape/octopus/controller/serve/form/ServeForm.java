package com.oneape.octopus.controller.serve.form;

import com.oneape.octopus.controller.BaseForm;
import com.oneape.octopus.model.DO.serve.ServeInfoDO;
import com.oneape.octopus.model.DTO.serve.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ServeForm extends BaseForm implements Serializable {
    @NotNull(message = "The serve Id is empty.", groups = {EditCheck.class, KeyCheck.class})
    private Long                 serveId;
    @NotBlank(message = "The serve name is empty.", groups = {AddCheck.class, EditCheck.class})
    private String               name;
    @NotNull(message = "The serve type is empty.", groups = {AddCheck.class, EditCheck.class})
    private Integer              reportType;
    @NotNull(message = "The serve visual type is empty.", groups = {AddCheck.class, EditCheck.class})
    private Integer              visualType;
    private String               xAxis;
    private String               yAxis;
    private Integer              paramLabelLen;
    private Integer              paramMediaLen;
    private Long                 sortId;
    private String               comment;
    private List<ServeParamDTO>  params;
    private List<ServeColumnDTO> columns;
    private ServeSqlDTO          dsl;
    private RichTextDTO          richText;

    public interface AddCheck {
    }

    public interface EditCheck {
    }

    public interface KeyCheck {
    }

    public ServeInfoDO toDO() {
        ServeInfoDO rdo = new ServeInfoDO();
        BeanUtils.copyProperties(this, rdo);
        rdo.setId(serveId);
        return rdo;
    }
}
