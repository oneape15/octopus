package com.oneape.octopus.dto.serve;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ServeSqlDTO implements Serializable {
    /**
     * dependency on the data source id
     */
    private Long datasourceId;
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
    private String text;
    /**
     * description
     */
    private String comment;

}
