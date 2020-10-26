package com.oneape.octopus.model.domain.serve;

import com.oneape.octopus.annotation.SortId;
import com.oneape.octopus.model.domain.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

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
     * Group name.
     */
    private String name;
    /**
     * The group icon. support the uri.
     */
    private String icon;
    /**
     * Group description information.
     */
    private String description;
    /**
     * Group sort key.
     */
    @SortId
    @Column(name = "sort_id")
    private Long   sortId;
}
