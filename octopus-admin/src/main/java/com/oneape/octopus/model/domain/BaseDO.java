package com.oneape.octopus.model.domain;

import com.oneape.octopus.annotation.AutoUniqueId;
import com.oneape.octopus.annotation.Creator;
import com.oneape.octopus.annotation.Modifier;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class BaseDO implements Serializable {
    /**
     * the primary key.
     */
    @AutoUniqueId
    @Column(name = "id", unique = true, nullable = false)
    private Long    id;
    /**
     * The soft deleted status. 0 - normal； 1 - archive；
     */
    @Column(name = "archive", nullable = false)
    private Integer archive;
    /**
     * creator of the data.
     */
    @Creator
    private Long    creator;
    /**
     * create time of the data.
     */
    private Long    created;
    /**
     * modifier of the data.
     */
    @Modifier
    private Long    modifier;
    /**
     * Recently, update time of the data.
     */
    private Long    modified;
}
