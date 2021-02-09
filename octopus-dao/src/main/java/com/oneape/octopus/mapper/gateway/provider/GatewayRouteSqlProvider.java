package com.oneape.octopus.mapper.gateway.provider;

import com.oneape.octopus.commons.enums.Archive;
import com.oneape.octopus.domain.gateway.GatewayRouteDO;
import com.oneape.octopus.mapper.BaseSqlProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.ArrayList;
import java.util.List;

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

    public String hasExistRouteId(@Param("routeId") String routeId, @Param("filterId") Long filterId) {
        List<String> wheres = new ArrayList<>();
        wheres.add(FIELD_ARCHIVE + " = " + Archive.NORMAL.value());
        wheres.add("route_id = #{routeId}");
        if (filterId != null) {
            wheres.add("id != #{filterId}");
        }

        return new SQL()
                .SELECT("COUNT(0)")
                .FROM(getTableName())
                .WHERE(wheres.toArray(new String[wheres.size()]))
                .toString();
    }
}
