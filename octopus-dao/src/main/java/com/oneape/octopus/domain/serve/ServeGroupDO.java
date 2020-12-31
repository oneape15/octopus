package com.oneape.octopus.domain.serve;

import com.oneape.octopus.commons.annotation.SortId;
import com.oneape.octopus.commons.enums.EntityColumn;
import com.oneape.octopus.commons.enums.ServeType;
import com.oneape.octopus.domain.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-10-26 11:13.
 * Modify:
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ServeGroupDO extends BaseDO {
    /**
     * The parent node id.
     */
    @EntityColumn(name = "parent_id")
    private Long   parentId;
    /**
     * Group name.
     */
    private String name;
    /**
     * The group icon. support the uri.
     */
    private String icon;
    /**
     * {@link ServeType}
     * serve type , report;  interface; eg.
     */
    @EntityColumn(name = "serve_type", nullable = false)
    private String serveType;
    /**
     * Group sort key.
     */
    @SortId
    @EntityColumn(name = "sort_id")
    private Long   sortId;
    /**
     * Group description information.
     */
    private String comment;

    public ServeGroupDO(Long id) {
        this.setId(id);
    }
}

