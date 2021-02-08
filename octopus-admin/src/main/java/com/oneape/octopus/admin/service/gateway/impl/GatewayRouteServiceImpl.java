package com.oneape.octopus.admin.service.gateway.impl;

import com.oneape.octopus.admin.service.gateway.GatewayRouteService;
import com.oneape.octopus.domain.gateway.GatewayRouteDO;
import com.oneape.octopus.mapper.gateway.GatewayRouteMapper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2021-02-07 15:19.
 * Modify:
 */
@Slf4j
@Service
public class GatewayRouteServiceImpl implements GatewayRouteService {

    @Resource
    private GatewayRouteMapper gatewayRouteMapper;
    @Resource
    private RedissonClient     redissonClient;

    /**
     * save data to table.
     * <p>
     * If the Model property ID is not null, the update operation is performed, or the insert operation is performed。
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int save(GatewayRouteDO model) {
        return 0;
    }

    /**
     * Delete by primary key Id.
     *
     * @param id Long
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int deleteById(Long id) {
        return 0;
    }

    /**
     * Get the model information by the primary key.
     *
     * @param id Long
     * @return T
     */
    @Override
    public GatewayRouteDO findById(Long id) {
        return null;
    }

    /**
     * Query against an object.
     *
     * @param model T
     * @return List
     */
    @Override
    public List<GatewayRouteDO> find(GatewayRouteDO model) {
        return null;
    }
}
