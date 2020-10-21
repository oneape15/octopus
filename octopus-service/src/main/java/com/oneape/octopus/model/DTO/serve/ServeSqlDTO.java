package com.oneape.octopus.model.DTO.serve;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ServeSqlDTO {
    /**
     * dependency on the data source id
     */
    private Long    datasourceId;
    /**
     * Cache time (seconds)
     */
    private Integer cachedTime;
    /**
     * timeout time (seconds)
     */
    private Integer timeout;
    /**
     * dsl sql content
     */
    private String  text;
    /**
     * description
     */
    private String  comment;

}
