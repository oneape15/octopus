package com.oneape.octopus.gateway.service;

import org.springframework.cloud.gateway.route.RouteDefinition;

/**
 * Dynamic routing service
 * Created by oneape<oneape15@163.com>
 * Created 2021-02-07 10:09.
 * Modify:
 */
public interface DynamicRouteService {

    /**
     * add the routing
     *
     * @param definition RouteDefinition
     * @return int 1 - success; 0 - fail
     */
    int addRoute(RouteDefinition definition);

    /**
     * update the routing
     *
     * @param definition RouteDefinition
     * @return int 1 - success; 0 - fail
     */
    int updateRoute(RouteDefinition definition);

    /**
     * Delete the routing
     *
     * @param id String
     * @return int 1 - success; 0 - fail
     */
    int deleteRoute(String id);
}
