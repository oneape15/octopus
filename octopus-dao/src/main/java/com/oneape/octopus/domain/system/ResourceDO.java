package com.oneape.octopus.domain.system;

import com.oneape.octopus.commons.annotation.SortId;
import com.oneape.octopus.commons.annotation.EntityColumn;
import com.oneape.octopus.domain.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Resource information table DO.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ResourceDO extends BaseDO {
    /**
     * The parent node id.
     */
    @EntityColumn(name = "parent_id")
    private Long    parentId;
    /**
     * The resource name.
     */
    private String  name;
    /**
     * The resource code.
     */
    private String  code;
    /**
     * The resource icon
     */
    private String  icon;
    /**
     * The resource type. 0 - menu; 1 - button.
     */
    private Integer type;
    /**
     * The resource path.
     */
    private String  path;
    /**
     * The resource sort field.
     */
    @SortId
    @EntityColumn(name = "sort_id")
    private Long    sortId;
    /**
     * description
     */
    private String  comment;

    public ResourceDO(Long id) {
        this.setId(id);
    }
}
