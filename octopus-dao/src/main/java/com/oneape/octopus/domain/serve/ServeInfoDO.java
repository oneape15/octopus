package com.oneape.octopus.domain.serve;

import com.oneape.octopus.commons.annotation.SortId;
import com.oneape.octopus.commons.annotation.EntityColumn;
import com.oneape.octopus.commons.enums.ServeStatusType;
import com.oneape.octopus.commons.enums.ServeType;
import com.oneape.octopus.commons.enums.VisualType;
import com.oneape.octopus.domain.BaseDO;
import com.oneape.octopus.dto.serve.ServeConfigTextDTO;
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
     * deploy datasource id.
     */
    @EntityColumn(name = "datasource_id")
    private Long datasourceId;
    /**
     * serveId name.
     */
    @EntityColumn(name = "name", nullable = false)
    private String name;
    /**
     * Serve code.
     */
    @EntityColumn(name = "code", nullable = false)
    private String code;
    /**
     * Timeliness of serve data. 0 - real time, 1 - minutes, 2 - hours, 3 - days, 4 - months;
     */
    @EntityColumn(name = "time_based")
    private Integer timeBased;
    /**
     * {@link ServeType}
     * serve type , report;  interface; eg.
     */
    @EntityColumn(name = "serve_type", nullable = false)
    private String serveType;
    /**
     * {@link VisualType}
     * the serveId visual type , 1 - table; 2 - line; 4 - bar; eg.
     */
    @EntityColumn(name = "visual_type", nullable = false)
    private Integer visualType;
    /**
     * {@link ServeStatusType}
     * the serve status
     */
    private String status;
    /**
     * Sort field
     */
    @SortId
    @EntityColumn(name = "sort_id")
    private Long sortId;
    /**
     * The owner id of the serve.
     */
    @EntityColumn(name = "owner_id")
    private Long ownerId;
    /**
     * {@link ServeConfigTextDTO }
     * The serve config text information.
     */
    @EntityColumn(name = "config_text", bigColumn = true)
    private String configText;
    /**
     * description
     */
    private String comment;

    public ServeInfoDO(Long id) {
        this.setId(id);
    }

}
