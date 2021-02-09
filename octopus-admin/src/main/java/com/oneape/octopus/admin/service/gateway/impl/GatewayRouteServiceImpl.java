package com.oneape.octopus.admin.service.gateway.impl;

import com.google.common.base.Preconditions;
import com.oneape.octopus.admin.config.I18nMsgConfig;
import com.oneape.octopus.admin.service.gateway.GatewayRouteService;
import com.oneape.octopus.commons.cause.BizException;
import com.oneape.octopus.domain.gateway.GatewayRouteDO;
import com.oneape.octopus.domain.serve.ServeGroupDO;
import com.oneape.octopus.domain.serve.ServeRlGroupDO;
import com.oneape.octopus.mapper.gateway.GatewayRouteMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
        Preconditions.checkNotNull(model, I18nMsgConfig.getMessage("route.info.null"));
        Preconditions.checkArgument(
                StringUtils.isNotBlank(model.getUri()),
                I18nMsgConfig.getMessage("route.uri.empty")
        );
        Preconditions.checkArgument(
                StringUtils.isNotBlank(model.getRouteId()),
                I18nMsgConfig.getMessage("route.routeId.empty"));

        // whether is updating.
        boolean isUpdate = false;
        if (model.getId() != null && model.getId() > 0) {
            Preconditions.checkNotNull(findById(model.getId()),
                    I18nMsgConfig.getMessage("route.id.invalid"));
            isUpdate = true;
        }

        Preconditions.checkArgument(
                gatewayRouteMapper.hasExistRouteId(model.getRouteId(), isUpdate ? model.getId() : null) == 0,
                I18nMsgConfig.getMessage("route.routeId.restrict")
        );

        int status;
        if (isUpdate) {
            status = gatewayRouteMapper.update(model);
        } else {
            status = gatewayRouteMapper.insert(model);

        }
        return status;
    }

    /**
     * Delete by primary key Id.
     *
     * @param id Long
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int deleteById(Long id) {
        Preconditions.checkNotNull(findById(id),
                I18nMsgConfig.getMessage("route.id.invalid"));
        return gatewayRouteMapper.delete(new GatewayRouteDO(id));
    }

    /**
     * Get the model information by the primary key.
     *
     * @param id Long
     * @return T
     */
    @Override
    public GatewayRouteDO findById(Long id) {
        return gatewayRouteMapper.findById(id);
    }

    /**
     * Query against an object.
     *
     * @param model T
     * @return List
     */
    @Override
    public List<GatewayRouteDO> find(GatewayRouteDO model) {
        return gatewayRouteMapper.list(model);
    }
}
