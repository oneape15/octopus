package com.oneape.octopus.model.DO.system;

import com.oneape.octopus.model.DO.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;

/**
 * Basic information table DO.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CommonInfoDO extends BaseDO {
    /**
     * The parent id.
     */
    @Column(name = "parent_id")
    private Long   parentId;
    /**
     * Basic information classification
     */
    private String classify;
    /**
     * The common information key.
     */
    private String key;
    /**
     * The common information value.
     */
    private String value;
}
