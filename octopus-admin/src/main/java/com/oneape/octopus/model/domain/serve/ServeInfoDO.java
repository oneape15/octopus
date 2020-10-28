package com.oneape.octopus.model.domain.serve;

import com.oneape.octopus.annotation.SortId;
import com.oneape.octopus.commons.enums.EntityColumn;
import com.oneape.octopus.model.domain.BaseDO;
import com.oneape.octopus.model.dto.serve.ServeConfigTextDTO;
import com.oneape.octopus.model.enums.ServeType;
import com.oneape.octopus.model.enums.VisualType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Serve information table DO.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ServeInfoDO extends BaseDO {
    /**
     * serveId name.
     */
    @EntityColumn(name = "name", nullable = false)
    private String  name;
    /**
     * The serve icon url.
     */
    private String  icon;
    /**
     * Timeliness of report data. 0 - real time, 1 - minutes, 2 - hours, 3 - days, 4 - months;
     */
    @EntityColumn(name = "time_based")
    private Integer timeBased;
    /**
     * {@link ServeType}
     * serve type , report;  interface; eg.
     */
    @EntityColumn(name = "serve_type", nullable = false)
    private String  serveType;
    /**
     * {@link VisualType}
     * the serveId visual type , 1 - table; 2 - line; 4 - bar; eg.
     */
    @EntityColumn(name = "visual_type", nullable = false)
    private Integer visualType;
    /**
     * Sort field
     */
    @SortId
    @EntityColumn(name = "sort_id")
    private Long    sortId;
    /**
     * {@link ServeConfigTextDTO }
     * The serve config text information.
     */
    @EntityColumn(name = "config_text", bigColumn = true)
    private String  configText;
    /**
     * description
     */
    private String  comment;

    public ServeInfoDO(String name) {
        this.name = name;
    }


    public ServeInfoDO(Long id) {
        this.setId(id);
    }

    public ServeInfoDO(Long id, String name) {
        setId(id);
        this.name = name;
    }
}
