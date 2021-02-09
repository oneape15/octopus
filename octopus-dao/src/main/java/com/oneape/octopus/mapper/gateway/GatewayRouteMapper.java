package com.oneape.octopus.mapper.gateway;

import com.oneape.octopus.domain.gateway.GatewayRouteDO;
import com.oneape.octopus.mapper.gateway.provider.GatewayRouteSqlProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2021-02-07 14:07.
 * Modify:
 */
public interface GatewayRouteMapper {

    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @InsertProvider(type = GatewayRouteSqlProvider.class, method = "insert")
    int insert(GatewayRouteDO model);

    /**
     * Update data by primary key.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = GatewayRouteSqlProvider.class, method = "updateById")
    int update(GatewayRouteDO model);

    /**
     * Delete data by primary key (soft delete, update archive state).
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = GatewayRouteSqlProvider.class, method = "deleteById")
    int delete(GatewayRouteDO model);

    /**
     * Finding by primary key.
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = GatewayRouteSqlProvider.class, method = "findById")
    GatewayRouteDO findById(@Param("id") Long id);

    /**
     * The query is based on a property in the entity that is not null.
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = GatewayRouteSqlProvider.class, method = "list")
    List<GatewayRouteDO> list(@Param("model") GatewayRouteDO model);

    /**
     * Determine if the same routeID exists。
     *
     * @param routeId  String
     * @param filterId Long
     * @return int
     */
    @SelectProvider(type = GatewayRouteSqlProvider.class, method = "hasExistRouteId")
    int hasExistRouteId(@Param("routeId") String routeId, @Param("filterId") Long filterId);
}
