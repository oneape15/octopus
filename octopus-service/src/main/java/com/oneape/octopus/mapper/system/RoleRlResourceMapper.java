package com.oneape.octopus.mapper.system;

import com.oneape.octopus.mapper.system.provider.RoleRlResourceSqlProvider;
import com.oneape.octopus.model.DO.system.RoleRlResourceDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoleRlResourceMapper {
    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @InsertProvider(type = RoleRlResourceSqlProvider.class, method = "insert")
    int insert(RoleRlResourceDO model);

    /**
     * Update data by primary key.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = RoleRlResourceSqlProvider.class, method = "updateById")
    int update(RoleRlResourceDO model);

    /**
     * Delete data by primary key (soft delete, update archive state).
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = RoleRlResourceSqlProvider.class, method = "delete")
    int delete(RoleRlResourceDO model);

    /**
     * Find by primary key.
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = RoleRlResourceSqlProvider.class, method = "findById")
    RoleRlResourceDO findById(@Param("id") Long id);

    /**
     * The query is based on a property in the entity that is not null.
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = RoleRlResourceSqlProvider.class, method = "list")
    List<RoleRlResourceDO> list(@Param("model") RoleRlResourceDO model);

    /**
     * Get the corresponding resource Id information based on the role Id list.
     *
     * @param roleIds List
     * @return List
     */
    @SelectProvider(type = RoleRlResourceSqlProvider.class, method = "getResIdByRoleIds")
    List<RoleRlResourceDO> getResIdByRoleIds(@Param("roleIds") List<Long> roleIds);
}
