package com.oneape.octopus.mapper.gateway.provider;

import com.oneape.octopus.domain.gateway.GatewayRouteDO;
import com.oneape.octopus.mapper.BaseSqlProvider;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2021-02-07 14:08.
 * Modify:
 */
public class GatewayRouteSqlProvider extends BaseSqlProvider<GatewayRouteDO> {
    public static final String TABLE_NAME = "gateway_route";

    /**
     * get table name.
     *
     * @return String
     */
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
