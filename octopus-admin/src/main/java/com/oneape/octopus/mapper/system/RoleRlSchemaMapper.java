package com.oneape.octopus.mapper.system;

import com.oneape.octopus.mapper.system.provider.RoleRlSchemaSqlProvider;
import com.oneape.octopus.model.domain.system.RoleRlSchemaDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-07-06 19:00.
 * Modify:
 */
@Mapper
public interface RoleRlSchemaMapper {

    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @InsertProvider(type = RoleRlSchemaSqlProvider.class, method = "insert")
    int insert(RoleRlSchemaDO model);

    /**
     * Update data by primary key.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = RoleRlSchemaSqlProvider.class, method = "updateById")
    int update(RoleRlSchemaDO model);

    /**
     * Delete data by primary key (soft delete, update archive state).
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = RoleRlSchemaSqlProvider.class, method = "delete")
    int delete(RoleRlSchemaDO model);

    /**
     * Delete data by role Id.
     *
     * @param roleId Long
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = RoleRlSchemaSqlProvider.class, method = "deleteByRoleId")
    int deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * Delete data by role id and datasource id.
     *
     * @param roleId       Long
     * @param datasourceId Long
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = RoleRlSchemaSqlProvider.class, method = "deleteBy")
    int deleteBy(@Param("roleId") Long roleId, @Param("datasourceId") Long datasourceId);

    /**
     * Delete data by id list
     *
     * @param ids List
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = RoleRlSchemaSqlProvider.class, method = "deleteByIds")
    int deleteByIds(@Param("ids") List<Long> ids);

    /**
     * Get the valid schema info.
     *
     * @param roleId Long
     * @return List
     */
    @SelectProvider(type = RoleRlSchemaSqlProvider.class, method = "findByRoleId")
    List<RoleRlSchemaDO> findByRoleId(@Param("roleId") Long roleId);

}
