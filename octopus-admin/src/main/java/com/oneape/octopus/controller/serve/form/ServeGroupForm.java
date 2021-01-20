package com.oneape.octopus.controller.serve.form;

import com.oneape.octopus.commons.validation.ServeTypeNotNull;
import com.oneape.octopus.domain.serve.ServeGroupDO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-11-16 16:19.
 * Modify:
 */
@Data
public class ServeGroupForm implements Serializable {
    /**
     * The parent node id.
     */
    @NotNull(message = "The parent group Id is empty.", groups = {EditCheck.class, AddCheck.class})
    private Long   parentId;
    @NotNull(message = "The serve group Id is empty.", groups = {EditCheck.class, KeyCheck.class})
    private Long   id;
    @NotBlank(message = "The serve group name is empty.", groups = {AddCheck.class, EditCheck.class})
    private String name;
    @ServeTypeNotNull(message = "Invalid ServeType value.", groups = {AddCheck.class, EditCheck.class, TreeCheck.class})
    private String serveType;

    private String icon;

    private String comment;

    // add children count size.
    private boolean addChildrenSize = false;
    // add archive node.
    private boolean addArchiveNode  = false;
    // add root node.
    private boolean addRootNode     = false;
    // add personal node.
    private boolean addPersonalNode = false;

    public interface AddCheck {
    }

    public interface EditCheck {
    }

    public interface KeyCheck {
    }

    public interface TreeCheck {
    }

    public ServeGroupDO toDO() {
        ServeGroupDO rdo = new ServeGroupDO();
        BeanUtils.copyProperties(this, rdo);
        return rdo;
    }
}