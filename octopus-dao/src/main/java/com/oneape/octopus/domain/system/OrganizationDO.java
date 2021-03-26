package com.oneape.octopus.domain.system;

import com.oneape.octopus.commons.annotation.EntityColumn;
import com.oneape.octopus.domain.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2021-01-25 14:40.
 * Modify:
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrganizationDO extends BaseDO {
    /**
     * The parent node id
     */
    @EntityColumn(name = "parent_id")
    private Long   parentId;
    /**
     * Organizational architecture is uniquely coded.
     */
    private String code;
    /**
     * Organizational architecture is uniquely name.
     */
    private String name;
    /**
     * ID of department head.
     */
    @EntityColumn(name = "dept_head_user_id")
    private Long   deptHeadUserId;
    /**
     * description
     */
    private String comment;

    public OrganizationDO(Long id) {
        setId(id);
    }
}
