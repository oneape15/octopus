package com.oneape.octopus.query.data;

import com.oneape.octopus.commons.enums.FileType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

/**
 * Export data param.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ExportDataParam extends ExecParam {
    /**
     * Field alias list.
     */
    private Map<String, String> field2Alias;
    /**
     * The local file title.
     */
    private String title;
    /**
     * Additional information.
     */
    private List<String> tips;

    /**
     * The file type.
     */
    private FileType exportFileType = FileType.CSV;
    /**
     * Whether a compressed file tag is required.
     */
    private Boolean needCompressFile = Boolean.TRUE;
}
