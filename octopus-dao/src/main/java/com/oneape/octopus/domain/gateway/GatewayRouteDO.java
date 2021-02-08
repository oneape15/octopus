package com.oneape.octopus.domain.gateway;

import com.oneape.octopus.commons.enums.EntityColumn;
import com.oneape.octopus.domain.BaseDO;
import lombok.Data;

/**
 * gateway information manage do.
 * Created by oneape<oneape15@163.com>
 * Created 2021-02-07 11:39.
 * Modify:
 */
@Data
public class GatewayRouteDO extends BaseDO {
    /**
     * the route unique id
     */
    @EntityColumn(name = "route_id")
    private String  routeId;
    private String  uri;
    private String  predicates;
    private String  filters;
    private Integer order;

    private String metadata;
}
