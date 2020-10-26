package com.oneape.octopus.model.dto.serve;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-06-04 15:19.
 * Modify:
 */
@Data
@NoArgsConstructor
public class RichTextDTO {
    /**
     * Business types. 0 - report; 1 - dashboard; 2 - interface;
     */
    private String bizType;
    /**
     * the business primary key
     */
    private Long   bizId;
    /**
     * the rich text content.
     */
    private String text;
}
