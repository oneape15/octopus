package com.oneape.octopus.dto.serve;

import lombok.Data;

import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-06-04 14:56.
 * Modify:
 */
@Data
public class ServeConfigTextDTO {
    /**
     * the serve column information.
     */
    private List<ServeColumnDTO> columns;
    /**
     * the serve param information.
     */
    private List<ServeParamDTO>  params;
    /**
     * the serve dsl sql information.
     */
    private ServeSqlDTO          sql;
    /**
     * the serve help document.
     */
    private RichTextDTO          richText;
    /**
     * the serve visual information.
     */
    private VisualInfoDTO        visualInfo;
}
