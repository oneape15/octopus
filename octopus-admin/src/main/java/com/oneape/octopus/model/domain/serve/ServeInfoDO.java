package com.oneape.octopus.model.domain.serve;

import com.oneape.octopus.annotation.SortId;
import com.oneape.octopus.model.domain.BaseDO;
import com.oneape.octopus.model.dto.serve.ServeConfigTextDTO;
import com.oneape.octopus.model.enums.ServeType;
import com.oneape.octopus.model.enums.VisualType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

/**
 * Serve information table DO.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ServeInfoDO extends BaseDO {
    /**
     * report name.
     */
    @Column(name = "name", nullable = false)
    private String  name;
    /**
     * Timeliness of report data. 0 - real time, 1 - minutes, 2 - hours, 3 - days, 4 - months;
     */
    @Column(name = "time_based")
    private Integer timeBased;
    /**
     * {@link ServeType}
     * serve type , report;  interface; eg.
     */
    @Column(name = "serve_type", nullable = false)
    private String  serveType;
    /**
     * {@link VisualType}
     * the report visual type , 1 - table; 2 - line; 4 - bar; eg.
     */
    @Column(name = "visual_type", nullable = false)
    private Integer visualType;
    /**
     * Sort field
     */
    @SortId
    @Column(name = "sort_id")
    private Long    sortId;
    /**
     * {@link ServeConfigTextDTO }
     * The serve config text information.
     */
    @Column(name = "config_text")
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
