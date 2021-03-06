package com.oneape.octopus.domain;

import com.oneape.octopus.commons.annotation.AutoUniqueId;
import com.oneape.octopus.commons.annotation.Creator;
import com.oneape.octopus.commons.annotation.Modifier;
import com.oneape.octopus.commons.annotation.EntityColumn;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class BaseDO implements Serializable {
    /**
     * the primary key.
     */
    @AutoUniqueId
    @EntityColumn(name = "id", unique = true, nullable = false)
    private Long id;
    /**
     * The soft deleted status. 0 - normal； 1 - archive；
     */
    @EntityColumn(name = "archive", nullable = false)
    private Integer archive;
    /**
     * creator of the data.
     */
    @Creator
    private Long creator;
    /**
     * create time of the data.
     */
    private Long created;
    /**
     * modifier of the data.
     */
    @Modifier
    private Long modifier;
    /**
     * Recently, update time of the data.
     */
    private Long modified;
}
