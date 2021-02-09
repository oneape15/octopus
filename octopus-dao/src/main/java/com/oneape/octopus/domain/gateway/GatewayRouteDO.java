package com.oneape.octopus.domain.gateway;

import com.oneape.octopus.commons.enums.EntityColumn;
import com.oneape.octopus.domain.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * gateway information manage do.
 * Created by oneape<oneape15@163.com>
 * Created 2021-02-07 11:39.
 * Modify:
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
public class GatewayRouteDO extends BaseDO {
    /**
     * the route unique id
     */
    @EntityColumn(name = "route_id")
    private String  routeId;
    /**
     * The URI that the routing rule forwards
     */
    private String  uri;
    /**
     * Routing assertion collection configuration
     */
    private String  predicates;
    /**
     * Routing filter collection configuration
     */
    private String  filters;
    /**
     * Route execution sequence
     */
    private Integer order;
    /**
     * state, 0 - invalid; 1 - valid
     */
    private Integer state;
    /**
     * metadata configuration information.
     */
    private String  metadata;
    /**
     * the route comment
     */
    private String  comment;

    public GatewayRouteDO(Long id) {
        this.setId(id);
    }
}
