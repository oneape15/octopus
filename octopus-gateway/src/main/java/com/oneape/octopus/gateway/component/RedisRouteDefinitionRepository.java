package com.oneape.octopus.gateway.component;

import com.alibaba.fastjson.JSON;
import org.redisson.api.RedissonClient;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2021-02-02 14:50.
 * Modify:
 */
@Component
public class RedisRouteDefinitionRepository implements RouteDefinitionRepository {

    public static final String GATEWAY_ROUTES = "gateway_routes";

    @Resource
    private RedissonClient      redissonClient;

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        List<RouteDefinition> routeDefinitions = new ArrayList<>();
        redissonClient.getList(GATEWAY_ROUTES)
                .forEach(rd -> routeDefinitions.add(JSON.parseObject(rd.toString(), RouteDefinition.class)));
        return Flux.fromIterable(routeDefinitions);
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> mono) {
        return null;
    }

    @Override
    public Mono<Void> delete(Mono<String> mono) {
        return null;
    }
}
