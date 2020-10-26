package com.oneape.octopus.controller.peekdata.form;

import com.oneape.octopus.controller.BaseForm;
import com.oneape.octopus.model.domain.peekdata.ImportRecordDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class DataImportForm extends BaseForm implements Serializable {
    private Long recordId;
    @NotNull(message = "数据源不能为空", groups = {ImportCheck.class})
    private Long datasourceId;

    @NotNull(message = "导入表不能为空", groups = {ImportCheck.class})
    private String tableName;

    @NotNull(message = "文件名不能为空", groups = {ImportCheck.class})
    private String fileName; //上传的文件名

    private Integer partitioned; // 是否分区, 1是0否

    private Integer cover; // 是否覆盖, 1是0否

    public interface ImportCheck {

    }

    public ImportRecordDO toDO() {
        ImportRecordDO irdo = new ImportRecordDO();
        BeanUtils.copyProperties(this, irdo);
        irdo.setId(recordId);
        return irdo;
    }


}
