package com.oneape.octopus.model.DO.report;

import com.oneape.octopus.model.DO.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-06-04 15:19.
 * Modify:
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class HelpDocumentDO extends BaseDO {
    /**
     * Business types. 0 - report; 1 - dashboard; 2 - interface;
     */
    @Column(name = "biz_type")
    private Integer bizType;
    /**
     * the business primary key
     */
    @Column(name = "biz_id")
    private Long    bizId;
    /**
     * the rich text content.
     */
    private String  text;
}
