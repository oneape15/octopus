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
     * The rich text show type , html, text, markdown eg.
     */
    private String showType;
    /**
     * the rich text content.
     */
    private String text;
}
