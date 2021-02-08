package com.oneape.octopus.gateway.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * Dynamic routing service.
 * Created by oneape<oneape15@163.com>
 * Created 2021-02-07 10:08.
 * Modify:
 */
@Slf4j
@Service
public class DynamicRouteServiceImpl implements DynamicRouteService, ApplicationEventPublisherAware {

    @Resource
    private RouteDefinitionWriter routeDefinitionWriter;

    private ApplicationEventPublisher publisher;

    private void notifyChanged() {
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    /**
     * add the routing
     *
     * @param definition RouteDefinition
     * @return int 1 - success; 0 - fail
     */
    @Override
    public int addRoute(RouteDefinition definition) {
        try {
            routeDefinitionWriter.save(Mono.just(definition)).subscribe();

            // refresh routes
            notifyChanged();
        } catch (Exception e) {
            log.error("add route fail.", e);
            return 0;
        }

        return 1;
    }

    /**
     * update the routing
     *
     * @param definition RouteDefinition
     * @return int 1 - success; 0 - fail
     */
    @Override
    public int updateRoute(RouteDefinition definition) {
        try {
            // delete the route.
            int status = deleteRoute(definition.getId());
            if (status == 0) {
                return 0;
            }

            // add the route.
            addRoute(definition);
        } catch (Exception e) {
            log.error("update route fail.", e);
            return 0;
        }

        return 1;
    }

    /**
     * Delete the routing
     *
     * @param id String
     * @return int 1 - success; 0 - fail
     */
    @Override
    public int deleteRoute(String id) {
        try {
            routeDefinitionWriter.delete(Mono.just(id)).subscribe();
            // refresh routes
            notifyChanged();
        } catch (Exception e) {
            log.error("delete route fail.", e);
            return 0;
        }

        return 1;
    }
}
