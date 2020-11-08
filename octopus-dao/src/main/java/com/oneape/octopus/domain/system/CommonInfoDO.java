package com.oneape.octopus.domain.system;

import com.oneape.octopus.commons.enums.EntityColumn;
import com.oneape.octopus.domain.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Basic information table DO.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CommonInfoDO extends BaseDO {
    /**
     * The parent id.
     */
    @EntityColumn(name = "parent_id")
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


    public CommonInfoDO(Long id) {
        this.setId(id);
    }
}
