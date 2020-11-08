package com.oneape.octopus.controller.serve.form;

import com.alibaba.fastjson.JSON;
import com.oneape.octopus.controller.BaseForm;
import com.oneape.octopus.domain.serve.ServeInfoDO;
import com.oneape.octopus.dto.serve.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ServeForm extends BaseForm implements Serializable {
    @NotNull(message = "The serve Id is empty.", groups = {EditCheck.class, KeyCheck.class})
    private Long    id;
    @NotBlank(message = "The serve name is empty.", groups = {AddCheck.class, EditCheck.class})
    private String  name;
    @NotNull(message = "The serve type is empty.", groups = {AddCheck.class, EditCheck.class})
    private String  serveType;
    @NotNull(message = "The serve visual type is empty.", groups = {AddCheck.class, EditCheck.class})
    private Integer visualType;

    private Long   sortId;
    private String comment;

    private List<ServeParamDTO>  params;
    private List<ServeColumnDTO> columns;
    private ServeSqlDTO          sql;
    private RichTextDTO          richText;
    private VisualInfoDTO        visualInfo;

    public interface AddCheck {
    }

    public interface EditCheck {
    }

    public interface KeyCheck {
    }

    public ServeInfoDO toDO() {
        ServeInfoDO rdo = new ServeInfoDO();
        BeanUtils.copyProperties(this, rdo);

        ServeConfigTextDTO config = new ServeConfigTextDTO();
        config.setColumns(columns);
        config.setParams(params);
        config.setSql(sql);
        config.setRichText(richText);
        config.setVisualInfo(visualInfo);
        String text = JSON.toJSONString(config);
        if (!StringUtils.equalsIgnoreCase(text, "{}")) {
            rdo.setConfigText(text);
        }

        return rdo;
    }
}
