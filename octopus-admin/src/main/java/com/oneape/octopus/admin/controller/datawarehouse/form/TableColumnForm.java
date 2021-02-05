package com.oneape.octopus.admin.controller.datawarehouse.form;

import com.oneape.octopus.domain.schema.TableColumnDO;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-06-01 15:11.
 * Modify:
 */
@Data
public class TableColumnForm implements Serializable {
    @NotNull(message = "The column primary key is null.", groups = {InfoCheck.class})
    private Long   id;
    private String dataType;
    private String alias;
    private String comment;

    public interface InfoCheck {
    }

    public TableColumnDO toDO() {
        TableColumnDO tcdo = new TableColumnDO();
        tcdo.setId(id);
        tcdo.setDataType(dataType);
        tcdo.setAlias(alias);
        tcdo.setComment(comment);
        return tcdo;
    }
}
